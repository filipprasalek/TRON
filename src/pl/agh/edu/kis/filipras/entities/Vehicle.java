package pl.agh.edu.kis.filipras.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.game.Tron;

import java.util.List;

/**
 * Created by filipras on 07.01.2017.
 */
public interface Vehicle{

    /**
     * Metoda zwraca kierunek pojazdu
     * @return Kierunek pojazdu
     */
    Direction getDirection();

    /**
     * Metoda umozliwiajac rysowanie pojazdu
     * @param sb Batch odpowiedzialny za rysowanie pojazdu
     */
    void draw(SpriteBatch sb);

    /**
     * Metoda zwraca szerokosc pojazdu
     * @return Szerokosc pojazdu w pixelach
     */
    float getWidth();

    /**
     * Metoda zwraca wysokosc pojazdu
     * @return Wysokosc pojazdu w pixelach
     */
    float getHeight();

    /**
     * Metoda zwraca pozycje lewego dolnego rogu pojazdu w poziomie
     * @return Pozycja lewego dolnego rogu pojazdu
     */
    float getPositionX();

    /**
     * Metoda zwraca pozycje lewego dolnego rogu pojazdu w pionie
     * @return Pozycja lewego dolnego rogu pojazdu
     */
    float getPositionY();

    /**
     * Metoda zwraca tablice wspolrzednych prostokata otaczajacego teksture pojazdu
     * @return Tablica wsporzednych prostokata otaczajacego teksture pojazdu
     */
    float[] getRoundigRectangle();

    /**
     * Metoda ustawia pozycje pojazdu
     * @param x Pozycja pojazdu w poziomie
     * @param y Pozycja pojazdu w pionie
     */
    void setPosition(float x, float y);

    /**
     * Metoda zmieniajace kierunek pojazdu
     * @param turnDirection Docelowy kierunek pojazdu
     */
    void makeATurn(Direction turnDirection);

    /**
     * Metoda pozwalajaca na przypisanie, lub ustawienie jego braku dla danego pojazdu
     * @param assigned True jesli ma byc przypisany, false jesli nie
     */
    void vehicleAssigned(boolean assigned);

    /**
     * Metoda zwraca czy pojazd jest przypisany
     * @return Przypisanie pojazdu
     */
    boolean checkIfAssigned();

}
