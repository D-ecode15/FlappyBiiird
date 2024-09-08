import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private Clip jumpClip;
    private Clip collisionClip;
    private Clip pointClip;

    public SoundManager() {
        jumpClip = loadSound("jump.wav");
        collisionClip = loadSound("hit.wav");
        pointClip=loadSound("point.wav");
    }

    private Clip loadSound(String fileName) {
        try {
            URL soundFile = getClass().getResource(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playJumpSound() {
        if (jumpClip != null) {
            jumpClip.setFramePosition(0); // Rewind the clip
            jumpClip.start();
        }
    }

    public void playCollisionSound() {
        if (collisionClip != null) {
            collisionClip.setFramePosition(0); // Rewind the clip
            collisionClip.start();
        }
    }
    public void playPointSound() {
        if (pointClip != null) {
            pointClip.setFramePosition(0); // Rewind the clip
            pointClip.start();
        }
    }
}
