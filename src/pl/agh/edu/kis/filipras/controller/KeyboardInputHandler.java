package pl.agh.edu.kis.filipras.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.game.Tron;
import pl.agh.edu.kis.filipras.packets.Packet03Move;

/**
 * Created by filipras on 07.01.2017.
 */
public class KeyboardInputHandler implements InputHandler {

    private Player player;

    public KeyboardInputHandler(Player player){
        this.player = player;
    }


    @Override
    public void turn(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && player.getDirection() != Direction.LEFT){
            player.setDirection(Direction.RIGHT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && player.getDirection() != Direction.RIGHT){
            player.setDirection(Direction.LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getDirection() != Direction.DOWN){
            player.setDirection(Direction.UP);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && player.getDirection() != Direction.UP){
            player.setDirection(Direction.DOWN);
        }

        Packet03Move packet = new Packet03Move(player.getUsername(),player.getPositionX(),player.getPositionY(),player.getDirection(),player.getVehicleCoords());
        packet.writeData(player.getGame().getClient());

    }

}
