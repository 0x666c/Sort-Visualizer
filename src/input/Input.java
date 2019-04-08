package input;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {
	
	public static final Input singleton = new Input();
	
	public static Point mousePosition = new Point(0,0);
	public static boolean isDragging = false;
	
	public void mouseDragged(MouseEvent e) {
		mousePosition = e.getPoint();
	}
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint();
	}
	public void mousePressed(MouseEvent e) {
		isDragging = true;
	}
	public void mouseReleased(MouseEvent e) {
		isDragging = false;
	}
	public void keyPressed(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	
	
	// Unused interface methods //
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
