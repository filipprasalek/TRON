package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.net.GameClient;
import pl.agh.edu.kis.filipras.net.GameServer;

/**
 * Created by filipras on 14.01.2017.
 */

//TODO: ulepsze
public class Packet02GameState extends Packet {

    private int numberOfPlayers;

    // from server to client
    public Packet02GameState(byte[] data){
        super(02);
        this.numberOfPlayers = Integer.parseInt(readData(data));
    }

    //from client to server
    public Packet02GameState(int numberOfPlayers){
        super(02);
        this.numberOfPlayers = numberOfPlayers;
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
