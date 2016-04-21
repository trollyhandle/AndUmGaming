package Game;

/**
 * Hexagon.java
 * Author: Tyler Holland
 * Represents a hexagon in the game board
 */
public class Hexagon extends Shape {

    public Hexagon(int q, int r)
    {
        super(q, r);
        poly_size = hex_size / 4;
    }

    public String type() { return "Hexagon"; }


    


}
