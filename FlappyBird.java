import javax.swing.*; // import swing package for GUI components
import java.awt.*;
import java.awt.event.*; // import event packages for handling user input
import java.util.ArrayList; // import ArrayList for managing multiple pipes
import java.util.Random; // import random for generating random pipe hights


//main class that extends JPanel for drawing game componets
public class FlappyBird extends JPanel implements MouseListener{
    // Declare the bird as a rectangle object
    private Rectangle bird;
    private int velocity = 0; // velocity of the bird, used to simulate gravity and flapping
    private final int GRAVITY = 1; // gravity effect, how fast the bird will fall
    private final int FLAP_STRENGTH = -15;
    private ArrayList<Rectangle> pipes; // list of pipes currently on the screen
    private  Random random; // random generator for pipe heights
    private int pipeSpawnTimer = 0; // timer for controlling when new pipes spawn
    private int score = 0; // players score

    //constructor for our class (like init in python)
    public FlappyBird() {
        bird = new Rectangle(50,250,30,30); // initialize the bird with position and size
        pipes = new ArrayList<>(); // initialize the ArrayList to store pipes
        random = new Random(); // initialize the random object fo pipe height generation
        JFrame frame = new JFrame("Flappy Bird"); // creates new window with the tile "Flappy Bird
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set defult close opperation to exit the application
        frame.setSize(800, 600); // set the size of the window to 800x600 pixels
        frame.add(this); // add this panel to the frame
        frame.setVisible(true); // makes this window visible

        addMouseListener(this); // add a mouse listener to the panel to detect clicks

        Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bird.y += velocity; // Update the bird's vertical position based on its velocity
                velocity += GRAVITY; // Apply gravity to the bird's velocity

                movePipes(); // move pipes to the left
                spawnPipes(); // spawn new pipes at regular intervals
                checkCollisions(); //check for collisions between the bird and the pipes


                repaint(); // redraw the panel to reflect changes
            }

        });
        timer.start(); // start the timer to countinuasly update game logic and repaint

    }// end of constructor

    private void movePipes() {
        //move pipes to the left and remove any that go offscreen
        for (int i =0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 5; // move the pipes to the left

            // remove pipes that are completely off-screen
            if (pipe.x + pipe.width < 0) {
                pipes.remove(i);
                i--; // adjust the index after removal
                score++; // increment the score when a pipe goes off-screen
            }
        }
    }
    private void spawnPipes() {
        // spawn pipes every 100 ticks of the timer
        pipeSpawnTimer++;
        if (pipeSpawnTimer >= 100) {
            int pipeHeight = random.nextInt(300) + 50; // randomize height for the top pipe
            int gap = 150; // gap between the top and bottom pipes
            pipes.add(new Rectangle(800, 0, 50, pipeHeight)); //TOP pipe
            pipes.add(new Rectangle(800, pipeHeight +gap, 50, 600 - pipeHeight - gap)); // Bottom pipe
            pipeSpawnTimer = 0; // rest the spawn timer
        }
    }
    private void checkCollisions() {
        // check if the bird collides with any pipes or the edges of the screen
        for (Rectangle pipe : pipes) {
            if (bird.intersects(pipe)) {
                gameOver(); // Emd the game if there's a collision
            }
        }

        //checks if the bird hits the top of bottom of the screen
        if (bird.y < 0 || bird .y + bird.height > 600) {
            gameOver(); // ends the game
        }
    }
    private void gameOver() {
        //stop the game and show a Game Over message
        JOptionPane.showMessageDialog(this, "GAME OVER! YOUR SCORE: " + score);
        System.exit(0); //Exit the Program
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // call the superclass method to handle standard painting task
        g.setColor(Color.RED); // set the color for painting the bird
        g.fillRect(bird.x, bird.y, bird.width, bird.height); //Draw the bird as a filled rectange

        // Draw the background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 800, 600);

        // Draw the bird
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        //Draw the pipes
        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Draw the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " +score, 10, 20);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        velocity = FLAP_STRENGTH; // set the bird's velocity to the flap strength when the mouse is clicked
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    // main method to start the game application

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { // invokelater is like an event queue for the opperating system
            public void run() { // run if the collection of the things we send to invokeLater
                new FlappyBird(); // creates an instance of Flappy Bird to start the game
            }
        });
    }
}// end of class
