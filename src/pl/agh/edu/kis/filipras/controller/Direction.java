package pl.agh.edu.kis.filipras.controller;

/**
 * Created by filipras on 07.01.2017.
 */
public enum Direction {
    UP("UP"),DOWN("DOWN"),LEFT("LEFT"),RIGHT("RIGHT");

    private final String text;

    Direction(final String text){
        this.text = text;
    }

    public static Direction getByText(String text){
        for (Direction direction : values()) {
            if (direction.toString().equals(text)){
                return direction;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
