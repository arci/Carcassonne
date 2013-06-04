package it.polimi.carcassonne.client.controller.swing.listener;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Take a screenshot of the game grid
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class ScreenshotListener implements ActionListener {

	private static List<String> types = Arrays.asList(ImageIO
			.getWriterFileSuffixes());

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage screen = createImage(SwingViewManager.getManager()
				.getGrid());

		JFileChooser fileChooser = new JFileChooser();

		int n = fileChooser.showSaveDialog(fileChooser);
		if (n == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			try {
				writeImage(screen, f.getAbsolutePath() + ".png");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null,
						"IO Exception" + e1.toString());
			}
		}
	}

	/**
	 * Create a BufferedImage for Swing components. The entire component will be
	 * captured to an image.
	 * 
	 * @param component
	 *            Swing component to create image from
	 * 
	 * @return image the image for the given region
	 */
	public static BufferedImage createImage(JComponent component) {
		Dimension d = component.getSize();

		if (d.width == 0 || d.height == 0) {
			d = component.getPreferredSize();
			component.setSize(d);
		}

		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return ScreenshotListener.createImage(component, region);
	}

	/**
	 * Create a BufferedImage for Swing components. All or part of the component
	 * can be captured to an image.
	 * 
	 * @param component
	 *            Swing component to create image from
	 * 
	 * @param region
	 *            The region of the component to be captured to an image
	 * 
	 * @return image the image for the given region
	 */
	public static BufferedImage createImage(JComponent component,
			Rectangle region) {
		// Make sure the component has a size and has been layed out.
		// (necessary check for components not added to a realized frame)

		if (!component.isDisplayable()) {
			Dimension d = component.getSize();

			if (d.width == 0 || d.height == 0) {
				d = component.getPreferredSize();
				component.setSize(d);
			}

			layoutComponent(component);
		}

		BufferedImage image = new BufferedImage(region.width, region.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		// Paint a background for non-opaque components,
		// otherwise the background will be black

		if (!component.isOpaque()) {
			g2d.setColor(component.getBackground());
			g2d.fillRect(region.x, region.y, region.width, region.height);
		}

		g2d.translate(-region.x, -region.y);
		component.paint(g2d);
		g2d.dispose();
		return image;
	}

	/**
	 * Write a BufferedImage to a File.
	 * 
	 * @param image
	 *            image to be written
	 * @param fileName
	 *            name of file to be created
	 * @exception IOException
	 *                if an error occurs during writing
	 */
	public static void writeImage(BufferedImage image, String fileName)
			throws IOException {
		if (fileName == null) {
			return;
		}

		int offset = fileName.lastIndexOf('.');

		if (offset == -1) {
			String message = "file suffix was not specified";
			throw new IOException(message);
		}

		String type = fileName.substring(offset + 1);

		if (types.contains(type)) {
			ImageIO.write(image, type, new File(fileName));
		} else {
			String message = "unknown writer file suffix (" + type + ")";
			throw new IOException(message);
		}
	}

	static void layoutComponent(Component component) {
		synchronized (component.getTreeLock()) {
			component.doLayout();

			if (component instanceof Container) {
				for (Component child : ((Container) component).getComponents()) {
					layoutComponent(child);
				}
			}
		}
	}
}
