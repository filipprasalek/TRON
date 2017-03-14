package pl.agh.edu.kis.filipras.net;

import pl.agh.edu.kis.filipras.game.Player;
import pl.agh.edu.kis.filipras.game.PlayerColor;
import pl.agh.edu.kis.filipras.game.Tron;
import pl.agh.edu.kis.filipras.packets.*;

import java.io.IOException;
import java.net.*;

/**
 * Created by filipras on 30.12.2016.
 */
public class GameClient implements Runnable {

    private final int port;
    DatagramSocket clientSocket;
    Tron game;

    public GameClient(int port,Tron game) {
        this.game = game;
        this.port = port;
        try {
            clientSocket =  new DatagramSocket();
        } catch (SocketException e){
            e.printStackTrace();
        }

        sendData("ping".getBytes());
    }

    public void sendData(byte[] data){
        try {
            DatagramPacket datagramPacket = new DatagramPacket(data,data.length,InetAddress.getLocalHost(),port);
            clientSocket.send(datagramPacket);
        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port){
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0,PacketTypes.ID_LENGTH));

        Packet packet = null;

        switch (type){
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login)packet,address,port);
                break;

            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress() + ":"+port+"]"+((Packet01Disconnect)packet).getUsername()+ " has left");
                handleDisconnect((Packet01Disconnect) packet);
                break;

            case GAME_STATE:
                packet = new Packet02GameState(data);
                game.updateState(((Packet02GameState)packet).getNumberOfPlayers());
                break;

            case MOVE:
                packet = new Packet03Move(data);
                handleMove((Packet03Move)packet);
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        System.out.println("["+address.getHostAddress() + ":"+port+"]"+packet.getUsername()+ " has joined the game");
        Player player = new Player(address,port,packet.getUsername(), packet.getX(),packet.getY());
        game.addNewPlayer(player);
    }

    private void handleDisconnect(Packet01Disconnect packet){
        game.removePlayer(packet.getUsername());
    }

    private void handleMove(Packet03Move packet){
        Player player = game.getPlayer(packet.getUsername());
        try {
            player.setDirection(packet.getDirection());
            //player.updatePosition(packet.getX(), packet.getY());
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket receivedMessage = new DatagramPacket(data, data.length);

            try {
                clientSocket.receive(receivedMessage);
                parsePacket(receivedMessage.getData(),receivedMessage.getAddress(),receivedMessage.getPort());
            } catch (Exception e){
               e.printStackTrace();
            }

        }
    }
}
