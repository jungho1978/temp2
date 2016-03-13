package practice;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class CrawlerViewer {
	private static int THUMBNAIL_HEIGHT = 250;
	private static int SCROLLBAR_HEIGHT = 27;
	private static int TITLE_HEIGHT = 18;

	private static final String styleSheet = "graph { padding: 100px; fill-color: #2b2b2b; }"
			+ "node { size: 20px; fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }"
			+ "node.current { fill-color: #ffc438; }"
			+ "edge { fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }"
			+ "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }";

	private ToastGlassPane overlay;
	private JPanel imagePane;
	
	public static CrawlerViewer create() {
		CrawlerViewer viewer = new CrawlerViewer();
		viewer.open();
		return viewer;
	}

	private void open() {
		JFrame frame = new JFrame("App Crawler for Android");
		frame.setBackground(ColorSchema.DARK_GRAY);
		frame.setVisible(true);
		frame.setSize(800, 800);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(createButtonPanel(), BorderLayout.PAGE_START);
		frame.add((Component) createGraphView(), BorderLayout.CENTER);
		frame.add(createImageScrollPane(), BorderLayout.PAGE_END);

		overlay = new ToastGlassPane(frame);
		frame.setGlassPane(overlay);
		overlay.setVisible(true);
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(ColorSchema.DARK_GRAY);
		panel.setLayout(new FlowLayout());
		panel.add(createRestButton());
		panel.add(createZoomOutButton());
		panel.add(createZoomInButton());
		return panel;
	}

	private View createGraphView() {
		Graph graph = new MultiGraph("Traversal");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.stylesheet", styleSheet);

		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		View view = viewer.addDefaultView(false);

		return view;
	}

	private JScrollPane createImageScrollPane() {
		JPanel panel = new JPanel();
		panel.setBackground(ColorSchema.DARK_GRAY);
		panel.setLayout(new FlowLayout());

		JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(100, THUMBNAIL_HEIGHT));

		JScrollBar scrollBar = scrollPane.getHorizontalScrollBar();
		scrollBar.setUI(new CustomScrollBarUI());
		
		// TODO: re-factoring
		this.imagePane = panel;

		return scrollPane;
	}

	private JButton createRestButton() {
		JButton button = createFlatButton("Reset");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Reset button clicked");
			}
		});
		return button;
	}

	private JButton createZoomOutButton() {
		JButton button = createFlatButton("Zoom out");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Zoom out clicked");
			}
		});
		return button;
	}

	private JButton createZoomInButton() {
		JButton button = createFlatButton("Zoom in");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Zoom in clicked");
			}
		});
		return button;
	}

	private final JButton createFlatButton(String text) {
		JButton button = new JButton(text);
		button.setForeground(ColorSchema.LIGHT_GRAY);
		button.setBackground(ColorSchema.MEDIUM_GRAY);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setPreferredSize(new Dimension(110, 25));
		button.setFont(new Font("Arial", Font.PLAIN, 12));
		return button;
	}

	public void showToast(String text) {
		overlay.setToast(text, false);
	}

	public void fadeToast(String text) {
		overlay.setToast(text, true);
	}
	
	public void addViewScreenshotThumbnail(BufferedImage screenshot, String title) {
		double scale = (double) (THUMBNAIL_HEIGHT - SCROLLBAR_HEIGHT - TITLE_HEIGHT) / screenshot.getHeight();
		int w = (int)(screenshot.getWidth() * scale);
		int h = (int)(screenshot.getHeight() * scale);
		BufferedImage thumbnail = resizeImage(screenshot, w, h);
		
		JLabel label = new JLabel(new ImageIcon(thumbnail));
		label.setText(title);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setForeground(ColorSchema.LIGHT_GRAY);
		label.setFont(new Font("Arial", Font.PLAIN, 10));
		
		imagePane.add(label);
		imagePane.updateUI();
	}
	
	private BufferedImage resizeImage(BufferedImage original, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g2d = resized.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
		g2d.dispose();
		
		return resized;
	}
}
