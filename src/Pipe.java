import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Klasa odpowiednialna za logike i rysowanie rur - przeszkod
 */
public class Pipe {
    /**
     * minimalna wspolrzedna na jaka moze wzniesc sie rura
     */
    protected final int rangeMin = 82;
    /**
     * maksymalna wspolrzedna na jaka moze opuscic sie rura
     */
    protected final int rangeMax = 334;
    private static Image pipeImageDown = null, pipeImageUp = null;
    private static int pipeHeight = 0, pipeWidth = 0;
    private int gap;
    private int x = 800;
    private int speed;
    /**
     * wspolrzedna y rury - jest to koniec rury wystajacej z gory
     */
    protected float y;
    private boolean gaveScore = false;

    /**
     * Metoda sluzaca do sprawdzania kolizji flappiego z przeszkoda
     *
     * @param flappy Obiekt flappiego ktorym gracz gra
     * @return czy gracz zderzyl sie z rura
     */
    public boolean collision(Flappy flappy) {
        //to have some sense in detection of collision
        int flappySize = 58;
        if (flappy.getX() + flappySize < x || flappy.getX() > x + pipeWidth) //collision impossible
            return false;
        if (flappy.getY() + 8 >= y && flappy.getY() + flappySize <= y + gap) {
            return false;
        }
        return true;
    }

    /**
     * Metoda sluzaca za sprawdzanie czy gracz powinien dostac punkt
     *
     * @param flappy Obiekt flappiego ktorym gracz gra
     * @return czy gracz powinien dostac punkt za przelecenie miedzy rurami
     */
    public boolean score(Flappy flappy) {
        if (!gaveScore) {
            if (Math.abs((flappy.getX() + 30) - (x + gap / 2)) < 8) {  //8 - random threshold
                gaveScore = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdzajaca czy rura przeszla juz przez polowe ekranu
     *
     * @return czy rura przeszla juz przez polowe ekranu
     */
    public boolean passedHalfScreen() {
        return x < 450;
    }

    /**
     * Metoda sprawdzajaca czy rura wyszla za ekran
     *
     * @return czy rura wyszla za ekran
     */
    public boolean passedAway() {
        return x < -pipeWidth;
    }

    /**
     * Konstruktor tworzacy zestaw dwoch rur, jedna z gory, druga z dolu.
     * Przygotowuje tekstury, oraz ich polozenie
     *
     * @param difficulty aktualny poziom trudnosci gry
     * @param speed      predkosc z jaka sie porusza swiat gry
     */
    public Pipe(Difficulty difficulty, int speed) {
        if (pipeImageUp == null) {
            pipeImageUp = new ImageIcon("sprites/greenPipeUpBig.png").getImage();
            pipeImageDown = new ImageIcon("sprites/greenPipeDownBig.png").getImage();
        }
        if (pipeHeight == 0) {
            pipeHeight = pipeImageDown.getHeight(null);
            pipeWidth = pipeImageDown.getWidth(null);
        }
        Random random = new Random();
        y = random.nextInt(rangeMax - rangeMin) + rangeMin;
        gap = difficulty.getGap();
        this.speed = speed;
    }

    /**
     * Metoda aktualizujaca logike rur
     */
    public void update() {
        x += speed;
    }

    /**
     * Metoda rysujaca rury na ekranie
     *
     * @param g2d element grafiki
     */
    public void draw(Graphics2D g2d) {
        g2d.drawImage(pipeImageDown, x, (int) y - pipeHeight, null);
        g2d.drawImage(pipeImageUp, x, (int) y + gap, null);
    }
}