package pl.agh.edu.kis.filipras.online;

import com.badlogic.gdx.Gdx;
import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.game.GameState;
import pl.agh.edu.kis.filipras.game.Tron;
import pl.agh.edu.kis.filipras.packets.*;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by filipras on 30.12.2016.
 * Klient do porozumiewania sie z serwerem
 */
public class GameClient implements Runnable {

    private final int port;
    private final InetAddress host;
    private DatagramSocket clientSocket;
    private Tron game;

    /**
     * Konstruktor parametrowu
     * @param port port na ktorym klient ma nasluchiwac
     * @param game instancja gry ktora ma obslugiwac
     * @param host adres IP hosta
     */
    public GameClient(int port,Tron game,InetAddress host) {

        this.game = game;
        this.port = port;
        this.host = host;

        try {
            clientSocket =  new DatagramSocket();
            clientSocket.setSoTimeout(10000);
        } catch (SocketException e){
            e.printStackTrace();
        }
        game.assignClient(this);
    }

    /**
     * Metoda pozwalajaca na wyslanie danych
     * @param data Dane do wyslania
     */
    public void sendData(byte[] data){
        try {
            DatagramPacket datagramPacket = new DatagramPacket(data,data.length,host,port);
            clientSocket.send(datagramPacket);
        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (Exception e){
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
                handleDisconnect((Packet01Disconnect) packet);
                break;

            case SERVER_FULL:
                game.getUtils().changeGameState(GameState.ERROR);
                JOptionPane.showMessageDialog(null,"Server is full. Try again later.");
                Gdx.app.exit();
                break;

            case MOVE:
                packet = new Packet03Move(data);
                handleMove((Packet03Move)packet);
                break;

            case DUPLICATE_USERNAME:
                game.getUtils().changeGameState(GameState.ERROR);
                JOptionPane.showMessageDialog(null,"This username is already in use. Choose another one.");
                Gdx.app.exit();
                break;

            case GAME_START:
                handleGameStart();
                break;

            case GAME_END:
                packet = new Packet06GameEnd(data);
                handleGameEnd((Packet06GameEnd)packet);
                break;

            default:
                break;
        }
    }

    private void handleGameStart(){
        game.getUtils().changeGameState(GameState.READY_TO_PLAY);
    }

    private void handleGameEnd(Packet06GameEnd packet){
        if(packet.getResult()){
            game.getUtils().changeGameState(GameState.WON);
        } else {
            game.getUtils().changeGameState(GameState.LOST);
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        System.out.println("["+address.getHostAddress() + ":"+port+"]"+packet.getUsername()+ " has joined the game");
        Player player = new Player(address,port,packet.getUsername(), packet.getX(),packet.getY());
        game.getUtils().addNewPlayer(player);
    }

    private void handleDisconnect(Packet01Disconnect packet){
        game.getUtils().removePlayer(packet.getUsername());
    }

    private void handleMove(Packet03Move packet){
        Player player = game.getUtils().getPlayer(packet.getUsername());
        try {
            player.setDirection(packet.getDirection());
            player.updatePosition(packet.getX(), packet.getY());
        } catch (NullPointerException e){
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
            } catch (IOException e){
                if (game.getUtils().getGameState() == GameState.READY_TO_PLAY){
                    JOptionPane.showMessageDialog(null,"Server connection lost. Game is over.");
                    Gdx.app.exit();
                }
            }

        }
    }
}
