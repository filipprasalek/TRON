package pl.agh.edu.kis.filipras.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import pl.agh.edu.kis.filipras.entities.Trace;

/**
 * Created by filipras on 08.01.2017.
 * Klasa odpowiedzialana za sprawdzanie czy wystapila kolizja miedzy pojazdem a jego sladem
 */
public class IntersectionDetector {

    /**
     * Metoda sprawdza czy wystapila kolizja
     * @param x1 wspolrzedna pojazdu
     * @param y1 wspolrzedna pojazdu
     * @param x2 wspolrzedna pojazdu (przeciwlegla do x1)
     * @param y2 wspolrzedna pojazdu (przeciwlegla do y1)
     * @param trace slad pojazdu
     * @return true jesli wystapila kolizja, w innym przypadku false
     */
    public static boolean checkTraceAndVehicle(float x1, float y1, float x2, float y2, Line trace){

        /*Rectangle rectangle = sprite.getBoundingRectangle();

        float   x1 = rectangle.x, y1 = rectangle.y + rectangle.height,
                x2 = rectangle.x + rectangle.width, y2 = rectangle.y + rectangle.height,
                x3 = rectangle.x + rectangle.width, y3 = rectangle.y,
                x4 = rectangle.x, y4 = rectangle.y;*/

        //float[] traceCoords = trace.getTraceCoords();



        /*return ((sectionCollider(x1,y1,x2,y2,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])) &&
                (sectionCollider(x3,y3,x4,y4,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3]))) ||
                ((sectionCollider(x1,y1,x4,y4,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])) &&
                (sectionCollider(x3,y3,x2,y2,traceCoords[0],traceCoords[1],traceCoords[2],traceCoords[3])));*/

        return collider(x1,y1,x2,y2,trace.getX1(),trace.getY1(),trace.getX2(),trace.getY2());
    }

    private static boolean collider(float x1, float y1, float x2, float y2, float lx1, float ly1, float lx2 ,float ly2){

        //if (k1 <= Math.max(y1,y2) && k1 >= Math.min(y1,y2) && x1 >= Math.min(z1,z2) && x1 <= Math.max(z1,z2)) return true;

        //if (y1 <= Math.max(k1,k2) && y1 >= Math.min(k1,k2) && z1 >= Math.min(x1,x2) && z1 <= Math.max(x1,x2)) return true;

        //if((x1 == z1 && y1 == k1) || (x2 == z2 && y2 == k2)) return true;



        if (Math.min(x1,x2) <= lx1 && Math.max(x1,x2) >= lx1 && y1 >= Math.min(ly1,ly2) && y1 <= Math.max(ly1,ly2) &&
                (Math.min(x1,x2) <= lx2 && Math.max(x1,x2) >= lx2 && y2 >= Math.min(ly1,ly2) && y2 <= Math.max(ly1,ly2))) return true;

        if (Math.min(y1,y2) <= ly1 && Math.max(y1,y2) >= ly1 && x1 >= Math.min(lx1,lx2) && x1 <= Math.max(lx1,lx2) &&
                (Math.min(y1,y2) <= ly2 && Math.max(y1,y2) >= ly2 && x2 >= Math.min(lx1,lx2) && x2 <= Math.max(lx1,lx2))) return true;

        if(Math.max(ly1,ly2) > Math.min(y1,y2) && Math.max(ly1,ly2) < Math.max(y1,y2) && lx1 == lx2 && lx1 < Math.max(x1,x2) && lx1 > Math.min(x1,x2)) return true;
        if(Math.min(ly1,ly2) > Math.min(y1,y2) && Math.min(ly1,ly2) < Math.max(y1,y2) && lx1 == lx2 && lx1 < Math.max(x1,x2) && lx1 > Math.min(x1,x2)) return true;

        if(Math.min(lx1,lx2) > Math.min(x1,x2) && Math.min(lx1,lx2) < Math.max(x1,x2) && ly1 == ly2 && ly1 < Math.max(y1,y2) && ly1 > Math.min(y1,y2)) return true;
        if(Math.max(lx1,lx2) > Math.min(x1,x2) && Math.max(lx1,lx2) < Math.max(x1,x2) && ly1 == ly2 && ly1 < Math.max(y1,y2) && ly1 > Math.min(y1,y2)) return true;



        return false;
    }

}
