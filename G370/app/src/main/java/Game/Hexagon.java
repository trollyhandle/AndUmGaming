package Game;

import android.graphics.Path;

/**
 * Hexagon.java
 * Author: Tyler Holland
 * Represents a hexagon in the game board
 */
public class Hexagon extends Shape {

    // TO SERIALIZE
    private int resource;  // type of resource tile this is (brick vs ore vs etc.)
    private int die;  // die-roll which causes this tile to generate its resource (2-12)

    public Hexagon(int q, int r)
    {
        super(q, r);
        resource = 0;
        die = 0;
    }

    public String type() { return "Hexagon"; }

    public int getResource() { return resource; }
    public void setResource(int resource) { this.resource = resource; }
    public void setDie(int die){this.die = die; }
    public int getDie() {return die;}

    public void updatePath(int hex_size, Point_XY boardCenter)
    {
        int poly_size = (int)(hex_size/1.3);  // size of actual drawn polygon
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(0, poly_size);

        path.rewind();  // clears path, leaves internal structure intact (faster reconstructing)
        path.addCircle(shape_center.x(), shape_center.y(), poly_size/4, Path.Direction.CCW);

        path.moveTo(pt.x(), pt.y());
        for (int i = 0; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
        //path.addCircle(shape_center.x(), shape_center.y(), poly_size/5, Path.Direction.CCW);
    }


    public Point_XY getCenter (int hex_size,Point_XY boardCenter){
        return boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
    }

    public int fontSize(int hex_size){
        return (int)(hex_size/1.3);
    }

}
