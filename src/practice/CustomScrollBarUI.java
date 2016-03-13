package practice;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class CustomScrollBarUI extends MetalScrollBarUI {
	private final Image imageThumb = createMockImage(32, 32, ColorSchema.DARK_GRAY);
	private final Image imageTrack = createMockImage(32, 32, ColorSchema.MEDIUM_GRAY);
	
	private final JButton invisibleButton = new JButton() {
		public Dimension getPreferredSize() {
			return new Dimension(0, 0);
		}
	};

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		g.setColor(ColorSchema.DARK_GRAY);
		((Graphics2D) g).drawImage(imageThumb, r.x, r.y, r.width, r.height, null);
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		((Graphics2D) g).drawImage(imageTrack, r.x, r.y, r.width, r.height, null);
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		return invisibleButton;
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		return invisibleButton;
	}

	private static Image createMockImage(int w, int h, Color c) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(c);
		g2d.fillRect(0, 0, w, h);
		g2d.dispose();
		return image;
	}
}
