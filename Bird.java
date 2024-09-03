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
    private int x, y;
    private int velocityY;
    private BufferedImage birdImage;

    public Bird(int startX, int startY) {
        x = startX;
        y = startY;
        velocityY = 0;
        loadImages();
    }

    private void loadImages() {
        try {
            birdImage = ImageIO.read(getClass().getResource("red_bird.png")); // Adjust path if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;

        // Prevent the bird from moving off-screen
        if (y < 0) y = 0;
    }

    public void jump() {
        velocityY = JUMP_STRENGTH;
    }

    public void draw(Graphics g) {
        g.drawImage(birdImage, x, y, BIRD_WIDTH, BIRD_HEIGHT, null);
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