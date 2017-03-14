package pl.agh.edu.kis.filipras.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pl.agh.edu.kis.filipras.controller.Direction;

/**
 * Created by filipras on 07.01.2017.
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

        MIDDLE = vehicle.getSprite().getHeight()/2;

        lastX = vehicle.getPositionX();
        lastY = vehicle.getPositionY();

        shapeRenderer = new ShapeRenderer();
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

    public void drawTrace(){

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        determineColor();

        if(isExpanding) {
            switch (vehicle.getDirection()){
                case UP:
                    lastX = vehicle.getPositionX();
                    shapeRenderer.line(lastX, lastY + MIDDLE, lastX, vehicle.getPositionY() + MIDDLE);
                    break;
                case DOWN:
                    lastX = vehicle.getPositionX();
                    shapeRenderer.line(lastX, lastY + MIDDLE, lastX, vehicle.getPositionY() + MIDDLE);
                    break;
                case LEFT:
                    lastY = vehicle.getPositionY();
                    shapeRenderer.line(lastX, lastY + MIDDLE, vehicle.getPositionX(), lastY + MIDDLE);
                    break;
                case RIGHT:
                    lastY = vehicle.getPositionY();
                    shapeRenderer.line(lastX, lastY + MIDDLE, vehicle.getPositionX(), lastY + MIDDLE);
                    break;
            }
        }

        else {
            shapeRenderer.line(traceCoordX1,traceCoordY1,traceCoordX2,traceCoordY2);
        }

        shapeRenderer.end();
    }

    public void finalTraceShape(){
        isExpanding = false;
        traceCoordX1 = lastX;
        traceCoordX2 = vehicle.getPositionX();
        traceCoordY1 = lastY+MIDDLE;
        traceCoordY2 = vehicle.getPositionY()+MIDDLE;
    }

    public float[] getTraceCoords(){
        float[] coordinates;

        if (isExpanding){
            if (vehicle.getDirection() == Direction.RIGHT){
                coordinates = new float[]{lastX,lastY+MIDDLE,vehicle.getPositionX()-1,lastY+MIDDLE};
            }

            else if (vehicle.getDirection() == Direction.LEFT){
                coordinates = new float[]{lastX,lastY+MIDDLE,vehicle.getPositionX()+1,lastY+MIDDLE};
            }

            else {
                coordinates = new float[]{lastX,lastY+MIDDLE,lastX,vehicle.getPositionY()+MIDDLE};
            }
        }

        else {
            coordinates = new float[]{traceCoordX1,traceCoordY1,traceCoordX2,traceCoordY2};
        }

        return coordinates;
    }

    public void clean(){
        shapeRenderer.dispose();
    }
}
