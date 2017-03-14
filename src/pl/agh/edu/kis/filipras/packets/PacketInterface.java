package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

/**
 * Created by filipras on 13.01.2017.
 */
public interface PacketInterface {

    /**
     * Metoda umozliwiajaca wyslanie danych w pakiecie za pomoca klienta
     * @param client klient, majacy wyslac dane
     */
    void writeData(GameClient client);

    /**
     * Metoda umozliwaijaca wyslanie danych w pakiecie za pomoca serwera
     * @param server serwer, majacy wyslac dane
     */
    void writeData(GameServer server);

    /**
     * Metoda pozwalajaca na zidentyfikowanie typu pakietu, co jest konieczne przy jego obsluzeniu
     * @param data dane pakietu
     * @return
     */
    String readData(byte[] data);

    /**
     * Metoda zwracajaca dane pakietu w bajtach
     * @return
     */
    byte[] getData();
}
