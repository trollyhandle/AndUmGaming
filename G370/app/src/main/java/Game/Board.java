package Game;

import android.graphics.Path;

/**
 * Board.java
 * Author: Tyler Holland
 * The board controller. Holds hexagons, vertices(, other things).
 */
public class Board {
    private int rings, arraySize;
    private Shape[][] vertices;

    private Point_XY center;
    private int hex_size;

    private Path display;

    public Board()
    {
        rings = 4;  // Two rings of full hexes, but vertices take more rings
        arraySize = rings*2+1;
        vertices = new Shape[arraySize][arraySize];
        center = new Point_XY(0, 0);
        hex_size = 1;
        display = new Path();
        initBoard();
    }

    public Path getPath() { return display; }
    public void update()
    {
        System.out.println("BOARD Updating path...");
        vertices[0][0].makeDrawable();
        display.rewind();
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                if (vertices[q][r] != null) {
                    // update shape's board_center
                    // update shape's hexagon size
                    vertices[q][r].makeDrawable();
                    display.addPath(vertices[q][r].getPath());
                }
            }
        }
    }


    public void move(int dx, int dy)
    {
        int new_x = center.x() + dx;
        int new_y = center.y() + dy;
        center = new Point_XY(new_x, new_y);
    }
    public void resize(int ds) { hex_size += ds; }
    public Point_XY getCenter() { return center; }
    public void setCenter(int x, int y)
    {
        center = new Point_XY(x, y);
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if (s != null) s.setBoardCenter(center);
            }
        }
    }
    public int getHexSize() { return hex_size; }
    public void setHexSize(int size) { hex_size = size; }

    public String toString()
    {
        String prt = "";

        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                prt += (s == null? "   ..   ": s + " ");
            }
            prt += '\n';
        }
        return prt;
    }

// *********** PRIVATE FUNCTIONS ***********

    private void initBoard()
    {
        int num = 0;  // for DEBUG
        for (int q = -rings; q <= rings; q++) {
            for (int r = Math.max(-rings, -q - rings); r <= Math.min(rings, -q + rings); r++) {
                int q_index = (q+arraySize)%arraySize;  // adjust for negative indices
                int r_index = (r+arraySize)%arraySize;  // adjust for negative indices
                if (Math.abs(q-r) % 3 == 0) {  // should be a full Hexagon
//                    int hx_q = q_index, hx_r = r_index;  // TODO change this? somehow? (for use in a separate hexagon array)
//                    System.out.println(String.format("Hex %1$2d at %2$2d, %3$2d -> %4$2d, %5$2d", num, q, r, hx_q, hx_r)); // DEBUG
                    vertices[q_index][r_index] = new Hexagon(q, r);
                    num += 1;
                }
                else {  // otherwise a Vertex
                    vertices[q_index][r_index] = new Vertex(q, r);
                }
            }
        }
        // The above algorithm misses 12 vertices on the outermost edges of the board.
        // Here they come.
        int[][] extra_vertices = {{rings/2, rings/2+1}, {-(rings/2), 2*(rings/2)+1}, {-(rings/2)-1, 2*(rings/2)+1}};
        for (int[] pair : extra_vertices) {
            vertices[( pair[0]+arraySize)%arraySize][( pair[1]+arraySize)%arraySize] =  // normal
                    new Vertex( pair[0], pair[1]);
            vertices[( pair[1]+arraySize)%arraySize][( pair[0]+arraySize)%arraySize] =  // reversed
                    new Vertex( pair[1], pair[0]);
            vertices[(-pair[0]+arraySize)%arraySize][(-pair[1]+arraySize)%arraySize] =  // negative
                    new Vertex(-pair[0], -pair[1]);
            vertices[(-pair[1]+arraySize)%arraySize][(-pair[0]+arraySize)%arraySize] =  // negative reversed
                    new Vertex(-pair[1], -pair[0]);
        }
//        System.out.println("\n");  // DEBUG
    }

}
