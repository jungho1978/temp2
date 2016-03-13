package practice;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class ToastGlassPane extends JComponent {
	private JFrame frame;
	private String text;
	private Animator animator = new Animator();
	private final Font font = new Font("Arial", Font.PLAIN, 26);
	
	public ToastGlassPane(JFrame frame) {
		this.frame = frame;
	}
	
	public void setToast(String text, boolean fadeOut) {
		this.text = text;
		this.animator.start(fadeOut);
	}
	
	@Override
	public void paint(Graphics g) {
		if (animator.isVisible() == false) {
			return;
		}
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		FontMetrics metrics = g2d.getFontMetrics(font);
		Rectangle2D bounds = metrics.getStringBounds(text, g2d);
		
		final int padding = 40;
		
		final int rectH = font.getSize() + padding;
		final int rectW = (int) (bounds.getWidth()) + padding;
		
		final int frameW = frame.getWidth();
		final int frameH = frame.getHeight();
		
		final int rectX = (frameW - rectW) / 2;
		final int rectY = (frameH - rectH) / 2 - 100;
		
		final int textX = rectX + (padding / 2);
		final int textY = rectY + (padding / 2) + font.getSize() - 5;
		
		final int alpha = animator.getAlpha();
		
		g2d.setColor(withAlpha(ColorSchema.MEDIUM_GRAY, alpha));
		g2d.fillRoundRect(rectX, rectY, rectW, rectH, 20, 20);
		
		g2d.setFont(font);
		g2d.setColor(withAlpha(ColorSchema.LIGHT_GRAY, alpha));
		g2d.drawString(text, textX, textY);
	}
	
	private Color withAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	class Animator implements ActionListener {
		final int frequency = 50;
		final int delay = 4000;
		final int fading = 1000;
		final Timer timer = new Timer(frequency, this);
		
		boolean isVisible = false;
		long begin = now();
		long end = now();
		int alpha = 255;
		boolean fadeOut = false;
		
		public Animator() {
			timer.setInitialDelay(delay);
		}
		
		public void start(boolean fadeOut) {
			this.fadeOut = fadeOut;
			this.alpha = 255;
			this.isVisible = true;
			this.begin = now() + delay;
			this.end = now() + delay + fading;
			
			frame.repaint();
			if (fadeOut) {
				timer.start();
			}
		}
		
		public boolean isVisible() {
			return this.isVisible;
		}
		
		public int getAlpha() {
			return this.alpha;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			float total = end - begin;
			float current = now()- begin;
			float progress = Math.min(1f, (current / total));
			
			this.alpha = (int) (bounds(1f - progress) * 255);
			
			if (now() > end) {
				this.isVisible = false;
				timer.stop();
			}
			
			frame.repaint();
		}
		
		private float bounds(float value) {
			if (value < 0f) {
				return 0f;
			}
			
			if (value > 1f) {
				return 1f;
			}
			
			return value;
		}
		
		private long now() {
			return System.currentTimeMillis();
		}
	}
}
