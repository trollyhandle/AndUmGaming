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
    private int direction;
    private Path path;

    private int owner;

    public Edge(Point_QR source, Point_QR dest, int dir)
    {
        this.source = source;
        this.dest = dest;
        direction = dir;
        owner = 0;
    }

    public Point_QR getSource() { return source; }
    public Point_QR getDestination() { return dest; }

    public int getOwner() { return owner; }

    public Path getPath() { return path; }
    public void getDrawable(int hex_size, Point_XY boardCenter)
    {
        Point_XY src_pt = boardCenter.jump_hex(source.q(), source.r(), hex_size);
        Point_XY dst_pt = boardCenter.jump_hex(dest.q(), dest.r(), hex_size);

        int angle = 60 * direction, offset = 15, vertex_size = hex_size/4;
        Point_XY src_a = src_pt.jump_linear(angle+offset, vertex_size);
        Point_XY src_b = src_pt.jump_linear(angle-offset, vertex_size);
        Point_XY dst_a = dst_pt.jump_linear(angle-offset, -vertex_size);
        Point_XY dst_b = dst_pt.jump_linear(angle+offset, -vertex_size);

        path = new Path();
        path.moveTo(src_a.x(), src_a.y());
        path.lineTo(dst_a.x(), dst_a.y());
        path.lineTo(dst_b.x(), dst_b.y());
        path.lineTo(src_b.x(), src_b.y());
        path.lineTo(src_a.x(), src_a.y());
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
