package pl.agh.edu.kis.filipras.controller;

import com.badlogic.gdx.Gdx;
import pl.agh.edu.kis.filipras.entities.Player;


/**
 * Created by filipras on 14.01.2017.
 */
public class VehicleMovementHandler implements MovementHandler {

    private static final int SPEED = 100;
    private Player player;

    public VehicleMovementHandler(Player player){
        this.player = player;
    }

    @Override
    public void move(){

        Direction direction = player.getDirection();

        if (player.isMooving()) {
            switch (direction) {
                case UP:
                    player.updatePosition(player.getPositionX(), player.getPositionY() + SPEED * Gdx.graphics.getDeltaTime());
                    break;
                case DOWN:
                    player.updatePosition(player.getPositionX(), player.getPositionY() - SPEED * Gdx.graphics.getDeltaTime());
                    break;
                case LEFT:
                    player.updatePosition(player.getPositionX() - SPEED * Gdx.graphics.getDeltaTime(), player.getPositionY());
                    break;
                case RIGHT:
                    player.updatePosition(player.getPositionX() + SPEED * Gdx.graphics.getDeltaTime(), player.getPositionY());
                    break;
            }
        }

    }

}
