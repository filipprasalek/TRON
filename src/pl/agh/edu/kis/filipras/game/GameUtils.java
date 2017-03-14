package pl.agh.edu.kis.filipras.game;

import pl.agh.edu.kis.filipras.entities.Player;

/**
 * Created by filipras on 18.01.2017.
 * Klasa odpowiada za konfiguracje danej gry
 */
public interface GameUtils {

    /**
     * Metoda pozwala na dodanie nowego gracza do lokalnej gry
     * @param player gracz
     */
    void addNewPlayer(Player player);

    /**
     * Metoda pozwala na usuniecie gracza z lokalnej gry, przy podaniu jego nazwy uzytownika
     * @param username nazwa uzytkownika
     */
    void removePlayer(String username);

    /**
     * Metoda pozwala na zmiane obecnego stanu gry
     * @param gameState docelowy stan gry
     */
    void changeGameState(GameState gameState);

    /**
     * Metoda zwraca obecny stan gry
     * @return obecny stan gry
     */
    GameState getGameState();

}
