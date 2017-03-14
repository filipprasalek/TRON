package pl.agh.edu.kis.filipras.entities;

import pl.agh.edu.kis.filipras.entities.Vehicle;

import java.net.InetAddress;

/**
 * Created by filipras on 08.01.2017.
 * Interfejs uzytkownika gry
 */
public interface User {

    /**
     * Metoda obslugujaca rozgrywke (sterowanie itp.) danego uzytkownika
     */
    void play();

    /**
     * Metoda przypisujaca pojazd danemu uzytkownikowi
     * @param vehicle pojazd do przypiania
     */
    void assignVehicle(Vehicle vehicle);

    /**
     * Metoda zwraca nazwe uzytkownika
     * @return
     */
    String getUsername();

    /**
     * Metoda zwraca IP uzytkownika
     * @return IP
     */
    InetAddress getIP();

    /**
     * Metoda zwraca port na ktorym dany uzytkownik nasluchuje
     * @return Port
     */
    int getPort();
}
