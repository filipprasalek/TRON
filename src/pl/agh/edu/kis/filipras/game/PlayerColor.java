package pl.agh.edu.kis.filipras.game;

/**
 * Created by filipras on 08.01.2017.
 */
public enum PlayerColor {
    BLUE(0),YELLOW(1),GREEN(2),RED(3);

    private int colorId;

    PlayerColor(int colorId){
        this.colorId = colorId;
    }

    //// TODO: 13.01.2017 WYJĄTKI!
    public static PlayerColor getById(int id) {
        for(PlayerColor c : values()) {
            if(c.colorId == id) return c;
        }
        return null;
    }

    public int getId(){
        return colorId;
    }
}
