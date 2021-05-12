import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.awt.Font.PLAIN;

/**
 * Klasa odpowiedzialna za scenerie gry
 */
public class GameScene {
    private Image day, night, ground;
    private int groundOffset = 0;
    private int speed;
    private int[] maxScore;
    private Font font, scoreFont;
    private boolean updatedHighScore = false;
    private Difficulty difficulty;
    private File highScoreFile;
    private GameWorld gameWorld;

    /**
     * Konstruktor przygotowuajcy scenerie do rysowania i aktualizowania
     *
     * @param speed     predkosc swiata gry
     * @param gameWorld swiat gry
     */
    public GameScene(int speed, GameWorld gameWorld) {
        day = new ImageIcon("sprites/day.png").getImage();
        night = new ImageIcon("sprites/night.png").getImage();
        ground = new ImageIcon("sprites/ground.png").getImage();
        maxScore = new int[Difficulty.values().length];
        this.speed = speed;
        this.gameWorld = gameWorld;
        difficulty = gameWorld.getDifficulty();

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE.ttf"));
            font = font.deriveFont(PLAIN, 50);
            scoreFont = font.deriveFont(PLAIN, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            highScoreFile = new File("highscore.txt");
            if (highScoreFile.createNewFile()) {    //no such file, need to create
                FileWriter myWriter = new FileWriter(highScoreFile);
                for (int i = 0; i < Difficulty.values().length; i++) {
                    myWriter.write(0 + "\n");
                }
                myWriter.close();
            } else {
                Scanner scanner = new Scanner(highScoreFile);
                for (int i = 0; i < maxScore.length; i++) {
                    if (scanner.hasNextInt())
                        maxScore[i] = scanner.nextInt();
                    else
                        maxScore[i] = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda odpowiedzialna za aktualizowanie bierzacego najwyzszego wyniku jaki zdobyl gracz
     */
    public void updateHighScore() {
        if (!updatedHighScore) {
            updatedHighScore = true;
            if (gameWorld.score > maxScore[difficulty.ordinal()]) {
                maxScore[difficulty.ordinal()] = gameWorld.score;
                try {
                    FileWriter myWriter = new FileWriter(highScoreFile);
                    for (int i = 0; i < Difficulty.values().length; i++) {
                        myWriter.write(Integer.toString(maxScore[i]) + "\n");
                    }
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Metoda rysujaca w odpowiednich momentach odpowiednie komunikaty graczowi
     *
     * @param g2d element grafiki
     */
    public void drawText(Graphics2D g2d) {
        GameWorld.State state = gameWorld.getState();
        Difficulty difficulty = gameWorld.getDifficulty();
        //Text
        g2d.setColor(Color.white);
        if (state == GameWorld.State.START) {
            g2d.setFont(font);
            g2d.drawString("PRESS  SPACE  TO  START", 150, 100);
            g2d.drawString("PRESS  P  TO  PAUSE", 150, 130);
            g2d.drawString("PRESS  P  TO  PAUSE", 150, 130);
            g2d.setFont(scoreFont);
            g2d.setColor(difficulty.getColor());
            g2d.drawString(difficulty.toString(), 300, 200);
        } else if (state == GameWorld.State.RUNNING) {
            g2d.setFont(scoreFont);
            g2d.drawString(Integer.toString(gameWorld.score), 380, 100);
        } else if (state == GameWorld.State.DEATH) {
            g2d.setFont(scoreFont);
            g2d.drawString("Your score  " + gameWorld.score, 120, 280);
            g2d.drawString("High score  " + maxScore[difficulty.ordinal()], 120, 360);
        } else if (state == GameWorld.State.PAUSE) {
            g2d.setFont(font);
            g2d.drawString("GAME  PAUSED", 150, 100);
            g2d.drawString("press  P  again  to  resume", 120, 130);
        }
    }

    /**
     * aktualizowanie stanu scenerii
     */
    public void update() {
        groundOffset += speed;
        if (groundOffset < -168) {
            groundOffset = 0;
        }
    }

    /**
     * Rysowanie scenerii
     *
     * @param g2d element grafiki
     */
    public void draw(Graphics2D g2d) {
        //background
        if (gameWorld.score < 5)
            g2d.drawImage(day, 0, 0, null);
        else
            g2d.drawImage(night, 0, 0, null);
        //ground
        for (int i = 0; i < 6; i++) {
            g2d.drawImage(ground, groundOffset + i * 168, 600 - 56, null);
        }
    }
}
