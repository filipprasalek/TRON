package pl.agh.edu.kis.filipras.net;


import pl.agh.edu.kis.filipras.game.Player;
import pl.agh.edu.kis.filipras.game.Tron;
import pl.agh.edu.kis.filipras.packets.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by filipras on 30.12.2016.
 */
public class GameServer implements Runnable {

    private DatagramSocket serverSocket;
    private ArrayList<Player> connectedPlayers = new ArrayList<Player>();
    private Tron game;

    public GameServer(int port, Tron game) {

        this.game = game;

        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException e){
            e.printStackTrace();
        }
    }

    private void sendData(byte[] data, InetAddress ip, int port){
        try {
            DatagramPacket datagramPacket = new DatagramPacket(data,data.length,ip,port);
            serverSocket.send(datagramPacket);
        } catch (UnknownHostException e){
            e.printStackTrace();
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
                removeConnection((Packet01Disconnect)packet);
                break;

            case GAME_STATE:
                packet = new Packet02GameState(connectedPlayers.size());
                packet.writeData(this);
                break;

            case MOVE:
                packet = new Packet03Move(data);
                this.handleMove(((Packet03Move)packet));
                break;

        }
    }


    public void addConnection(Player player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (Player p: connectedPlayers){
            if(player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.getIP() == null){
                    p.setIP(player.getIP());
                }

                if (p.getPort() == -1){
                    p.setPort(player.getPort());
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(),p.getIP(),p.getPort());

                packet = new Packet00Login(p.getUsername(),p.getPositionX(),p.getPositionY(),p.getDirection());
                sendData(packet.getData(),player.getIP(),player.getPort());
            }
        }

        if (!alreadyConnected){
            connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet){
        connectedPlayers.remove(getPlayerId(packet.getUsername()));
        packet.writeData(this);
    }

    private int getPlayerId(String username){
        int index = 0;

        for (Player player: connectedPlayers){
            if (player.getUsername().equals(username)){
                break;
            }
            index++;
        }

        return index;
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        System.out.println("["+address.getHostAddress() + ":"+port+"]"+((Packet00Login)packet).getUsername()+ " has connected");
        Player player = new Player(address,port,packet.getUsername(),packet.getX(),packet.getY());
        addConnection(player,packet);
    }

    private void handleMove(Packet03Move packet){
        if (getPlayer(packet.getUsername())!=null){
            int index = getPlayerId(packet.getUsername());
            connectedPlayers.get(index).setDirection(packet.getDirection());
            connectedPlayers.get(index).updatePosition(packet.getX(),packet.getY());
            packet.writeData(this);
        }
    }

    //TODO: wyjątek
    private Player getPlayer(String username){

        for (Player player : connectedPlayers){
            if (player.getUsername().equals(username)) return player;
        }

        return null;
    }

    @Override
    public void run() {

        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket receivedMessage = new DatagramPacket(data, data.length);

            try {
                serverSocket.receive(receivedMessage);
                parsePacket(receivedMessage.getData(),receivedMessage.getAddress(),receivedMessage.getPort());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDataToAllClients(byte[] data){
        for (Player player: connectedPlayers){
            sendData(data, player.getIP(), player.getPort());
        }
    }
}
