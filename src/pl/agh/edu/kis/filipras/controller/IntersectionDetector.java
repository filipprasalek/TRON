package pl.agh.edu.kis.filipras.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import pl.agh.edu.kis.filipras.game.Trace;

/**
 * Created by filipras on 08.01.2017.
 */
public class IntersectionDetector {
    public static boolean checkTraceAndVehicle(Sprite sprite, Trace trace){

        Rectangle rectangle = sprite.getBoundingRectangle();

        float   x1 = rectangle.x, y1 = rectangle.y + rectangle.height,
                x2 = rectangle.x + rectangle.width, y2 = rectangle.y + rectangle.height,
                x3 = rectangle.x + rectangle.width, y3 = rectangle.y,
                x4 = rectangle.x, y4 = rectangle.y;

        float[] traceCoords = trace.getTraceCoords();



        return ((sectionCollider(x1,y1,x2,y2,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])) &&
                (sectionCollider(x3,y3,x4,y4,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3]))) ||
                ((sectionCollider(x1,y1,x4,y4,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])) &&
                (sectionCollider(x3,y3,x2,y2,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])));
    }

    private static boolean sectionCollider(float x1, float y1, float x2, float y2, float z1, float k1, float z2 ,float k2){

        if((x1 == z1 && y1 == k1) || (x2 == z2 && y2 == k2)) return true;

        if (k1 <= Math.max(y1,y2) && k1 >= Math.min(y1,y2) && x1 >= Math.min(z1,z2) && x1 <= Math.max(z1,z2)) return true;

        if (y1 <= Math.max(k1,k2) && y1 >= Math.min(k1,k2) && z1 >= Math.min(x1,x2) && z1 <= Math.max(x1,x2)) return true;

        return false;
    }
}
