package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by filipras on 21.01.2017.
 */
public class Packet04DuplicateUsername extends Packet {

    InetAddress ip;
    int port;

    //from client to server
    public Packet04DuplicateUsername(InetAddress ip, int port){
        super();
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void writeData(GameClient client){
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server){
        server.sendData(getData(),ip,port);
    }

    @Override
    public byte[] getData(){
        try {
            return ("04").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }
}
