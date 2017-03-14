package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

/**
 * Created by filipras on 14.01.2017.
 */

//TODO: ulepsze
public class Packet02ServerState extends Packet {

    private int numberOfPlayers;
    private boolean gameInProgress;

    // from server to client
    public Packet02ServerState(byte[] data){
        super(02);
        this.numberOfPlayers = Integer.parseInt(readData(data));
    }

    //from client to server
    public Packet02ServerState(int x){
        super(02);
        this.numberOfPlayers = x;
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
        return ("02"+this.numberOfPlayers).getBytes();
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }
}
