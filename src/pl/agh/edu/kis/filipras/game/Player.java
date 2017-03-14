package pl.agh.edu.kis.filipras.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pl.agh.edu.kis.filipras.controller.*;
import pl.agh.edu.kis.filipras.packets.Packet03Move;

import java.net.InetAddress;


/**
 * Created by filipras on 08.01.2017.
 */

//// TODO: 15.01.2017 CZY BEDZIE ROZSZERZALO ZWYKLEGO PLAYERA
public class Player implements User {

    private InetAddress ipAdress;
    private int port;
    private String username;
    private boolean vehicleAssigned;

    private Vehicle vehicle;
    private MovementHandler movementHandler;
    private InputHandler inputHandler;

    private boolean localPlayer;

    private float positionX;
    private float positionY;
    private Direction direction;

    public Player(InetAddress ipAdress, int port,String username,float positionX, float positionY){

        this.ipAdress = ipAdress;
        this.port = port;
        this.username = username;
        this.positionX = positionX;
        this.positionY = positionY;
        direction = Direction.UP;

    }

    public Player(String username, float positionX, float positionY) {
        this.ipAdress = null;
        this.port = -1;
        this.username = username;
        this.positionX = positionX;
        this.positionY = positionY;
        localPlayer = true;
        direction = Direction.UP;
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

    @Override
    public void assignVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
        vehicleAssigned = true;
        movementHandler = new VehicleMovementHandler(this);
        inputHandler = new KeyboardInputHandler(this);
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
        if (vehicle != null) vehicle.getSprite().setPosition(positionX,positionY);
    }

    public float getPositionX(){
        return positionX;
    }

    public float getPositionY(){
        return positionY;
    }

    public boolean checkIfVehicleAssigned(){
        return vehicleAssigned;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public void play(){

        movementHandler.move();

        if (localPlayer) {
            inputHandler.turn();
        }


        /*for (Trace trace : vehicle.getTraces()) {
            trace.drawTrace();
            if(IntersectionDetector.checkTraceAndVehicle(vehicle.getSprite(),trace)){
                ShapeRenderer sp = new ShapeRenderer();
                sp.begin(ShapeRenderer.ShapeType.Line);
                sp.setColor(1,1,1,1);
                sp.line(600,600,600,800);
                sp.end();
                sp.dispose();
            }
        }*/

    }


    //TODO: przemysle to
    public void clean(){
        vehicle.clean();
    }

}
