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
    }

    public String toString()
    {
        return String.format("[%1$2d,%2$2d]", q, r);
    }
}
