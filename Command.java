package bugs;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Command class for bug program
 * Creates an instance to hold the different commands for later execution 
 * 
 * @author Rajveer Parikh
 * @version March 2015
 */
public class Command {
	
	double x1; 
	double x2; 
	double y1; 
	double y2; 
	Color color; 

	Command(double x1, double y1, double x2, double y2, Color color) {
		this.color = color;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
