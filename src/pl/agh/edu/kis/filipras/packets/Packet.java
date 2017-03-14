package pl.agh.edu.kis.filipras.packets;

import pl.agh.edu.kis.filipras.online.GameClient;
import pl.agh.edu.kis.filipras.online.GameServer;

import java.io.UnsupportedEncodingException;

/**
 * Created by filipras on 13.01.2017.
 */
public abstract class Packet implements PacketInterface{

    public Packet(){
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data){
        try {
            String message = new String(data,"UTF-8").trim();
            return message.substring(PacketTypes.ID_LENGTH);
        } catch (UnsupportedEncodingException e){
            return "-1";
        }
    }

    /**
     * Metoda pozwala na zidentyfikowanie rodzaju pakietu przy pomocy jego ID
     * @param packetId ID pakietu
     * @return typ pakitu
     */
    public static PacketTypes lookupPacket(String packetId){
        try{
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e){
            return PacketTypes.INVALID;
        }
    }

    /**
     * Metoda pozwala na zidentyfikowanie rodzaju pakietu przy pomocy jego ID
     * @param id ID pakietu
     * @return typ pakitu
     */
    public static PacketTypes lookupPacket(int id){
        for (PacketTypes packet : PacketTypes.values()){
            if(packet.getId() == id){
                return packet;
            }
        }
        return PacketTypes.INVALID;
    }

    public abstract byte[] getData();
}
