package pl.agh.edu.kis.filipras.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import pl.agh.edu.kis.filipras.controller.Direction;
import pl.agh.edu.kis.filipras.entities.Motorcycle;
import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.entities.PlayerColor;
import pl.agh.edu.kis.filipras.entities.Vehicle;
import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.packets.Packet00Login;
import pl.agh.edu.kis.filipras.packets.Packet01Disconnect;
import pl.agh.edu.kis.filipras.packets.Packet03Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa reprezentujaca rozgrywke
 */
public class Tron extends ApplicationAdapter {

    public static Tron game;

    private TronUtils config;
    private GameClient gameClient;

    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private Music backgroundMusic;
    private Sprite waitingScreen;
    private Sprite winnerScreen;
    private Sprite looserScreen;
    private Sprite playAgainScreen;
    private Sprite yesScreen;
    private Sprite noScreen;
    public Array<Sprite> vehicleTextures = new Array<Sprite>();

    private String localPlayerUsername;
    private int playAgainAnswer;


    public Tron(String username){
        localPlayerUsername = username;
        config = new TronUtils(username);
        game = this;
    }

    /**
     * Metoda zwraca konfiguracje rozgrywki
     * @return konfiguracja rozgrywki
     */
    public TronUtils getUtils(){
        return config;
    }

    /**
     * Metoda przypisuje klienta do danej rozgrywki
     * @param client Klient do przypisania
     */
    public void assignClient(GameClient client){
        gameClient = client;
        config.assignClient(client);
    }

    /**
     * Metoda zwraca klienta
     * @return Przypisany klient
     */
    public GameClient getClient(){
        return gameClient;
    }

    /**
     * Metoda zwraca narzedzie odpowiedzialne za rysowanie
     * @return Narzedzie do rysowania
     */
    public ShapeRenderer getRenderer(){
        return renderer;
    }

    private void loadVehicleTextures(){

        vehicleTextures.add(new Sprite(new Texture("motorcycle_blue.png")));
        vehicleTextures.add(new Sprite(new Texture("motorcycle_yellow.png")));
        vehicleTextures.add(new Sprite(new Texture("motorcycle_green.png")));
        vehicleTextures.add(new Sprite(new Texture("motorcycle_red.png")));

        for (Sprite s: vehicleTextures) {
            s.setOrigin(0,s.getHeight()/2);
            s.setCenter(100,100);
            s.setRotation(s.getRotation() + 90);
        }
    }

    private void loadScreens(){
        waitingScreen = new Sprite(new Texture("waiting.png"));
        waitingScreen.setCenter((float)Gdx.graphics.getWidth() / 2 ,(float)Gdx.graphics.getHeight()/2);
        winnerScreen = new Sprite(new Texture("won.png"));
        winnerScreen.setCenter((float)Gdx.graphics.getWidth() / 2 ,(float)Gdx.graphics.getHeight()/2);
        looserScreen = new Sprite(new Texture("lost.png"));
        looserScreen.setCenter((float)Gdx.graphics.getWidth() / 2,(float)Gdx.graphics.getHeight()/2);
        playAgainScreen = new Sprite(new Texture("play_again.png"));
        playAgainScreen.setCenter((float)Gdx.graphics.getWidth() / 2,(float)Gdx.graphics.getHeight()/2);
        yesScreen = new Sprite(new Texture("yes.png"));
        yesScreen.setCenter((float)Gdx.graphics.getWidth() / 2,(float)Gdx.graphics.getHeight()/2);
        noScreen = new Sprite(new Texture("no.png"));
        noScreen.setCenter((float)Gdx.graphics.getWidth() / 2,(float)Gdx.graphics.getHeight()/2);

    }

	@Override
    public void create () {

        renderer = new ShapeRenderer();

        loadVehicleTextures();
        loadScreens();
        config.addVehicles();

        Player player = new Player(localPlayerUsername, 400, 100, this);
        config.assignLocalPlayer(player);
        Packet00Login login = new Packet00Login(player.getUsername(), player.getPositionX(), player.getPositionY(), player.getDirection());
        login.writeData(gameClient);
        config.addNewPlayer(player);

        batch = new SpriteBatch();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("game_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

    }

	@Override
	public void render () {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(config.getGameState() == GameState.WAITING ) {

            if(batch != null) {
                batch.begin();
                waitingScreen.draw(batch);
                batch.end();
            }

        } else if (config.getGameState() == GameState.WON){

            synchronized (config.getVehicleList()) {
                for (Vehicle vehicle : config.getVehicleList()) {
                    if (vehicle.checkIfAssigned()) vehicle.draw(batch);
                }
            }

            if(batch != null){
                batch.begin();
                winnerScreen.draw(batch);
                batch.end();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) config.changeGameState(GameState.PLAY_AGAIN);

        } else if (config.getGameState() == GameState.LOST) {

            synchronized (config.getVehicleList()) {
                for (Vehicle vehicle : config.getVehicleList()) {
                    if (vehicle.checkIfAssigned()) vehicle.draw(batch);
                }
            }

            if (batch != null) {
                batch.begin();
                looserScreen.draw(batch);
                batch.end();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) config.changeGameState(GameState.PLAY_AGAIN);

        } else if (config.getGameState() == GameState.PLAY_AGAIN) {

            if(batch != null) {
                batch.begin();
                playAgainScreen.draw(batch);
                yesScreen.draw(batch);
                noScreen.draw(batch);
                batch.end();
            }

            if (renderer!= null && playAgainAnswer == 0) {
                renderer.setColor(Color.GREEN);
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.line(635, 200, 750, 200);
                renderer.end();
            }

            if (renderer!= null && playAgainAnswer == 1) {
                renderer.setColor(Color.GREEN);
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.line(375, 200, 490, 200);
                renderer.end();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                if (playAgainAnswer == 1) playAgainAnswer = 0;
                else playAgainAnswer = 1;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                if (playAgainAnswer == 1) config.changeGameState(GameState.PLAY_AGAIN_YES);
                else if (playAgainAnswer == 0) config.changeGameState(GameState.PLAY_AGAIN_NO);
            }

        } else if(config.getGameState() == GameState.READY_TO_PLAY) {

            for (Vehicle vehicle : config.getVehicleList()) {
                if (vehicle.checkIfAssigned()) vehicle.draw(batch);
            }


            config.getLocalPlayer().play();

            if (renderer!= null) {
                renderer.setColor(Color.GREEN);
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.line(1, 1, 1200, 1);
                renderer.line(1, 1, 1, 700);
                renderer.line(1200, 0, 1200, 700);
                renderer.line(0, 699, 1200, 699);
                renderer.end();
            }
        }
    }

	@Override
	public void dispose() {

        if (config.getGameState() == GameState.WAITING || config.getGameState() == GameState.READY_TO_PLAY ) {
            Packet01Disconnect packet = new Packet01Disconnect(true,config.getLocalPlayer().getUsername());
            packet.writeData(gameClient);
        }

        if (config.getGameState() != GameState.WAITING) {
            if (batch != null) batch.dispose();
            if (renderer != null) renderer.dispose();
        }

    }
}
