package Game;

import android.graphics.Path;

/**
 * Hexagon.java
 * Author: Tyler Holland
 * Represents a hexagon in the game board
 */
public class Hexagon extends Shape {
    private int resource;

    public Hexagon(int q, int r)
    {
        super(q, r);
        resource = 0;
    }

    public int getResource() { return resource; }
    public void setResource(int resource) { this.resource = resource; }

    public String type() { return "Hexagon"; }

    public void update(int hex_size, Point_XY boardCenter)
    {
        int poly_size = (int)(hex_size/1.3);
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(0, poly_size);

        path.rewind();
        path.addCircle(shape_center.x(), shape_center.y(), 8, Path.Direction.CCW);
        path.moveTo(pt.x(), pt.y());

        for (int i = 0; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
    }

    public String serialize()
    {
        String json = "{\"shape\":{";
        json += "\"type\":\"" + type() + "\",";
        json += "\"coord\":" + coord.serialize() + ",";
        json += "\"resource\":" + resource + "}}";
        return json;
    }

}
