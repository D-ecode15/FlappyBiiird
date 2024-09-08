import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bird {
    public static final int BIRD_WIDTH = 35; // Adjust width and height according to your images
    public static final int BIRD_HEIGHT = 25;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -10;
    private static final int FLAP_TIMER_MAX = 8; // Adjust for flap speed

    private int x, y;
    private int velocityY;
    private BufferedImage[] birdImages; // Array to hold bird images
    private int flapTimer;
    private int flapState; // 0 = downflap, 1 = midflap, 2 = upflap

    public Bird(int startX, int startY) {
        x = startX;
        y = startY;
        velocityY = 0;
        flapTimer = 0;
        flapState = 1; // Start with midflap
        loadImages();
    }

    private void loadImages() {
        try {
            birdImages = new BufferedImage[3];
            birdImages[0] = ImageIO.read(getClass().getResource("redbird-downflap.png")); // Downflap image
            birdImages[1] = ImageIO.read(getClass().getResource("red_bird.png"));  // Midflap image
            birdImages[2] = ImageIO.read(getClass().getResource("redbird-upflap.png"));   // Upflap image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;

        // Prevent the bird from moving off-screen
        if (y < 0) y = 0;

        // Update flap animation
        flapTimer++;
        if (flapTimer >= FLAP_TIMER_MAX) {
            flapTimer = 0;
            flapState = (flapState + 1) % 3; // Cycle through flap states
        }
    }

    public void jump() {
        velocityY = JUMP_STRENGTH;
        flapState = 2; // Set to upflap when jumping
    }

    public void draw(Graphics g) {
        g.drawImage(birdImages[flapState], x, y, BIRD_WIDTH, BIRD_HEIGHT, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, BIRD_WIDTH, BIRD_HEIGHT);
    }

    // Getter methods for position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Method to check if the bird is touching the ground
    public boolean isTouchingGround(int floorY) {
        return y + BIRD_HEIGHT >= floorY;
    }
}
