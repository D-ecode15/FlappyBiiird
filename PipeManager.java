import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PipeManager {
    private List<Pipe> pipes;
    private int width, height;
    private int gapHeight = 150;
    private int pipeWidth = 65;
    private int pipeSpeed = 5;
    private int pipeSpawnInterval = 80;
    private int timeSinceLastSpawn;

    public PipeManager(int width, int height) {
        this.width = width;
        this.height = height;
        pipes = new ArrayList<>();
        timeSinceLastSpawn = 0;
    }

    public void update() {
        timeSinceLastSpawn++;
        if (timeSinceLastSpawn >= pipeSpawnInterval) {
            spawnPipe();
            timeSinceLastSpawn = 0;
        }

        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.update();
            if (pipe.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics g, BufferedImage pipeImage) {
        for (Pipe pipe : pipes) {
            if (pipeImage != null) {
                pipe.draw(g, pipeImage);
            } else {
                pipe.draw(g);
            }
        }
    }

    private void spawnPipe() {
        pipes.add(new Pipe(width, pipeWidth, height, gapHeight, pipeSpeed));
    }

    public boolean collides(Rectangle bird) {
        for (Pipe pipe : pipes) {
            if (pipe.collides(bird)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPass(Rectangle bird) {
        for (Pipe pipe : pipes) {
            if (!pipe.isPassed() && pipe.getX() + pipeWidth < bird.x) {
                pipe.setPassed(true);
                return true;
            }
        }
        return false;
    }
}