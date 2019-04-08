package window;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import input.Input;
import sorts.Sorts;
import sorts.Sorts.ISort;

public class Window {
	public static void main(String[] args) {Window.init(); Window.entry();}
	
	public static final int WIDTH = 1280, HEIGHT = 500;
	public static int STATUS_X = 10, STATUS_Y = 10;
	
	private static int padding = 1;
	private static int renderOffset = 0;
	private static double loopDelay = 500.0 * 1e6;
	private static double msDelay = 0;	
	private static boolean showMax = true, showMin = true;
	private static int[] tosort;
	
	private static boolean sorting = false;
	private static ISort currentSort = Sorts.bubbleSort();
	
	
	
	private static JFrame frame;
	private static SettingsPane settingsPane;
	private static Canvas canvas;
	
	private static BufferStrategy bs;
	private static Graphics g;
	
	public static void init() {
		frame = new JFrame();
		frame.setTitle("Sort Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.requestFocus();
		frame.requestFocusInWindow();
		frame.addKeyListener(Input.singleton);
		frame.addMouseListener(Input.singleton);
		frame.addMouseMotionListener(Input.singleton);
		JPanel cp = new JPanel();
		cp.setLayout(new BorderLayout());
		frame.setContentPane(cp);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(Window.WIDTH, Window.HEIGHT));
		canvas.addKeyListener(Input.singleton);
		canvas.addMouseListener(Input.singleton);
		canvas.addMouseMotionListener(Input.singleton);
		settingsPane = new SettingsPane();
		frame.add(settingsPane, BorderLayout.EAST);
		frame.add(canvas, BorderLayout.WEST);
		canvas.createBufferStrategy(1);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
		frame.pack();
	}
	
	
	
	private static void render() {
		if(tosort != null) {
			g.translate(renderOffset, 0);
			
			int max = 0;
			int min = tosort.length;
			int[] cmp = currentSort.getCurrentComparing();
			
			if(showMax)
			for (int i = 0; i < tosort.length; i++) {
				max = Math.max(max, tosort[i]);
			}
			
			if(showMin)
			for (int i = 0; i < tosort.length; i++) {
				min = Math.min(min, tosort[i]);
			}
			g.setColor(Color.WHITE);
			for (int i = 0; i < tosort.length; i++) {
				if (tosort[i] == max) {
					if(showMax) {
						g.setColor(Color.BLUE);
						g.fillRect(i * padding, HEIGHT - tosort[i], padding, tosort[i]);
						g.setColor(Color.WHITE);
						max = -1;
					}
				} else if (tosort[i] == min) {
					if(showMin) {
						g.setColor(Color.CYAN);
						g.fillRect(i * padding, HEIGHT - tosort[i], padding, tosort[i]);
						g.setColor(Color.WHITE);
						min = -1;
					}
				} else {
					g.fillRect(i * padding, HEIGHT - tosort[i], padding, tosort[i]);
					for (int j = 0; j < cmp.length; j++) {
						if(i == cmp[j]) {
							g.setColor(Color.RED);
							g.fillRect(i * padding, HEIGHT - tosort[i], padding, tosort[i]);
							g.setColor(Color.WHITE);
						}
						continue;
					}
				}
				g.setColor(Color.BLACK);
				g.drawRect(i * padding, HEIGHT - tosort[i], padding, tosort[i]);
				g.setColor(Color.WHITE);
			}
		}
		g.setFont(new Font("Consolas", Font.PLAIN, 13));
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.drawString("Elements: " + ((tosort == null ? "n/a" : tosort.length) + "  Array accesses: "
				+ currentSort.arrayAccesses() + "  Comparisons: " + currentSort.comparisons() + "  Delay: " + msDelay),
				STATUS_X, STATUS_Y);
	}
	
	private static void tick() {
		if(sorting) {
			if(currentSort.sortOneIteration(tosort)) {
				settingsPane.finishedCallback();
			}
			
		}
	}
	
	public static void setNewArray(int[] array) {
		tosort = array;
		
		padding = (WIDTH) / (tosort.length);
		
		renderOffset = (WIDTH - (tosort.length * padding)) / 2;
	}
	
	
	private static double delta_tick = 0;
	private static long now_tick;
	private static long last_tick = System.nanoTime();
	private static void entry() {
		final double times_fps = 1e9 / 60;
		
		double delta_render = 0;
		long now_render;
		long last_render = System.nanoTime();
		
		preinit();
		
		while(true) {
			now_render = System.nanoTime();
			delta_render += (now_render - last_render) / times_fps;
			last_render = now_render;
			
			now_tick = System.nanoTime();
			delta_tick += (now_tick - last_tick) / (loopDelay);
			last_tick = now_tick;
			
			if (delta_tick >= 1) {
				tick();
				delta_tick--;
			}
			if (delta_render >= 1) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				render();
				
				if(tosort == null) {
					g.setColor(Color.WHITE);
					g.setFont(new Font("Monospaced", Font.BOLD, 24));
					g.drawString("Press \"Generate Random\" to create a new array!", WIDTH/2 - 325, HEIGHT / 2);
				}
				
				g.dispose();
				bs.show();
				g = bs.getDrawGraphics();
				
				delta_render--;
			}
		}	
	}
	
	public static void startSorting(ISort sort) {
		currentSort = sort;
		currentSort.reset();
		
		sorting = true;
	}
	
	public static void setDelay(double msDelay) {
		loopDelay = msDelay * 1e6;
		Window.msDelay = msDelay;
		delta_tick = 0;
	}
	
	public static void setShowMax(boolean show) {
		showMax = show;
	}
	
	public static void setShowMin(boolean show) {
		showMin = show;
	}
	
	public static void pauseSorting() {
		sorting = false;
	}
	
	public static void stopSorting() {
		sorting = false;
		currentSort.reset();
	}
	
	public static boolean isSorting() {
		return sorting;
	}
	
	// Clear bg a couple of times, so there will be no blinking at low framerates
	private static void preinit() {
		for (int i = 0; i < 15; i++) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.dispose();
			bs.show();
			g = bs.getDrawGraphics();
		}
	}
	
	public static int[] genRandomArr(int len, int highBound) {
		Random r = new Random();
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = r.nextInt(highBound - 14) + 14;
		}
		return arr;
	}
}
