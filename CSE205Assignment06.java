/*----------------------------------------------------------------------------------------------------------
*CSE_205: M-F 8:35-9:35
*Assignment: Assignment 6
*Author: Sean Casaus (1204885847)
*Description: Driver class that sets the window, and adds the first four columns to the <rectangles> arraylist.
 ---------------------------------------------------------------------------------------------------------*/

public class CSE205Assignment06 {
	final static int HEIGHT = 800; //Sets Height of window
	final static int WIDTH = 800; //Sets Width of window
	final static int TIME = 20; 
	
	public static void main(String[]args) {
		builder newBuild = new builder();
		newBuild.setWindow(WIDTH, HEIGHT); //Sets the height and width of the window
		newBuild.setTimer(TIME); //Starts the timer.
		builder.initialColumns(); //Starts the <rectangles> arraylist off with the initial 4 sets of columns. 
		
	}
}