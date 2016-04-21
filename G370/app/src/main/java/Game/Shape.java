package Game;

import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public abstract class Shape {

    protected Point_QR coord;
    protected Point_XY boardCenter;
    protected int hex_size;
    protected int poly_size;

    private PathShape polygon;
    private ShapeDrawable display;

    private static int[][] directions = {
            { 1, 0}, { 1, -1}, {0, -1},
            {-1, 0}, {-1,  1}, {0,  1}  };

    
    public Shape(int q, int r)
    {
        coord = new Point_QR(q, r);
        boardCenter = new Point_XY(0, 0);
        hex_size = 1;
    }


    public Point_QR getNeighbor(int dir)
    {
        int neighbor_q = coord.q() + directions[dir][0];
        int neighbor_r = coord.r() + directions[dir][1];
        return new Point_QR(neighbor_q, neighbor_r);
    }

    public int getSize() { return hex_size; }
    public void setSize(int hexSize) { this.hex_size = hexSize; }
    
    public void setBoardCenter(Point_XY newCenter) { boardCenter = newCenter; }

    public ShapeDrawable getDrawable()
    {
        System.out.println("SHAPE Getting drawable..");
//        display = new ShapeDrawable(makeDrawable());
        display = new ShapeDrawable(new OvalShape());
        display.getPaint().setColor(0xffffffff);
        System.out.println("SHAPE Setting color");
        return display;
    }

    private PathShape makeDrawable()
    {
        System.out.println("SHAPE Making path...");
        System.out.println("SHAPE polygon size: " + poly_size + " (" + type() + ")");
        Point_XY shape_center = boardCenter;//.jump_hex(coord.q(), coord.r(), hex_size);  // TODO
        Point_XY pt = shape_center.jump_linear(0, poly_size);
        System.out.println("SHAPE Center at " + shape_center);
        System.out.println("SHAPE jump to" + pt);

        Path path = new Path();
        path.addCircle(shape_center.x(), shape_center.y(), 40, Path.Direction.CCW);
//        path.moveTo(pt.x(), pt.y());
//
//        for (int i = 30; i <= 360; i += 60) {
//            pt = shape_center.jump_linear(i, poly_size);
//            System.out.println("SHAPE jump to" + pt);
//            path.lineTo(pt.x(), pt.y());
//        }
//        path.close();
        System.out.println("SHAPE Path created");
        return new PathShape(path, poly_size, poly_size);
//        return new OvalShape();
    }
//  def place():
//    
//    self.center = hex_to_pixel(grid_center, self.size, self.q, self.r)
//    # print(str(self) + ", distance = {:3}".format(distance(grid_center, self.center)))  # DEBUG
//    self.poly = Polygon(jump_linear(self.center, 30, self.vertex_size),  // vertex_size
//                        jump_linear(self.center, 90, self.vertex_size),
//                        jump_linear(self.center, 150, self.vertex_size),
//                        jump_linear(self.center, 210, self.vertex_size),
//                        jump_linear(self.center, 270, self.vertex_size),
//                        jump_linear(self.center, 330, self.vertex_size))
//    self.cir_center = Circle(self.center, 3)
//    self.txt = Text(self.center, self.text)
//    self.txt.setSize(int(max(min(self.vertex_size//2, 32), 5)))
//
    
    
    public abstract String type();
    public String toString()
    {
//        String str = this.type();
//        str += "(" + coord + "), hex_size " + hex_size;
//        return str;
        return "" + coord;
    }
    
}
