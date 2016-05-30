package bugs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer ;
import javax.swing.JPanel;

/**
 * View class for bug program 
 * 
 * @author Rajveer Parikh
 * @version March 2015
 */
public class View extends JPanel implements ActionListener{
	
	Color color; 
	double x; 
	double y; 
	double angle; 
	ArrayList<Bugs> bugs; 
	Timer timer = new Timer(1000, this);
	Interpreter interpret;

	public View(Interpreter interpret) {
		this.interpret = interpret;
		timer.start();
	}
	
	/**
	 * Paints a triangle to represent the bug 
	 * 
	 * @param g Where to paint this Bug.
	 */
	@Override
	public synchronized void paint(Graphics g) {
//	    if (color == null) return;
//	    g.setColor(color);
	    
//	    int x1 = (int) (scaleX(x) + computeDeltaX(12, (int)angle));
//	    int x2 = (int) (scaleX(x) + computeDeltaX(6, (int)angle - 135));
//	    int x3 = (int) (scaleX(x) + computeDeltaX(6, (int)angle + 135));
//	    
//	    int y1 = (int) (scaleY(y) + computeDeltaY(12, (int)angle));
//	    int y2 = (int) (scaleY(y) + computeDeltaY(6, (int)angle - 135));
//	    int y3 = (int) (scaleY(y) + computeDeltaY(6, (int)angle + 135));
//	    g.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
	}

	/**
	 * Computes how much to move to add to this Bug's x-coordinate,
	 * in order to displace the Bug by "distance" pixels in 
	 * direction "degrees".
	 * 
	 * @param distance The distance to move.
	 * @param degrees The direction in which to move.
	 * @return The amount to be added to the x-coordinate.
	 */
	private static double computeDeltaX(int distance, int degrees) {
	    double radians = Math.toRadians(degrees);
	    return distance * Math.cos(radians);
	}

	/**
	 * Computes how much to move to add to this Bug's y-coordinate,
	 * in order to displace the Bug by "distance" pixels in 
	 * direction "degrees.
	 * 
	 * @param distance The distance to move.
	 * @param degrees The direction in which to move.
	 * @return The amount to be added to the y-coordinate.
	 */
	private static double computeDeltaY(int distance, int degrees) {
	    double radians = Math.toRadians(degrees);
	    return distance * Math.sin(-radians);
	}
	
	/**
	 * Calls the repaint method based on the timer
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==timer) {
			repaint();
		}
		
	}

}
