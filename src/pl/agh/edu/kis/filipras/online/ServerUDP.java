package pl.agh.edu.kis.filipras.online;

import pl.agh.edu.kis.filipras.entities.Player;
import pl.agh.edu.kis.filipras.entities.User;
import pl.agh.edu.kis.filipras.packets.Packet;
import pl.agh.edu.kis.filipras.packets.Packet00Login;
import pl.agh.edu.kis.filipras.packets.Packet01Disconnect;
import pl.agh.edu.kis.filipras.packets.PacketInterface;

import java.net.InetAddress;

/**
 * Created by filipras on 30.12.2016.
 */
public interface ServerUDP {

    /**
     * Metoda umozliwia wysylanie danych na odpowiedni adres
     * @param data dane do wyslania
     * @param ip docelowy adres ip
     * @param port docelowy port
     */
    void sendData(byte[] data, InetAddress ip, int port);

    /**
     * Metoda obslugujaca dodanie dowego polaczenia do serwera
     * @param player gracz ktory bedzie dodany do listy obslugiwanych polaczen
     * @param packet pakiet danych niezbedny do utworzenia polaczenia
     */
    void addConnection(User player, PacketInterface packet);

    /**
     * Metoda umozliwiajaca usuniecie polaczenia
     * @param packet pakiet danych niezbedy do usuniecia polaczenia
     */
    void removeConnection(PacketInterface packet);

    /**
     * Metoda umozliwiajaca wyslanie danych do wszyskich aktualnie polaczonych uzytkownikow
     * @param data dane do wyslania
     */
    void sendDataToAllClients(byte[] data);
}
