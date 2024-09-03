import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 350;
    private final int HEIGHT = 600;
    private Bird bird;
    private PipeManager pipeManager;
    private Timer timer;
    private boolean gameOver;
    private int score;
    private BufferedImage backgroundImage;
    private BufferedImage floorImage;
    private BufferedImage startScreenImage; // New image for the start screen
    private BufferedImage gameOverImage;
    private int backgroundX1, backgroundX2;
    private int floorX1, floorX2;
    public final int BACKGROUND_SPEED = 1;
    public final int FLOOR_SPEED = 2; // Speed at which the floor scrolls

    private BufferedImage pipeImage;
    private SoundManager soundManager;

    private enum GameState {
        START_SCREEN, PLAYING, GAME_OVER
    }

    private GameState gameState;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setFocusable(true);

        bird = new Bird(WIDTH / 2 - Bird.BIRD_WIDTH / 2, HEIGHT / 2 - Bird.BIRD_HEIGHT / 2); // Center the bird
        pipeManager = new PipeManager(WIDTH, HEIGHT);
        timer = new Timer(20, this);
        gameOver = false;
        score = 0;

        soundManager = new SoundManager();

        backgroundX1 = 0;
        backgroundX2 = WIDTH;
        floorX1 = 0;
        floorX2 = WIDTH;

        loadImages();

        gameState = GameState.START_SCREEN;
    }

    private BufferedImage[] numberImages; // Array to hold number images

    private void loadImages() {
        try {
            pipeImage = ImageIO.read(getClass().getResource("pipe-green.png"));
            backgroundImage = ImageIO.read(getClass().getResource("background.png"));
            floorImage = ImageIO.read(getClass().getResource("floor.png"));
            startScreenImage = ImageIO.read(getClass().getResource("message.png")); // Load start screen overlay image
            gameOverImage = ImageIO.read(getClass().getResource("gameover.png"));

            // Load number images for score display
            numberImages = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                numberImages[i] = ImageIO.read(getClass().getResource(i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startGame() {
        timer.start();
    }
    private void drawScore(Graphics g) {
        String scoreStr = String.valueOf(score);
        int scoreLength = scoreStr.length();
        int totalWidth = scoreLength * numberImages[0].getWidth(); // Calculate width of score images
        int x = (WIDTH - totalWidth) / 2; // Center the score horizontally

        for (int i = 0; i < scoreLength; i++) {
            int digit = Character.getNumericValue(scoreStr.charAt(i)); // Get digit from score
            g.drawImage(numberImages[digit], x + i * numberImages[digit].getWidth(), 30, null); // Draw each digit image
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.PLAYING) {
            if (!gameOver) {
                bird.update();
                pipeManager.update();

                if (bird.isTouchingGround(HEIGHT - floorImage.getHeight()) || pipeManager.collides(bird.getBounds())) {
                    gameOver = true;
                    gameState = GameState.GAME_OVER;
                    soundManager.playCollisionSound();
                }

                if (pipeManager.checkPass(bird.getBounds())) {
                    score++;
                    soundManager.playPointSound();
                }


                // Update background and floor positions
                backgroundX1 -= BACKGROUND_SPEED;
                backgroundX2 -= BACKGROUND_SPEED;
                floorX1 -= FLOOR_SPEED;
                floorX2 -= FLOOR_SPEED;

                if (backgroundX1 + WIDTH <= 0) {
                    backgroundX1 = backgroundX2 + WIDTH;
                }
                if (backgroundX2 + WIDTH <= 0) {
                    backgroundX2 = backgroundX1 + WIDTH;
                }

                if (floorX1 + WIDTH <= 0) {
                    floorX1 = floorX2 + WIDTH;
                }
                if (floorX2 + WIDTH <= 0) {
                    floorX2 = floorX1 + WIDTH;
                }
            }
        } else if (gameState == GameState.START_SCREEN) {
            // Update background and floor positions only when game is in START_SCREEN state
            backgroundX1 -= BACKGROUND_SPEED;
            backgroundX2 -= BACKGROUND_SPEED;
            floorX1 -= FLOOR_SPEED;
            floorX2 -= FLOOR_SPEED;

            if (backgroundX1 + WIDTH <= 0) {
                backgroundX1 = backgroundX2 + WIDTH;
            }
            if (backgroundX2 + WIDTH <= 0) {
                backgroundX2 = backgroundX1 + WIDTH;
            }

            if (floorX1 + WIDTH <= 0) {
                floorX1 = floorX2 + WIDTH;
            }
            if (floorX2 + WIDTH <= 0) {
                floorX2 = floorX1 + WIDTH;
            }
        }
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background
        g.drawImage(backgroundImage, backgroundX1, 0, WIDTH, HEIGHT, this);
        g.drawImage(backgroundImage, backgroundX2, 0, WIDTH, HEIGHT, this);

        if (gameState == GameState.START_SCREEN) {
            // Draw the bird in the starting position
//            bird.draw(g);

            // Draw the start screen overlay image instead of text
            if (startScreenImage != null) {
                int overlayWidth = startScreenImage.getWidth() + 15;
                int overlayHeight = startScreenImage.getHeight() + 90;
                g.drawImage(startScreenImage, (WIDTH - overlayWidth) / 2, (HEIGHT - overlayHeight) / 2, this);
            }
        } else if (gameState == GameState.PLAYING || gameState == GameState.GAME_OVER) {
            // Draw the bird and pipes first
            bird.draw(g);
            pipeManager.draw(g, pipeImage);

            // Draw the score using images (if playing)
            if (gameState == GameState.PLAYING) {
                drawScore(g);
            }

            // If game over, draw the game over popup image
            if (gameState == GameState.GAME_OVER && gameOverImage != null) {
                int gameOverWidth = gameOverImage.getWidth() + 15;
                int gameOverHeight = gameOverImage.getHeight() + 90;
                g.drawImage(gameOverImage, (WIDTH - gameOverWidth) / 2, (HEIGHT - gameOverHeight) / 2, this);

                g.setFont(new Font("Century", Font.BOLD, 24));
                g.setColor(Color.BLACK);
                String restartText = "Press Space to Start again";
                int restartWidth = g.getFontMetrics().stringWidth(restartText);
                g.drawString(restartText, (WIDTH - restartWidth) / 2, HEIGHT / 2 + 20);
            }
        }

        // Draw the floor last so it covers the bottom of the pipes
        g.drawImage(floorImage, floorX1, HEIGHT - floorImage.getHeight(), WIDTH, floorImage.getHeight(), this);
        g.drawImage(floorImage, floorX2, HEIGHT - floorImage.getHeight(), WIDTH, floorImage.getHeight(), this);
    }






    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameState == GameState.START_SCREEN) {
                gameState = GameState.PLAYING;
                startGame();
            } else if (gameState == GameState.GAME_OVER) {
                resetGame();
                gameState = GameState.START_SCREEN;
            } else if (gameState == GameState.PLAYING) {
                bird.jump();
                soundManager.playJumpSound();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void resetGame() {
        bird = new Bird(WIDTH / 2 - Bird.BIRD_WIDTH / 2, HEIGHT / 2 - Bird.BIRD_HEIGHT / 2); // Center the bird again
        pipeManager = new PipeManager(WIDTH, HEIGHT);
        gameOver = false;
        score = 0;
        gameState = GameState.START_SCREEN;
    }
}
