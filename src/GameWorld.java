import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Klasa odpowiedzialna za zarzadzanie wszystkimi klasami, logike gry
 * oraz odpowiedzialna za wartstwe graficzna. Jest to klasa ktora wszystkie te elementy
 * laczy ze soba.
 */
public class GameWorld extends JPanel implements ActionListener {
    /**
     * Klasa ktora zawiera mozliwe stany w jakich gra moze sie znajdowac
     */
    public enum State {

        /**
         * Stan, kiedy gra jest na poczatku i oczekuje na dzialanie uzytkownika
         */
        START,
        /**
         * Stan pauzy gry
         */
        PAUSE,
        /**
         * Normalny stan gry, w ktorym gracz w nia gra
         */
        RUNNING,
        /**
         * Stan gry, gdy gracz przegra
         */
        DEATH
    }

    private int gamespeed = 120; //fps
    private JFrame frame;
    private Semaphore musicSem = new Semaphore(1);
    private final int speed = -3;
    private State state = State.START;
    private Difficulty difficulty = Difficulty.EASY;
    private Flappy flappy;
    private Deque<Pipe> pipes = new LinkedList<>();
    private Timer timer;
    private GameScene scene;
    /**
     * Zmienna mowiaca o tym czy inny watek powinien zagrac dzwiek machniecia skrzydlami
     */
    public boolean flap = false;
    /**
     * Zmienna oznaczajaca aktualny wynik gracza
     */
    public int score = 0;

    /**
     * Metoda zwracajaca wybrany poziom trudnosci
     *
     * @return wybrany poziom trudnosci
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Metoda zwracaja stan w jakim gra sie znajduje
     *
     * @return stan w jakim gra sie znajduje
     */
    public State getState() {
        return state;
    }

    /**
     * Konstruktor, w nim odbywa sie przygotowanie gry, ladowanie ustawien i tekstur
     *
     * @param frame glowny Frame w jakim znajduje sie Jpanel
     */
    public GameWorld(JFrame frame) {
        this.frame = frame;
        //ładowanie gry
        flappy = new Flappy(300, 250);

        timer = new Timer(1000 / gamespeed, this);
        timer.setRepeats(true);
        timer.start();

        AudioManager audioManager;
        audioManager = new AudioManager(this, musicSem);
        Thread audioThread = new Thread(audioManager);
        audioThread.start();

        scene = new GameScene(speed, this);

        addKeyListener(new Adapter());
        setFocusable(true);
    }

    private void restart() {
        state = State.START;
        scene = new GameScene(speed, this);
        flappy = new Flappy(300, 250);
        pipes.clear();
        score = 0;
    }

    private void managePipes() {
        if (pipes.size() == 0) {
            if (difficulty.isMoving())
                pipes.add(new MovingPipe(difficulty, speed));
            else
                pipes.add(new Pipe(difficulty, speed));
        } else {
            if (pipes.getLast().passedHalfScreen()) {
                if (difficulty.isMoving())
                    pipes.add(new MovingPipe(difficulty, speed));
                else
                    pipes.add(new Pipe(difficulty, speed));
            }
            if (pipes.getFirst().passedAway()) {
                pipes.removeFirst();
            }
        }
    }

    private void update() {
        if (state == State.RUNNING) {
            scene.update();
            managePipes();
            flappy.update();
            for (Pipe p : pipes) {
                p.update();
                if (p.score(flappy)) {
                    score++;
                }
                if (p.collision(flappy))
                    state = State.DEATH;
            }
            if (flappy.collideWithGround()) {
                state = State.DEATH;
            }
        } else if (state == State.DEATH && !flappy.collideWithGround()) {
            flappy.update();
        }

        if (state == State.DEATH) {
            scene.updateHighScore();
        }
    }

    /**
     * Metoda odpowiedzialna za rysowanie okna i tego co na nim sie dzieje.
     *
     * @param g element potrzebny do rysowania gry
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            musicSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        update(); //update game logic before drawing
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.BLACK);
        //draw scene
        scene.draw(g2d);
        //tubes
        for (Pipe p : pipes) {
            p.draw(g2d);
        }
        flappy.draw(g2d);
        scene.drawText(g2d);
        musicSem.release();
    }

    /**
     * Klasa pomocnicza, pozwalajaca na latwa obsluge wydarzen zwiazanych z interakcja gracza z klawiatura
     */
    class Adapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_UP:
                    if (state == State.START)
                        state = State.RUNNING;
                    if (state == State.RUNNING) {
                        flappy.goUp();
                        flap = true;
                    }
                    if (state == State.DEATH && flappy.collideWithGround()) {
                        restart();
                    }
                    break;
                case KeyEvent.VK_P:
                    if (state == State.RUNNING) {
                        state = State.PAUSE;
                    } else if (state == State.PAUSE) {
                        state = State.RUNNING;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (state == State.START || state == State.DEATH) {
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (state == State.START) {
                        difficulty = difficulty.next();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (state == State.START) {
                        difficulty = difficulty.previous();
                    }
                    break;
            }
        }

        /**
         * Metoda nieuzywana - override wymagany przez KeyAdapter
         *
         * @param e nieuzywany parametr
         */
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
        }
    }


    /**
     * Metoda potrzebna do odpowiedniej obslugi rysowania obrazu
     *
     * @param actionEvent nieuzywany parametr
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        repaint();
    }
}