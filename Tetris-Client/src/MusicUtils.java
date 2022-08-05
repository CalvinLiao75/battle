import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class MusicUtils {

    private Clip clip;

    public void playMusic(String fileName) {
        try {
            URL soundURL = getClass().getResource(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String fileName) {
        try {
            URL soundURL = getClass().getResource(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
