package pl.agh.edu.kis.filipras.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.game.Tron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by filipras on 07.01.2017.
 */
public class Motorcycle implements Vehicle {

    private Direction currentDirection;
    private Sprite sprite;
    private List<Trace> traces;
    private PlayerColor playerColor;

    private boolean isAssigned;

    public Motorcycle(PlayerColor playerColor){

        this.playerColor = playerColor;
        loadAppearance();

        currentDirection = Direction.UP;
        sprite.setCenter(100,100);

        /*sprite.setOrigin(0,sprite.getHeight()/2);
        sprite.setCenter(100,100);
        sprite.setRotation(sprite.getRotation() + 90);*/

        traces = Collections.synchronizedList(new ArrayList<Trace>());
        traces.add(new Trace(this,playerColor));
    }

    public float getPositionX(){
        return sprite.getX();
    }

    public float getPositionY(){
        return sprite.getY();
    }

    public float getWidth(){
        return sprite.getWidth();
    }

    public float getHeight(){
        return sprite.getHeight();
    }

    private void loadAppearance(){
        switch (playerColor){
            case BLUE:
                sprite = Tron.game.vehicleTextures.get(0);
                break;
            case GREEN:
                //sprite = new Sprite(new Texture("motorcycle_green.png"));
                sprite = Tron.game.vehicleTextures.get(2);

                break;
            case RED:
                //sprite = new Sprite(new Texture("motorcycle_red.png"));
                sprite = Tron.game.vehicleTextures.get(3);

                break;
            case YELLOW:
                //sprite = new Sprite(new Texture("motorcycle_yellow.png"));
                sprite = Tron.game.vehicleTextures.get(1);

                break;
        }
    }

    private void changeState(Direction direction){
        currentDirection = direction;
        if (traces.size() > 0) traces.get(traces.size()-1).finalTraceShape();
        traces.add(new Trace(this,playerColor));
    }


    @Override
    public float[] getRoundigRectangle(){

        Rectangle rectangle = sprite.getBoundingRectangle();
        float   x1 = rectangle.x, y1 = rectangle.y,
                x2 = rectangle.x + rectangle.width, y2 = rectangle.y+rectangle.height;

        float[] toReturn = {x1,y1,x2,y2};

        return toReturn;
    }

    @Override
    public Direction getDirection() {
        return currentDirection;
    }


    @Override
    public void setPosition(float x, float y){
        sprite.setPosition(x,y);
    }

    @Override
    public void draw(SpriteBatch sb) {

        sb.begin();
        sprite.draw(sb);
        sb.end();

        synchronized (traces){
            for (Trace trace : traces) {
                trace.drawTrace();
            }
        }

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
    public boolean checkIfAssigned(){
        return isAssigned;
    }

    @Override
    public void vehicleAssigned(boolean assigned){
        isAssigned = assigned;
        if (assigned == false){
            if (getDirection() == Direction.LEFT){
                sprite.setRotation(sprite.getRotation() - 90);
            } else if(getDirection() == Direction.RIGHT){
                sprite.setRotation(sprite.getRotation() + 90);
            } else if (getDirection() == Direction.DOWN){
                sprite.setRotation(sprite.getRotation() - 180);
            }
            traces.clear();
            currentDirection = Direction.UP;
        }
    }

}
