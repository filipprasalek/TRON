package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by filipras on 23.01.2017.
 */
public class Packet07Trace extends Packet {

    private String username;
    private float x1,y1,x2,y2;

    public Packet07Trace(String username, float x1, float y1, float x2, float y2){
        super();
        this.username = username;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Packet07Trace(byte[] data){
        super();
        String[] dataArray = (readData(data)).split(",");
        this.x1 = Float.parseFloat(dataArray[0]);
        this.y1 = Float.parseFloat(dataArray[1]);
        this.x2 = Float.parseFloat(dataArray[2]);
        this.y2 = Float.parseFloat(dataArray[3]);
        this.username = dataArray[4];
    }

    public float getX1(){
        return x1;
    }

    public float getY1(){
        return y1;
    }

    public float getX2(){
        return x2;
    }

    public float getY2(){
        return y2;
    }

    public String getUsername(){
        return username;
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
            return ("07"+x1+","+y1+","+x2+","+y2+","+username).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }
}
