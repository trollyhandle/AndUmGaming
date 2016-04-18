package Game;

import java.util.ArrayList;


/**
 * Board.java
 * Author: Tyler Holland
 * The board controller. Holds hexagons, vertices(, other things).
 */
public class Board {
    private int rings;
    private int arraySize;
    private Shape[][] vertices;

    public Board(int rings)
    {
        rings *= 2;  // vertices take more rings
        this.rings = rings;
        arraySize = rings*2+1;
        vertices = new Shape[arraySize][arraySize];
        int num = 0;
        for (int q = -rings; q <= rings; q++) {
            for (int r = Math.max(-rings, -q - rings); r <= Math.min(rings, -q + rings); r++) {
                int q_index = q < 0? q + arraySize: q;
                int r_index = r < 0? r + arraySize: r;
                if (Math.abs(q-r) % 3 == 0) {  // should be a full Hexagon
                    int hx_q = q_index, hx_r = r_index;  // TODO change this? somehow? (for use in a separate hexagon array)
                    System.out.println(String.format("Hex %1$2d at %2$2d, %3$2d -> %4$2d, %5$2d", num, q, r, hx_q, hx_r)); // DEBUG
                    vertices[q_index][r_index] = new Hexagon(q, r);
                    num += 1;
                }
                else {  // otherwise a Vertex
                    // System.out.println("placing V {:2} in {:2} {:2}".format(num, q, r), sep = "")  // DEBUG
                    vertices[q_index][r_index] = new Vertex(q, r);
                }
            }
        }
        System.out.println("\n");  // DEBUG
    }



    public String toString()
    {
        String prt = "";

        for (int q = -arraySize/2; q <= arraySize/2; q++) {
            for (int r = -arraySize/2; r <= arraySize/2; r++) {
                Shape s = vertices[q<0? q+arraySize: q][r<0? r+arraySize: r];
                prt += (s == null? "   ..   ": s + " ");
            }
            prt += '\n';
        }
        return prt;
    }
}
