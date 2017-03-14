package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by filipras on 13.01.2017.
 */
public class Packet00Login extends Packet {

    private String username;
    float x,y;
    Direction direction;

    // from server to client
    public Packet00Login(byte[] data){
        super();
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.direction = Direction.getByText(dataArray[3]);
    }

    //from client to server
    public Packet00Login(String username,float x, float y, Direction direction){
        super();
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
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
            return ("00"+this.username+","+x+","+y+","+direction.toString()).getBytes("UTF-8");
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

    public Direction getDirection(){
        return direction;
    }

}
