package pl.agh.edu.kis.filipras.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.entities.Motorcycle;
import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.entities.PlayerColor;
import pl.agh.edu.kis.filipras.entities.Vehicle;
import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;
import pl.agh.edu.kis.filipras.packets.Packet00Login;
import pl.agh.edu.kis.filipras.packets.Packet01Disconnect;
import pl.agh.edu.kis.filipras.packets.Packet03Move;

import javax.swing.*;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by filipras on 18.01.2017.
 */
public class TronUtils implements GameUtils{

    private GameState gameState = GameState.WAITING;
    private Player localPlayer;
    private String localPlayerUsername;
    private int numberOfPlayers;
    private List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<Vehicle>());
    private List<Player> listOfPlayers = Collections.synchronizedList(new ArrayList<Player>());
    private GameClient gameClient;

    public TronUtils(String username){
        localPlayerUsername = username;
    }

    /**
     * Metoda przypisuje klienta do gry
     * @param client Klient ktory ma byc przypisany
     */
    public void assignClient(GameClient client){
        gameClient = client;

    }

    public void changeGameState(GameState gameState){

        this.gameState = gameState;

        if (gameState == GameState.READY_TO_PLAY){
            startAllPlayers();
            return;
        }

        if (gameState == GameState.LOST || gameState == GameState.WON){

            Packet01Disconnect packet = new Packet01Disconnect(false,localPlayer.getUsername());
            packet.writeData(gameClient);

            stopAllPlayers();

            /*int response = JOptionPane.showConfirmDialog(null, "Do you want to play one more time?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                clear();
                this.gameState = GameState.WAITING;
                relogin();
            } else if (response == JOptionPane.NO_OPTION){
                Gdx.app.exit();
            }*/

        }

        if (gameState == GameState.PLAY_AGAIN){
            clear();
            return;
        }

        if (gameState == GameState.PLAY_AGAIN_YES){
            this.gameState = GameState.WAITING;
            relogin();
            return;
        }

        if (gameState == GameState.PLAY_AGAIN_NO){
            Gdx.app.exit();
        }

    }

    public GameState getGameState(){
        return gameState;
    }

    public void addVehicle(Vehicle v){
        vehicles.add(v);
    }

    public void addNewPlayer(Player player) {
        player.assignVehicle(vehicles.get(numberOfPlayers));
        vehicles.get(numberOfPlayers).vehicleAssigned(true);
        listOfPlayers.add(player);
        numberOfPlayers++;
    }

    /**
     * Metoda dodaje pojazdy do gry
     */
    public void addVehicles(){
        for (int i = 0; i < 4; i++){
            addVehicle(new Motorcycle(PlayerColor.getById(i)));
        }
    }

    public void removePlayer(String username){
        synchronized (listOfPlayers){
            for (Player player: listOfPlayers) {
                if (player.getUsername().equals(username)){
                    listOfPlayers.remove(player);
                    break;
                }
            }
        }
    }

    /**
     * Metoda zwraca gracza przy pomocy jego usenrame'a
     * @param username Nick gracza
     * @return Gracz
     */
    public Player getPlayer(String username){
        synchronized (listOfPlayers){
            for (Player player: listOfPlayers) {
                if (player.getUsername().equals(username)){
                    return player;
                }
            }
        }

        return null;
    }

    /**
     * Metoda zwraca lokalego gracza, sterowanego przez uzytkownika aplikacji
     * @return lokalny gracz
     */
    public Player getLocalPlayer(){return localPlayer;}

    /**
     * Metoda przypisuje lokalnego gracza
     * @param player Docelowy lokalny gracz
     */
    public void assignLocalPlayer(Player player){localPlayer = player;}

    /**
     * Metoda zwraca liste pojazdow w grze
     * @return lista pojazdow w grze
     */
    public List<Vehicle> getVehicleList(){return vehicles;}

    private void clear(){
        listOfPlayers.clear();
        for (Vehicle v: vehicles) {
            v.vehicleAssigned(false);
        }
        localPlayer.setDirection(Direction.UP);
        localPlayer.updatePosition(400,100);
        vehicles.clear();
        numberOfPlayers = 0;
    }

    private void relogin(){
        Packet00Login login = new Packet00Login(localPlayerUsername,400,100, Direction.UP);
        login.writeData(gameClient);
        addVehicles();
        addNewPlayer(localPlayer);

    }

    private void stopAllPlayers(){
        for (Player p: listOfPlayers) p.changeMovementState(false);
    }

    private void startAllPlayers(){
        for (Player p: listOfPlayers) p.changeMovementState(true);
    }


}