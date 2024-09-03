import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setTitle("Flappy Bird");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        // Set the frame icon (optional)
        try {
            Image icon = ImageIO.read(Main.class.getResourceAsStream ("flapbird.png"));
            frame.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
        panel.startGame();
    }
}
