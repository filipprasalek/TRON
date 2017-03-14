package pl.agh.edu.kis.filipras.packets;

/**
 * Created by filipras on 13.01.2017.
 */
public enum PacketTypes {
    INVALID(-1), LOGIN(00), DISCONNECT(01), SERVER_FULL(02), MOVE(03), DUPLICATE_USERNAME(04),
    GAME_START(05), GAME_END(06), TRACE(07);

    private int packetId;

    public final static int ID_LENGTH = 2;

    PacketTypes(int packetId){
        this.packetId = packetId;
    }

    public int getId(){
        return packetId;
    }
}
