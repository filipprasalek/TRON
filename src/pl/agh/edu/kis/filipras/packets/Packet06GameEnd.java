package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by filipras on 19.01.2017.
 */
public class Packet06GameEnd extends Packet{

    boolean won;
    InetAddress ip;
    int port;

    public Packet06GameEnd(boolean won, InetAddress ip, int port){
        super();
        this.won = won;
        this.ip = ip;
        this.port = port;
    }

    public Packet06GameEnd(byte[] data){
        super();
        this.won = Boolean.parseBoolean(readData(data));
    }

    public boolean getResult(){
        return won;
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
            return ("06"+won).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e){
            return new byte[0];
        }
    }
}
