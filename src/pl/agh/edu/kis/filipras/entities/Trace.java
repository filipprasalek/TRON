package pl.agh.edu.kis.filipras.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.entities.PlayerColor;
import pl.agh.edu.kis.filipras.entities.Vehicle;
import pl.agh.edu.kis.filipras.game.Tron;
import pl.agh.edu.kis.filipras.packets.Packet07Trace;

/**
 * Created by filipras on 07.01.2017.
 * Klasa reprezentujaca slad zostawiany przez pojazd
 */
public class Trace {

    private Vehicle vehicle;
    private float lastX;
    private float lastY;
    private boolean isExpanding;
    private PlayerColor playerColor;

    private float traceCoordX1;
    private float traceCoordX2;
    private float traceCoordY1;
    private float traceCoordY2;

    private final float MIDDLE;

    private ShapeRenderer shapeRenderer;

    public Trace(Vehicle vehicle,PlayerColor playerColor){

        this.vehicle = vehicle;
        this.playerColor = playerColor;

        MIDDLE = vehicle.getHeight()/2;

        lastX = vehicle.getPositionX();
        lastY = vehicle.getPositionY();

        shapeRenderer = Tron.game.getRenderer();
        isExpanding = true;
    }

    private void determineColor(){
        switch (playerColor){
            case BLUE:
                shapeRenderer.setColor(Color.SKY);
                break;
            case YELLOW:
                shapeRenderer.setColor(Color.YELLOW);
                break;
            case RED:
                shapeRenderer.setColor(Color.RED);
                break;
            case GREEN:
                shapeRenderer.setColor(Color.GREEN);
                break;
        }
    }


    /**
     * Metoda rysuje slad
     */
    public void drawTrace(){

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        determineColor();

        if(isExpanding) {
            switch (vehicle.getDirection()){

                case UP:
                case DOWN:
                    lastX = vehicle.getPositionX();
                    shapeRenderer.line(lastX, lastY + MIDDLE, lastX, vehicle.getPositionY()+ MIDDLE);
                    break;

                case LEFT:
                case RIGHT:
                    lastY = vehicle.getPositionY();
                    shapeRenderer.line(lastX, lastY + MIDDLE, vehicle.getPositionX(),lastY + MIDDLE);
                    break;

                default:
                    break;
            }
        }

        else {
            shapeRenderer.line(traceCoordX1,traceCoordY1,traceCoordX2,traceCoordY2);
        }

        shapeRenderer.end();
    }

    /**
     * Metoda konczy rysowanie slady i zapisuje go w ostatecznej postaci w miejscu w ktorym sa przechowywane
     */
    public void finalTraceShape(){
        isExpanding = false;
        traceCoordX1 = lastX;
        traceCoordX2 = vehicle.getPositionX();
        traceCoordY1 = lastY+MIDDLE;
        traceCoordY2 = vehicle.getPositionY()+MIDDLE;

        if (playerColor == PlayerColor.BLUE) {
            Packet07Trace packet = new Packet07Trace(Tron.game.getUtils().getLocalPlayer().getUsername(), traceCoordX1, traceCoordY1, traceCoordX2, traceCoordY2);
            packet.writeData(Tron.game.getClient());
        }
    }

}
