package pl.agh.edu.kis.filipras.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import pl.agh.edu.kis.filipras.controller.Direction;

import java.util.ArrayList;

/**
 * Created by filipras on 07.01.2017.
 */
public class Motorcycle implements Vehicle{

    private Direction currentDirection;
    private Sprite sprite;
    private ArrayList<Trace> traces;
    private PlayerColor playerColor;

    private boolean isAssigned;

    public Motorcycle(PlayerColor playerColor){

        this.playerColor = playerColor;
        loadAppearance();

        currentDirection = Direction.UP;

        sprite.setOrigin(0,sprite.getHeight()/2);
        sprite.setRotation(sprite.getRotation() + 90);
        sprite.setCenter(100 + playerColor.getId()*100,240);

        traces = new ArrayList<Trace>();
        traces.add(new Trace(this,playerColor));
    }

    public float getPositionX(){
        return sprite.getX();
    }

    public float getPositionY(){
        return sprite.getY();
    }

    private void loadAppearance(){
        switch (playerColor){
            case BLUE:
                sprite = new Sprite(new Texture("motorcycle_blue.png"));
                break;
            case GREEN:
                sprite = new Sprite(new Texture("motorcycle_green.png"));
                break;
            case RED:
                sprite = new Sprite(new Texture("motorcycle_red.png"));
                break;
            case YELLOW:
                sprite = new Sprite(new Texture("motorcycle_yellow.png"));
                break;
        }
    }

    @Override
    public void updatePosition(float x, float y){
        sprite.setX(x);
        sprite.setY(y);
    }

    @Override
    public Direction getDirection() {
        return currentDirection;
    }


    @Override
    public Sprite getSprite() {
        return sprite;
    }

    private void changeState(Direction direction){
        currentDirection = direction;
        traces.get(traces.size()-1).finalTraceShape();
//        traces.add(new Trace(this,playerColor));
    }

    @Override
    public void makeATurn(Direction turnDirection){
        if (currentDirection == Direction.UP){
            switch (turnDirection){
                case LEFT:
                    changeState(Direction.LEFT);
                    sprite.setRotation(sprite.getRotation() + 90);
                    break;
                case RIGHT:
                    changeState(Direction.RIGHT);
                    sprite.setRotation(sprite.getRotation() - 90);
                    break;
                default:
                    break;
            }
        }

        else if (currentDirection == Direction.RIGHT){
            switch (turnDirection){
                case UP:
                    changeState(Direction.UP);
                    sprite.setRotation(sprite.getRotation() + 90);
                    break;
                case DOWN:
                    changeState(Direction.DOWN);
                    sprite.setRotation(sprite.getRotation() - 90);
                    break;
                default:
                    break;
            }
        }

        else if (currentDirection == Direction.LEFT){
            switch (turnDirection){
                case UP:
                    changeState(Direction.UP);
                    sprite.setRotation(sprite.getRotation() - 90);
                    break;
                case DOWN:
                    changeState(Direction.DOWN);
                    sprite.setRotation(sprite.getRotation() + 90);
                    break;
                default:
                    break;
            }
        }

        else if (currentDirection == Direction.DOWN){
            switch (turnDirection){
                case LEFT:
                    changeState(Direction.LEFT);
                    sprite.setRotation(sprite.getRotation() - 90);
                    break;
                case RIGHT:
                    changeState(Direction.RIGHT);
                    sprite.setRotation(sprite.getRotation() + 90);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public ArrayList<Trace> getTraces(){

        return traces;
    }

    @Override
    public void clean(){
        for (Trace trace: traces){
            trace.clean();
        }
    }

    @Override
    public boolean checkIfAssigned(){
        return isAssigned;
    }

    @Override
    public void vehicleAssigned(){
        isAssigned = true;
    }

}
