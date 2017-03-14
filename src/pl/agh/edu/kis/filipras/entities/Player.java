package pl.agh.edu.kis.filipras.entities;

import pl.agh.edu.kis.filipras.controller.*;
import pl.agh.edu.kis.filipras.game.Tron;

import java.net.InetAddress;


/**
 * Created by filipras on 08.01.2017.
 */

public class Player implements User {

    private Tron game;

    private InetAddress ipAdress;
    private int port;
    private String username;

    private Vehicle vehicle;
    private MovementHandler movementHandler;
    private InputHandler inputHandler;

    private float positionX;
    private float positionY;
    private Direction direction;
    private boolean isMooving;

    public Player(InetAddress ipAdress, int port,String username,float positionX, float positionY){

        this.ipAdress = ipAdress;
        this.port = port;
        this.username = username;
        this.positionX = positionX;
        this.positionY = positionY;
        direction = Direction.UP;


    }

    public Player(String username, float positionX, float positionY, Tron game) {
        this.ipAdress = null;
        this.port = -1;
        this.username = username;
        this.positionX = positionX;
        this.positionY = positionY;
        direction = Direction.UP;
        movementHandler = new VehicleMovementHandler(this);
        inputHandler = new KeyboardInputHandler(this);
        this.game =game;
    }

    public Tron getGame(){
        return game;
    }

    public InetAddress getIP(){
        return ipAdress;
    }

    public int getPort(){
        return port;
    }

    public void setIP(InetAddress ipAdress){
        this.ipAdress = ipAdress;
    }

    public void setPort(int port){
        this.port = port;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
        if (vehicle!=null) vehicle.makeATurn(direction);
    }

    public void updatePosition(float x, float y){
        positionX = x;
        positionY = y;
        if (vehicle != null) vehicle.setPosition(positionX,positionY);
    }

    public float getPositionX(){
        return positionX;
    }

    public float getPositionY(){
        return positionY;
    }

    public float[] getVehicleCoords(){
        if (vehicle != null) return vehicle.getRoundigRectangle();
        return new float[4];
    }

    @Override
    public void assignVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public void play(){

        movementHandler.move();
        inputHandler.turn();

    }

    public void changeMovementState(boolean isMooving){
        this.isMooving = isMooving;
    }

    public boolean isMooving(){
        return isMooving;
    }


}
