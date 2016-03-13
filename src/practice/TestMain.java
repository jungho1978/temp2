package practice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TestMain {
	private static final int THUMBNAIL_HEIGHT = 250;
	private static final int SCROLLBAR_HEIGHT = 27;
	private static final int TITLE_HEIGHT = 18;
	
	public static void main(String[] args) {
		final CrawlerViewer viewer = CrawlerViewer.create();
		for (int i = 0; i < 20; i++) {
			viewer.addViewScreenshotThumbnail(createDummyImage(), "DummyActivity");
		}
		viewer.fadeToast("Launching app ...");
	}
	
	private static BufferedImage createDummyImage() {
		BufferedImage image = new BufferedImage(400, 800, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 400, 800);
		g.setColor(Color.gray);
		g.fillRect(100, 100, 100, 100);
		return image;
	}
}
