import javax.swing.*;
import java.awt.*;

/**
 * Klasa glowna programu, odpowiedzialna za przygotowanie graficzne okienka oraz zaladowanie logiki gry
 *
 * @author Mateusz Jarzebski
 */
public class Engine extends JFrame {
    /**
     * Konstruktor Klasy okna gry
     */
    public Engine() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800 + 16, 600 + 39);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);
        add(new GameWorld(this));
    }

    /**
     * Metoda glowna, ktora wywoluje nowy watek gry
     *
     * @param args nieuzywany argument
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Engine();
            }
        });
    }
}