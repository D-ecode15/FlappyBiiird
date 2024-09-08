import java.awt.*;
import java.awt.image.BufferedImage;

public class Pipe {
    private int x, width, height;
    private int gapHeight;
    private int speed;
    private boolean passed;
    private Rectangle topPipe, bottomPipe;

    public Pipe(int x, int width, int height, int gapHeight, int speed) {
        this.x = x;
        this.width = width;
        this.height = height;
        this.gapHeight = gapHeight;
        this.speed = speed;
        this.passed = false;

        int gapY = (int) (Math.random() * (height - gapHeight - 200)) + 100; // Adjust gap position

        topPipe = new Rectangle(x, 0, width, gapY);
        bottomPipe = new Rectangle(x, gapY + gapHeight, width, height - (gapY + gapHeight));
    }


    public void update() {
        x -= speed;
        topPipe.x = x;
        bottomPipe.x = x;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);
        g.fillRect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
    }

    public void draw(Graphics g, BufferedImage pipeImage) {
        // Draw upper pipe inverted (flipped vertically)
        if (pipeImage != null) {
            // Flip the upper pipe image vertically
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(pipeImage, x, topPipe.height - height, width, height, null); // Normal lower pipe
            g2d.drawImage(pipeImage, x, topPipe.height, width, -height, null); // Inverted upper pipe
            g2d.dispose();
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);
            g.fillRect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
        }

        // Draw lower pipe normally
        if (pipeImage != null) {
            g.drawImage(pipeImage, x, bottomPipe.y, width, height, null);
        }
    }



    public boolean collides(Rectangle bird) {
        return bird.intersects(topPipe) || bird.intersects(bottomPipe);
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public Rectangle getTopPipe() {
        return topPipe;
    }

    public Rectangle getBottomPipe() {
        return bottomPipe;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}