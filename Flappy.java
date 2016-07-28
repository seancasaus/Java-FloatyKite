/*----------------------------------------------------------------------------------------------------------
*CSE_205: M-F 8:35-9:35
*Assignment: Assignment 6
*Author: Sean Casaus (1204885847)
*Description: JPanel extended class used for the painter method in the builder class. 
 ---------------------------------------------------------------------------------------------------------*/
import java.awt.Graphics;
import javax.swing.JPanel;

public class Flappy extends JPanel{
	private static final long serialVersionUID = 1L; //serial number.

	//Needs class to extend JPanel. 
	public void paintComponent(Graphics g) {
			super.paintComponent(g); //Calls the super class' paint component method
			builder.paint(g); //Calls Paint method from the bulder class. 
			repaint();
	}	
}

