package pl.agh.edu.kis.filipras.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import pl.agh.edu.kis.filipras.game.Direction;
import pl.agh.edu.kis.filipras.game.Vehicle;

/**
 * Created by filipras on 07.01.2017.
 */
public class KeyboardMovementHandler implements MovementHandler {

    private final int SPEED = 3;
    private Vehicle vehicle;

    public KeyboardMovementHandler(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    @Override
    public void move(){

        Direction direction = vehicle.getDirection();
        Sprite sprite = vehicle.getSprite();

        switch (direction){
            case UP:
                sprite.setPosition(sprite.getX(),sprite.getY() + SPEED);
                break;
            case DOWN:
                sprite.setPosition(sprite.getX(),sprite.getY() - SPEED);
                break;
            case LEFT:
                sprite.setPosition(sprite.getX() - SPEED,sprite.getY());
                break;
            case RIGHT:
                sprite.setPosition(sprite.getX() + SPEED,sprite.getY());
                break;
        }
    }

    @Override
    public void turn(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) vehicle.makeATurn(Direction.RIGHT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) vehicle.makeATurn(Direction.LEFT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) vehicle.makeATurn(Direction.UP);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) vehicle.makeATurn(Direction.DOWN);
    }

}
