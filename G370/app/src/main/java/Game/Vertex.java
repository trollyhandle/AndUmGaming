package Game;

import android.graphics.Path;

import com.google.gson.annotations.Expose;

/**
 * Vertex.java
 * Author: Tyler Holland
 * Represents a vertex in the game board
 */
public class Vertex extends Shape {

    // TO SERIALIZE
    @Expose
    private int owner;  // player who has built upon this Vertex
    @Expose
    private int level;  // level of building upgrades: 0-none, 1-settlement, 2-city
    @Expose
    private String type;

    // NOT SERIALIZE
    private boolean selected;

    public Vertex(int q, int r)
    {
        super(q, r);
        type = "vertex";
        owner = level = 0;
        selected = false;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getOwner() { return owner; }
    public void setOwner(int player) { owner = player; }

    public boolean isOwned() { return owner != 0; }

    public boolean getSelected() { return selected; }
    public void setSelected(boolean select) { selected = select; }

    public void updatePath(int hex_size, Point_XY boardCenter)
    {
        int poly_size = hex_size / 4;  // size of actual drawn hexagon
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(330, poly_size);

        path.rewind();  // clears path but leaves internal structure intact (faster readding)
        if (selected)  // if selected, put a giant gaping hole
            path.addCircle(shape_center.x(), shape_center.y(), poly_size/2, Path.Direction.CCW);
        else if (level != 2)  // if not a city, add small circle
            path.addCircle(shape_center.x(), shape_center.y(), poly_size/5, Path.Direction.CCW);

        // make the hexagon shape
        path.moveTo(pt.x(), pt.y());
        for (int i = 30; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
    }

    public String getType() { return type = "vertex"; }
//    public String getType() { return "vertex"; }
    public String toString()
    {
        return getType() + ":" + coord + "|" + owner + level;
    }
}
