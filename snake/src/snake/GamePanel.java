//necessary imports
package snake;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.*;


public class GamePanel extends JPanel implements ActionListener {
	
	//pre-setup
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; //determines the size of gridboxes; size of items
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; //how fast the game moves; higher number, slower the game
	
	final int x[] = new int[GAME_UNITS]; //array to hold x values of snake body
	final int y[] = new int[GAME_UNITS]; //array to hold y values of snake body
	
	int bodyParts = 6; //initial snake body length
	int applesEaten; //apple score; will initially be 0
	int appleX; //x coordinate of apple; random spawning
	int appleY; //y coordinate of apple; random spawning
	
	char direction = 'R'; //beginning direction; R (Right) L (Left) U (Up) D (Down)
	boolean running = false;
	Timer timer;
	Random random;
	
	//constructor
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	//start game method
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	//
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	//drawing gridlines and apples and snake body
	public void draw(Graphics g) {
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { //lines on x and y axis to make grid
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //vertical lines
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //horizontal lines
			}
		
			g.setColor(Color.red); //apple color
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //draw apple
		
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green); //snake head color
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //draw snake head
				}
				else {
					g.setColor(new Color(100, 200, 45)); //snake body color
					//rainbow snake code
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //draw snake body
				}
			}
			//score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	//starts the game with one apple
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}
	
	//move method
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
	
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
	}
	
	//check score method
	public void checkApple() {
		if ( (x[0] == appleX) && (y[0] == appleY)) {
			 bodyParts++;
			 applesEaten++;
			 newApple();
		}
	}
	
	//check collision method
	public void checkCollisions() {
		//checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ( (x[0] == x[i]) && (y[0] == y[i]) ) {
				running = false;
			}
		}
		//checks if head collides with left border
		if (x[0] < 0) {
			running = false;
		}
		//checks if head collides with right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//checks if head collides with top border
		if (y[0] < 0) {
			running = false;
		}
		//checks if head collides with bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	
	//game over pop up method
	public void gameOver(Graphics g) {
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
	}
	
	//
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	//
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
