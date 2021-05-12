import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * Klasa odpowiedzialna za oprawe audio gry
 */
public class AudioManager implements Runnable {
    private File pointSound, deathSound, flapSound;
    private GameWorld gameWorld;
    private int prevScore = 0;
    private boolean dead = false;
    private Semaphore semaphore;
    private boolean flap;

    /**
     * Konstruktor menedzera oprawy audio
     *
     * @param gameWorld swiat gry
     * @param semaphore semafor synchronizujacy watek audio z watkiem gry
     */
    public AudioManager(GameWorld gameWorld, Semaphore semaphore) {
        this.gameWorld = gameWorld;
        this.semaphore = semaphore;
    }

    private void play(File sound) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metoda odpowiedzialna za granie dzwiekow w odpowiednich momentach gry
     */
    @Override
    public void run() {
        pointSound = new File("sounds/point.wav");
        deathSound = new File("sounds/death.wav");
        flapSound = new File("sounds/flap.wav");

        while (true) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameWorld.State state = gameWorld.getState();
            int newScore = gameWorld.score;
            if (gameWorld.flap) {
                gameWorld.flap = false;
                flap = true;
            }
            semaphore.release();

            if (newScore == 0 && state != GameWorld.State.DEATH) {
                prevScore = 0;
                dead = false;
            }

            if (prevScore < newScore) {
                prevScore = newScore;
                play(pointSound);
            }

            if (!dead && state == GameWorld.State.DEATH) {
                dead = true;
                play(deathSound);
            }

            if (flap) {
                flap = false;
                play(flapSound);
            }
        }
    }
}
