package Game;

import android.graphics.Path;

/**
 * Edge.java
 * Author: Tyler Holland
 * Represents an edge in the game board
 */
public class Edge {

    // TO SERIALIZE
    private Point_QR source;
    private Point_QR destination;
    private int direction;
    private int owner;

    // NOT SERIALIZE
    private Path path;

    public Edge(Point_QR source, Point_QR dest, int dir)
    {
        this.source = source;
        this.destination = dest;
        direction = dir;
        owner = 0;
        path = new Path();
    }

    public String type() { return "Edge"; }

    public Point_QR getSource() { return source; }
    public Point_QR getDestination() { return destination; }

    public boolean isOwned() { return owner != 0; }
    public int getOwner() { return owner; }
    public void setOwner(int player) { owner = player; }

    public Path getPath() { return path; }
    public void update(int hex_size, Point_XY boardCenter)
    {
        Point_XY src_pt = boardCenter.jump_hex(source.q(), source.r(), hex_size);
        Point_XY dst_pt = boardCenter.jump_hex(destination.q(), destination.r(), hex_size);

        // calculate the four endpoints of the rectangle this edge will become
        int angle = 60 * direction, offset = 15, vertex_size = hex_size/4;
        Point_XY src_a = src_pt.jump_linear(angle+offset, vertex_size);
        Point_XY src_b = src_pt.jump_linear(angle-offset, vertex_size);
        Point_XY dst_a = dst_pt.jump_linear(angle-offset, -vertex_size);
        Point_XY dst_b = dst_pt.jump_linear(angle+offset, -vertex_size);

        path.rewind();
        path.moveTo(src_a.x(), src_a.y());
        path.lineTo(dst_a.x(), dst_a.y());
        path.lineTo(dst_b.x(), dst_b.y());
        path.lineTo(src_b.x(), src_b.y());
        path.lineTo(src_a.x(), src_a.y());
        path.close();
    }

    public boolean isEdge(Point_QR a, Point_QR b)
    {
        if (a.equals(source))
            return b.equals(destination);
        else if (b.equals(source))
            return a.equals(destination);
        return false;
    }

    public String toString()
    {
        return "" + source + " -> " + destination;
    }
}
