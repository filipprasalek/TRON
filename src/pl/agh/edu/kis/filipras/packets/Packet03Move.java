package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;

/**
 * Created by filipras on 14.01.2017.
 */
public class Packet03Move extends Packet{

    private String username;
    private float x,y;
    private Direction direction;
    private float[] coords;


    // from server to client
    public Packet03Move(byte[] data){
        super();
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.direction = Direction.getByText(dataArray[3]);
        coords = new float[4];
        coords[0] = Float.parseFloat(dataArray[4]);
        coords[1] = Float.parseFloat(dataArray[5]);
        coords[2] = Float.parseFloat(dataArray[6]);
        coords[3] = Float.parseFloat(dataArray[7]);

    }

    //from client to server
    public Packet03Move(String username,float x, float y,Direction direction,float[] vehicleCoords){
        super();
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.coords = vehicleCoords.clone();

    }

    @Override
    public void writeData(GameClient client){
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server){
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData(){
        try {
            return ("03"+this.username+","+x+","+y +","+direction.toString()+","+coords[0]+","+coords[1]+","+coords[2]+","+coords[3]).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }

    public String getUsername(){
        return username;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getX1(){
        return coords[0];
    }

    public float getY1(){
        return coords[1];
    }

    public float getX2(){
        return coords[2];
    }

    public float getY2(){
        return coords[3];
    }

    public Direction getDirection(){return direction;}

}
