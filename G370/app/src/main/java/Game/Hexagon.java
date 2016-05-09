package Game;

import android.graphics.Path;

/**
 * Hexagon.java
 * Author: Tyler Holland
 * Represents a hexagon in the game board
 */
public class Hexagon extends Shape {
    private int resource;
    private int die;

    public Hexagon(int q, int r)
    {
        super(q, r);
        resource = 0;
        die = 0;
    }

    public int getResource() { return resource; }
    public void setResource(int resource) { this.resource = resource; }
    public void setDie(int die){this.die = die; }
    public int getDie() {return die;}

    public String type() { return "Hexagon"; }

    public void update(int hex_size, Point_XY boardCenter)
    {
        int poly_size = (int)(hex_size/1.3);
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(0, poly_size);

        path.rewind();
        path.addCircle(shape_center.x(), shape_center.y(), poly_size/4, Path.Direction.CCW);
        path.moveTo(pt.x(), pt.y());

        for (int i = 0; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
        //path.addCircle(shape_center.x(), shape_center.y(), poly_size/5, Path.Direction.CCW);

    }

    public String serialize()
    {
        String json = "{\"shape\":{";
        json += "\"type\":\"" + type() + "\",";
        json += "\"coord\":" + coord.serialize() + ",";
        json += "\"resource\":" + resource + "}}";
        return json;
    }

    public Point_XY getCenter (int hex_size,Point_XY boardCenter){
        return boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
    }

    public int fontSize(int hex_size){
        return (int)(hex_size/1.3);
    }

}
