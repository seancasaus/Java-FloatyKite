/*----------------------------------------------------------------------------------------------------------
*CSE_205: M-F 8:35-9:35
*Assignment: Assignment 6
*Author: Sean Casaus (1204885847)
*Description: The class that builds the game. Contains the actionlistener, and the main painter method 
 ---------------------------------------------------------------------------------------------------------*/
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class builder implements ActionListener, MouseListener, KeyListener{
	private static JFrame jframe = new JFrame();
	private static Flappy flappy = new Flappy(); 
	private static Rectangle bird = new Rectangle(CSE205Assignment06.WIDTH / 3, CSE205Assignment06.HEIGHT / 2 - 10, 40, 40); //Creates the player controllable "bird".
	private static BufferedImage kite = null; //The player controlled object is generically referred to as a bird, but is displayed as a kite png. 
	private static int ticks;
	private static int yMotion; //Motion of the bird. 
	private static boolean endGame = false; //Used to control the end of the game.
	private static boolean running = false; //Used to control the start of the game. 
	public static int score = 0; //The player's score
	public static int highscore = 0;
	public static ArrayList<Rectangle> columns = new ArrayList<Rectangle>();; //Arraylist of columns. 
	public static Random random = new Random(); //Random number generator
	
	//Creates a non-resizable Window.
	public void setWindow(int height, int width) {
		jframe.add(flappy); //JPanel extended methods in the Flappy class to be used in builder class. 
		jframe.setTitle("Floaty Kite"); //Title of game
		jframe.setSize(width, height); 
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setVisible(true);
		jframe.setResizable(false); //Non-resizable
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE ); //Allows the user to exit the program.
	}
	
	//Starts the <rectangles> arraylist off with the initial 4 sets of columns. 
	public static void initialColumns(){
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
	}
	
	//Creates and starts a timer. 
	public void setTimer(int time) {
		Timer timer = new Timer(time, this); 
		timer.start();
	}
	
	//increments the score if the bird passes through the pipes. 
	public static void setScore() {
		for (Rectangle tempColumn: columns) {
			if (tempColumn.y == 0 && bird.x + bird.width / 2 > tempColumn.x + tempColumn.width / 2 - 5
					&& bird.x + bird.width / 2 < tempColumn.x + tempColumn.width / 2 + 5) {
				score++;
			}
		}
	}
	
	//Saves the highest score of the user after the game has ended. 
	public static void saveHighScore() {
		if (endGame) {
			if (highscore < score ) {
				highscore = score;
			}
		}
	}
	
	//Adds columns that represent pipes to the arraylist.
	public static void addColumn(Boolean startingColumn) {
		int space = 350; //space between the comlumns. 
		int width = 100; //width of the columns
		int height = 50 + random.nextInt(300); //Randomly generated height with max of 300. 
		
		//If it is a starting column.
		if (startingColumn == true) {
			//Adds the starting bottom Rectangle column to the arraylist.
			columns.add(new Rectangle(CSE205Assignment06.WIDTH + width + columns.size() * 300, CSE205Assignment06.HEIGHT - height - 120, width, height));
			//Adds the starting top Rectangle column to the arraylist.
			columns.add(new Rectangle(CSE205Assignment06.WIDTH + width + (columns.size() - 1) * 300, 0, width, CSE205Assignment06.HEIGHT - height - space));
		}
		
		//If its not a starting column
		else {
			//Creates the next set of columns appropriately spaced from the previous set, and adds them to the arraylist. 
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, CSE205Assignment06.HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, CSE205Assignment06.HEIGHT - height - space));
		}
	}
	
	//paints a single column dark green. 
	public static void paintColumn(Graphics g, Rectangle tempColumn) {
		g.setColor(Color.green.darker()); //Sets color of grass to green.
		g.fillRect(tempColumn.x, tempColumn.y, tempColumn.width, tempColumn.height); //Fills Grass Layer
	}
	
	//Recursive method that's used to move columns across the screen. 
	public static void moveColumns(Rectangle tempColumn, int i) {
		if (tempColumn.x + tempColumn.width < 0) {
			columns.remove(tempColumn);
			//Calls addColumn only once as the top and bottom columns are removed. 
			if (tempColumn.y == 0) {
				addColumn(false);
			}
		i++;
		moveColumns(columns.get(i), i);
		}
	}
	
	//Loads kite.png for the bird. Uses event handling to communicate that kite is null. 
	public static void loadImage() {
		//Attempts to load kite.png
		try {
			kite = ImageIO.read(Class.class.getResourceAsStream("/kite.png"));
		} catch (IOException e) {
			kite = null; //If image is not loaded, then kite is null. 
		}
		flappy.repaint();
	}
	
	//Recursive method that sets the speed of the columns. 
	public static void setSpeed(int columnIndex, int speed) {
		if (columnIndex >= columns.size())
			return;
		
		columns.get(columnIndex).x -= speed;
		setSpeed(++columnIndex, speed);
	}
	
	//When the mouse or spacebar is clicked
	public static void birdManager() {
		//when the game is over, it is reset. 
		if (endGame) {
			bird = new Rectangle(CSE205Assignment06.WIDTH / 3, CSE205Assignment06.HEIGHT / 2 - 10, 40, 40); //Creates the player controllable "bird".
			columns.clear();
			yMotion = 0;
			score = 0;
			
			//Starts the <rectangles> arraylist off with the initial 4 sets of columns. 
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			endGame = false;
		}
		
		//When the game has not been started yet.
		if (!running) {
			running = true;
		}
		
		//While the game is being played, the bitrd will move vertically. 
		else if (!endGame) {
			if (yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
		}
	}

	//Called from the JPanel extended Flappy class.
	//Fills objects. 
	public static void paint(Graphics g) {
		builder.loadImage();
		g.setColor(Color.cyan); //Sets color of Backdrop to cyan.
		g.fillRect(0, 0, CSE205Assignment06.WIDTH, CSE205Assignment06.HEIGHT); //Fills the Backdrop
		
		//If kite.png did not load for whatever reason, then a red box replaces the image
		if (kite == null) {
			g.setColor(Color.red); //Sets color of the bird box to red.
			g.fillRect(bird.x, bird.y, bird.width, bird.height); //Fills a bird box graphic if image of the kite is not loaded.
		}
		//If kite.png loads as it is supposed to, then the kite image is displayed as the bird. 
		else {
			g.drawImage(kite, bird.x, bird.y, bird.width, bird.height, null); //Uses the loaded image to create the bird. 
		}

		g.setColor(Color.orange); //Sets color of sediment to Orange.
		g.fillRect(0, CSE205Assignment06.HEIGHT - 120, CSE205Assignment06.WIDTH, 120); //Fills Sediment layer.
		
		g.setColor(Color.green); //Sets color of grass to green.
		g.fillRect(0, CSE205Assignment06.HEIGHT - 120, CSE205Assignment06.WIDTH, 20); //Fills Grass layer.
		
		//paints every column in the arraylist.
		for (Rectangle tempColumn : columns) {
			paintColumn(g, tempColumn);
		}
		
		g.setColor(Color.white); //Sets color of font to white. 
		g.setFont(new Font("Arial", 1, 100)); //Fills a font for the display menu. 
		
		if (endGame) {
			g.drawString("Game Over.", 100 , CSE205Assignment06.HEIGHT / 2 - 50); //Creates Font displaying that the game has ended. 
		}
		
		if (!running) {
			g.drawString("Space Start.", 100 , CSE205Assignment06.HEIGHT / 2 - 50); //Prompts the user to start the game. 
		}
		
		//Displays the high score and the current score. 
		if (!endGame && running) {
			g.drawString(String.valueOf(score), CSE205Assignment06.WIDTH / 2 - 25, 100); //Fills the font for the score. 
			g.setColor(Color.white); //Sets color of score font to white.
			g.setFont(new Font("Arial", 1, 50)); //Creates font for the highscore.
			g.drawString(String.valueOf(highscore), 0, 50); //Fills the font for the highscore. 
		}
	}
	
	//Action listener method that handles events. 
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = 12; //speed of columns
		ticks++;
		
		if (running) {
			moveColumns(columns.get(0), 0); //Calls the recursive moveColumns method to move the columns across the screen. 
			setScore(); //Calls setScore method to increment the score. 
			saveHighScore(); //Saves the highscore. 
			setSpeed(0, speed); //Recursive method that sets the speed of the columns. 
			
			//If there is a collision with a column, then the game is over. 
			for (Rectangle tempColumn : columns) {
				if (tempColumn.intersects(bird)) {
					endGame = true;
					bird.x = tempColumn.x - bird.width; //Bird cannot go through columns. 
				}
			}
			
			//If the bird touches the top or the bottom of the screen, then the game is over. 
			if (bird.y > CSE205Assignment06.HEIGHT - 120 || bird.y < 0) {
				endGame = true;
			}
			
			//If the game ends, then the bird falls to the ground. 
			if (endGame) {
				bird.y = CSE205Assignment06.HEIGHT  - 120 - bird.height;
				bird.x -= speed; //Bird will move at speed of columns if the game ends. 
			}
			
			//Handles the bird's vertical motion. 
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			bird.y += yMotion;
		}
		flappy.repaint();
	}

	//uses mouse to manage bird movement
	@Override
	public void mouseClicked(MouseEvent e) {
		birdManager(); //When the mouse is clicked. 
	}
	
	//Uses spacebar to manage bird movement
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			birdManager(); //when the spacebar is clicked. 
		}
	}

	
	//Unused overwritten methods. 
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent arg0) {	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
	
