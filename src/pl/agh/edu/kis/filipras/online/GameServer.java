package pl.agh.edu.kis.filipras.online;

import pl.agh.edu.kis.filipras.engine.IntersectionDetector;
import pl.agh.edu.kis.filipras.engine.Line;
import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.entities.User;
import pl.agh.edu.kis.filipras.packets.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * Created by filipras on 30.12.2016.
 */
public class GameServer implements Runnable,ServerUDP {

    private DatagramSocket serverSocket;
    private List<Player> connectedPlayers = Collections.synchronizedList(new ArrayList<Player>());
    private boolean gameInProgress;
    private HashMap<InetAddress,Long> time = new HashMap<InetAddress,Long>();
    private final int REQUIRED_PLAYERS;
    private static final int HEIGHT = 700;
    private static final int WIDTH = 1200;
    boolean timeout;

    private ArrayList<Line> traces;
    private HashMap<String,float[]> lastPositions;

    public GameServer(int port, int players){

        REQUIRED_PLAYERS = players;
        traces = new ArrayList<Line>();
        lastPositions = new HashMap<String, float[]>();

        try {
            serverSocket = new DatagramSocket(port);
            serverSocket.setSoTimeout(10000);
        } catch (SocketException e){
            e.printStackTrace();
        }

    }

