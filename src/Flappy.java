import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiednialna za logike i rysowanie flappiego
 */
public class Flappy {
    private Image flappy, flappyUp, flappyDown1, flappyDown2;

    private int flappyX, flappyY;
    private float speed = 0;
    private final int maxSpeed = 10;
    private final float gravity = 0.6f;
    private final float kick = -10.0f;

    /**
     * Metoda sluzaca do pobierania wspolrzednej Y
     *
     * @return wspolrzedna Y w jakiej flappy sie znajduje
     */
    public int getY() {
        return flappyY;
    }

    /**
     * Metoda sluzaca do pobierania wspolrzednej X
     *
     * @return wspolrzedna X w jakiej flappy sie znajduje
     */
    public int getX() {
        return flappyX;
    }

    /**
     * Konstruktor klasy Flappy, odpowiedzialny za przygotowanie wszystkiego zwiazanego z glownym
     * bohaterem gry.
     *
     * @param x wspolrzedna x w jakiej flappy ma zaczac
     * @param y wspolrzedna y w jakiej flappy ma zaczac
     */
    public Flappy(int x, int y) {
        flappyX = x;
        flappyY = y;

        flappy = new ImageIcon("spriteshd/front2.png").getImage();
        flappyUp = new ImageIcon("spriteshd/up2.png").getImage();
        flappyDown1 = new ImageIcon("spriteshd/down1.png").getImage();
        flappyDown2 = new ImageIcon("spriteshd/down2.png").getImage();
    }

    /**
     * Metoda sluzaca do sprawdzania czy flappy koliduje z ziemia
     *
     * @return Czy flappy uderzyl w ziemie
     */
    public boolean collideWithGround() {
        if (flappyY > 600 - 56 - 60)    //screen height - ground height - flappy point
            return true;
        return false;
    }


    private void animate(Graphics2D g2d) {
        if (speed <= 0) {
            g2d.drawImage(flappyUp, flappyX, flappyY, null);
        } else if (speed < 4) {
            g2d.drawImage(flappy, flappyX, flappyY, null);
        } else if (speed >= 4 && speed < 8) {
            g2d.drawImage(flappyDown1, flappyX, flappyY, null);
        } else {
            g2d.drawImage(flappyDown2, flappyX, flappyY, null);
        }

    }

    /**
     * Metoda odpowiedzialna za aktualizowanie logiki flappiego
     */
    public void update() {
        speed += gravity;
        if (speed > maxSpeed)
            speed = maxSpeed;
        flappyY += speed;
    }


    /**
     * Metoda odpowiedzialna za rysowanie flappiego na ekranie
     *
     * @param g2d element grafiki
     */
    public void draw(Graphics2D g2d) {
        animate(g2d);
    }

    /**
     * Wywolanie tej metody sprawia ze flappy leci do gory
     */
    public void goUp() {
        speed = kick;
    }
}
