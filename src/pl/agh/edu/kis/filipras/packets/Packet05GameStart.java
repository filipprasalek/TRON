package pl.agh.edu.kis.filipras.packets;


import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;

/**
 * Created by filipras on 19.01.2017.
 */
public class Packet05GameStart extends Packet{

    // from server to client
    public Packet05GameStart(){
        super();
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
        try{
            return ("05").getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }

}
