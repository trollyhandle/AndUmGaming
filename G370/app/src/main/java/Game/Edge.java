package Game;

import android.graphics.Path;

/**
 * Edge.java
 * Author: Tyler Holland
 * Represents an edge in the game board
 */
public class Edge {
    private Point_QR source;
    private Point_QR dest;

    protected int hex_size;
    protected Point_XY boardCenter;
    protected Path path;
    private boolean debug = false;

    private int owner;

    public Edge(Point_QR source, Point_QR dest)
    {
        this.source = source;
        this.dest = dest;
        owner = 0;
    }



    public int getSize() { return hex_size; }
    public void setHexSize(int hexSize) { this.hex_size = hexSize; }
    public Point_QR getSource() { return source; }
    public Point_QR getDestination() { return dest; }

    public int getOwner() { return owner; }

    public void setBoardCenter(Point_XY newCenter) { boardCenter = newCenter; }

    public Path getPath() { return path; }

    public void makeDrawable()
    {
        Point_XY src = boardCenter.jump_hex(source.q(), source.r(), hex_size);
        Point_XY dst = boardCenter.jump_hex(dest.q(), dest.r(), hex_size);

        path = new Path();
        path.addCircle(src.x(), src.y(), 8, Path.Direction.CCW);
        path.lineTo(dst.x(), dst.y());
        path.addCircle(dst.x(), dst.y(), 8, Path.Direction.CCW);

        path.close();
    }

    public String type() { return "Edge"; }
    public String toString()
    {
        return "" + source + " -> " + dest;
    }
    public String serialize()
    {
        return "";
    }
}