    @Override
    public void sendData(byte[] data, InetAddress ip, int port){
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

        PacketTypes type;

        try {
            String message = new String(data,"UTF-8").trim();
            type = Packet.lookupPacket(message.substring(0,PacketTypes.ID_LENGTH));
        } catch (UnsupportedEncodingException e){
            type = PacketTypes.INVALID;
        }

        Packet packet;

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
                handleDisconnect((Packet01Disconnect)packet);
                break;

            case MOVE:
                packet = new Packet03Move(data);
                this.handleMove(((Packet03Move)packet));
                break;

            case TRACE:
                packet = new Packet07Trace(data);
                addTrace((Packet07Trace)packet);
                break;

            default:
                break;

        }
    }

    @Override
    public void addConnection(User player, PacketInterface packet) {

        boolean alreadyConnected = false;

        synchronized (connectedPlayers) {
            for (Player p : connectedPlayers) {
                if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                    if (p.getIP() == null) {
                        p.setIP(player.getIP());
                    }

                    if (p.getPort() == -1) {
                        p.setPort(player.getPort());
                    }
                    alreadyConnected = true;

                } else {
                    sendData(packet.getData(), p.getIP(), p.getPort());

                    packet = new Packet00Login(p.getUsername(), p.getPositionX(), p.getPositionY(), p.getDirection());
                    sendData(packet.getData(), player.getIP(), player.getPort());
                }
            }
        }

        if (!alreadyConnected){
            connectedPlayers.add((Player)player);
        }
    }

    @Override
    public void removeConnection(PacketInterface packet){
        try {
            connectedPlayers.remove(getPlayerId(((Packet01Disconnect)packet).getUsername()));
            if (gameInProgress && connectedPlayers.size() == 0){
                clearServer();
            }
        } catch (IndexOutOfBoundsException e){
        } catch (ClassCastException ex){
            return;
        }
        packet.writeData(this);
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){

        if (checkForDuplicateName(packet.getUsername())){
            Packet04DuplicateUsername duplicateUsername = new Packet04DuplicateUsername(address,port);
            duplicateUsername.writeData(this);
            return;
        }

        if(!gameInProgress) {
            time.put(address,System.currentTimeMillis());
            Player player = new Player(address, port, packet.getUsername(), 400 + 400*connectedPlayers.size(), 100);
            packet = new Packet00Login(packet.getUsername(),400 + 400*connectedPlayers.size(),100,packet.getDirection());
            addConnection(player, packet);
            float[] tabOfCoords = {player.getPositionX(),player.getPositionY()};
            lastPositions.put(packet.getUsername(),tabOfCoords);

        } else {
            Packet02ServerFull packetFull = new Packet02ServerFull(address,port);
            packetFull.writeData(this);
            return;
        }

        if (connectedPlayers.size() == REQUIRED_PLAYERS) sendGameStartPacket();
    }

    private void handleDisconnect(Packet01Disconnect packet){
        removeConnection(packet);
        System.out.println("DISCONNECT");
        if(packet.wasError() && connectedPlayers.size() == 1){
            Packet06GameEnd packetWon = new Packet06GameEnd(true,connectedPlayers.get(0).getIP(),connectedPlayers.get(0).getPort());
            packetWon.writeData(this);
        }
    }

    private void handleCollsion(Player movingPlayer){

        Packet06GameEnd packetEndLost = new Packet06GameEnd(false,movingPlayer.getIP(),movingPlayer.getPort());
        System.out.println(movingPlayer.getPositionX() + " " + movingPlayer.getPositionY() + movingPlayer.getUsername());

        if (connectedPlayers.size() == 2) {
            for (Player player : connectedPlayers){
                if (!player.getUsername().equals(movingPlayer.getUsername())){
                    Packet06GameEnd packetEndWon = new Packet06GameEnd(true,player.getIP(),player.getPort());
                    packetEndWon.writeData(this);
                    packetEndLost.writeData(this);
                    clearServer();
                    return;
                }
            }
        }

        packetEndLost.writeData(this);
    }

    private void handleMove(Packet03Move packet){
        if (getPlayer(packet.getUsername())!=null){

            int index = getPlayerId(packet.getUsername());
            Player movingPlayer = connectedPlayers.get(index);

            if (timeout){
                Packet06GameEnd won = new Packet06GameEnd(true,movingPlayer.getIP(),movingPlayer.getPort());
                won.writeData(this);
                return;
            }

            movingPlayer.setDirection(packet.getDirection());
            movingPlayer.updatePosition(packet.getX(),packet.getY());

            packet.writeData(this);

            for (Line l :traces){
                if(IntersectionDetector.checkTraceAndVehicle(packet.getX1(),packet.getY1(),packet.getX2(),packet.getY2(),l)){
                    handleCollsion(movingPlayer);
                    return;
                }
            }

            synchronized (connectedPlayers){
                for (Player p : connectedPlayers) {
                    if (!p.getUsername().equals(movingPlayer.getUsername())){
                        Line templine = new Line(lastPositions.get(p.getUsername())[0],lastPositions.get(p.getUsername())[1],p.getPositionX(),p.getPositionY());
                        if (IntersectionDetector.checkTraceAndVehicle(packet.getX1(), packet.getY1(), packet.getX2(), packet.getY2() , templine)) {
                            handleCollsion(movingPlayer);
                            return;
                        }
                    }
                }
            }

            if (packet.getX2() > WIDTH || packet.getX1() < 0 || packet.getY2() > HEIGHT || packet.getY1() < 0){
                handleCollsion(movingPlayer);
            }

        }
    }

    private boolean checkForDuplicateName(String name){

        synchronized (connectedPlayers) {
            for (Player player : connectedPlayers) {
                if (name.equals(player.getUsername())) return true;
            }
        }

        return false;
    }

    private void sendGameStartPacket(){
        for (Player p : connectedPlayers){
            Packet03Move packet03Move = new Packet03Move(p.getUsername(),p.getPositionX(),p.getPositionY(),p.getDirection(),p.getVehicleCoords());
            packet03Move.writeData(this);
        }

        Iterator it = time.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            time.put((InetAddress)pair.getKey(),System.currentTimeMillis());
        }

        gameInProgress = true;
        Packet05GameStart packet = new Packet05GameStart();
        packet.writeData(this);
    }

    private int getPlayerId(String username){
        int index = 0;

        synchronized (connectedPlayers) {
            for (Player player : connectedPlayers) {
                if (player.getUsername().equals(username)) {
                    break;
                }
                index++;
            }
        }

        return index;
    }

    private Player getPlayer(String username){

        synchronized (connectedPlayers) {
            for (Player player : connectedPlayers) {
                if (player.getUsername().equals(username)) return player;
            }
        }

        return null;
    }

    private void addTrace(Packet07Trace packet){

        float[] tabOfCoords = {packet.getX2(),packet.getY2()};
        lastPositions.put(packet.getUsername(),tabOfCoords);
        traces.add(new Line(packet.getX1(),packet.getY1(),packet.getX2(),packet.getY2()));

    }

    private void clearServer(){
        connectedPlayers.clear();
        traces.clear();
        lastPositions.clear();
        gameInProgress = false;
        Iterator it = time.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            time.put((InetAddress)pair.getKey(),System.currentTimeMillis());
        }

    }

    @Override
    public void run() {

        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket receivedMessage = new DatagramPacket(data, data.length);

            try {
                serverSocket.receive(receivedMessage);
                parsePacket(receivedMessage.getData(),receivedMessage.getAddress(),receivedMessage.getPort());

                if (time.get(receivedMessage.getAddress())!= null) {
                    time.put(receivedMessage.getAddress(), System.currentTimeMillis());
                }

                Iterator it = time.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    System.out.println(System.currentTimeMillis() - (Long) pair.getValue());
                    if (System.currentTimeMillis() - (Long) pair.getValue()  > 3000){
                        if (gameInProgress) timeout = true;
                    }
                }

                /*while (it.hasNext()) {
                    if((Map.Entry)it).getValue() > (long)3000){
                        timeout = true;
                    }
                }*/
            } catch (IOException e) {
                if (gameInProgress) clearServer();
            } catch (NullPointerException e1){
            }
        }
    }

    @Override
    public void sendDataToAllClients(byte[] data){
        for (Player player: connectedPlayers){
            sendData(data, player.getIP(), player.getPort());
        }
    }
}
