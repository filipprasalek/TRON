package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;

/**
 * Created by filipras on 14.01.2017.
 */
public class Packet01Disconnect extends Packet{

    private String username;
    private boolean error;

    // from server to client
    public Packet01Disconnect(byte[] data){
        super();
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.error = Boolean.parseBoolean(dataArray[1]);
    }

    //from client to server
    public Packet01Disconnect(boolean error,String username){
        super();
        this.username = username;
        this.error = error;
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
            return ("01"+this.username+","+error).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }

    public String getUsername(){
        return username;
    }

    public boolean wasError(){return error;}
}
