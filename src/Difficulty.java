import java.awt.*;

/**
 * Klasa zawierajaca mozliwe tryby trudnosci gry
 */
public enum Difficulty {
    /**
     * Latwy poziom trudnosci
     */
    EASY(200, Color.green),
    /**
     * Sredni poziom trudnosci
     */
    MEDIUM(175, Color.magenta),
    /**
     * Trudny poziom trudnosci
     */
    HARD(175, Color.red, true);

    private Color color;
    private int gap;
    private boolean isMoving = false;

    /**
     * @return zwraca zmienna typu boolean, mowiaca czy rury maja sie ruszac w danym poziomie trudnosci (true - maja sie ruszac, false - nie)
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * @return Kolor jaki ma dany poziom trudnosc
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return wielkosc przerwy miedzy rurami
     */
    public int getGap() {
        return gap;
    }

    private static Difficulty[] vals = values();

    /**
     * Metoda sluzaca do zmiany poziomu trudnosci
     *
     * @return Nastepny poziom trudnosci jaki moze wybrac gracz
     */
    public Difficulty next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

    /**
     * Metoda sluzaca do zmiany poziomu trudnosci
     *
     * @return Poprzedni poziom trudnosci jaki moze wybrac gracz
     */
    public Difficulty previous() {
        int r = (this.ordinal() - 1) % vals.length;
        if (r < 0) {
            r += vals.length;
        }
        return vals[r];
    }

    /**
     * Konstruktor poziomu trudnosci, ustawiajacy wielkosc przery oraz kolor napisu,
     * domyslne zachowanie sie rur - nie poruszaja sie w gore i w dol
     *
     * @param gap   wielkosc przerwy miedzy rurami danego poziomu trudnosci
     * @param color kolor danego poziomu trudnosc
     */
    Difficulty(int gap, Color color) {
        this.gap = gap;
        this.color = color;
    }

    /**
     * Konstruktor poziomu trudnosci , ustawiajacy wielkosc przery oraz kolor napisu oraz zachowanie
     * sie rur
     *
     * @param gap      wielkosc przerwy miedzy rurami danego poziomu trudnosci
     * @param color    kolor danego poziomu trudnosc
     * @param isMoving zmienna mowiaca o tym czy rury maja sie poruszac w danym poziomie trudnosci
     */
    Difficulty(int gap, Color color, boolean isMoving) {
        this.gap = gap;
        this.color = color;
        this.isMoving = isMoving;
    }
}
