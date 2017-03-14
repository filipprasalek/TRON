package pl.agh.edu.kis.filipras.online;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Created by filipras on 24.01.2017.
 * Launcher serwera
 */
public class ServerLauncher {

    private ServerUDP gameServer;
    private static JFrame f;

    private static void createAndShowGUI(){
        f = new JFrame("Server Launcher");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(400, 200);
        f.add(new ServerLauncherGUI(f));
        f.setResizable(false);
        f.setLocationRelativeTo(null);
    }

    /**
     * Metoda sprawdza czy dany port jest dostepny
     * @param port Port do sprawdzania
     * @return Dostepnosc portu
     */
    public boolean portAvailable(int port) {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
        }

        return false;
    }

    /**
     * Metoda inicjujaca prace serwera
     * @param port port na ktorzym ma byc otwarty
     * @param players maksymalna ilosc graczy na serwerze
     */
    public void initServer (int port, int players){

        gameServer = new GameServer(port,players);
        Thread server = new Thread((GameServer)gameServer);
        server.start();

    }

    public static void main (String[] arg) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

}
