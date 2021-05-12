/**
 * Klasa rozszerzajaca mozliwosci rur o poruszanie sie
 */
public class MovingPipe extends Pipe {

    private float ySpeed = 1.5f;

    /**
     * Konstruktor klasy MovingPipe, ustawiajacy
     * losowo czy rura porusza sie w gore czy w dol
     *
     * @param difficulty wybrany poziom trudnosci
     * @param speed      predkosc swiata gry
     */
    public MovingPipe(Difficulty difficulty, int speed) {
        super(difficulty, speed);
        if (Math.random() < 0.5)
            ySpeed *= -1;
    }

    /**
     * metoda aktualizaca logike rur, pozwalajaca na ich poruszanie sie w gore i w dol
     */
    @Override
    public void update() {
        super.update();
        if (ySpeed > 0) {
            if (y < rangeMax)
                y += ySpeed;
            else
                ySpeed = -ySpeed;
        } else {
            if (y > rangeMin)
                y += ySpeed;
            else
                ySpeed = -ySpeed;
        }

    }
}
