package Game;

/**
 * Vertex.java
 * Author: Tyler Holland
 * Represents a vertex in the game board
 */
public class Vertex extends Shape {

    public Vertex(int q, int r)
    {
        super(q, r);
    }

    public String toString()
    {
        return String.format("(%1$2d,%2$2d)", q, r);
    }
}
