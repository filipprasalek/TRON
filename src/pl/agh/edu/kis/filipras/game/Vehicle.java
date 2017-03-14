package pl.agh.edu.kis.filipras.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import pl.agh.edu.kis.filipras.controller.Direction;

import java.util.ArrayList;

/**
 * Created by filipras on 07.01.2017.
 */
public interface Vehicle{

    Direction getDirection();
    Sprite getSprite();
    ArrayList<Trace> getTraces();

    float getPositionX();
    float getPositionY();

    //TODO: ogarne to
    void updatePosition(float x, float y);

    void makeATurn(Direction turnDirection);
    void vehicleAssigned();
    boolean checkIfAssigned();

    void clean();

}
