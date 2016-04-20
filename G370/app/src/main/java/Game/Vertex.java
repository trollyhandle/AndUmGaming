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

    public String type() { return "Vertex"; }
}
