package pl.agh.edu.kis.filipras.game;

/**
 * Created by filipras on 08.01.2017.
 */
public interface User {

    void play();
    void assignVehicle(Vehicle vehicle);
    String getUsername();
    //TODO: przemysle
    void clean();
}
