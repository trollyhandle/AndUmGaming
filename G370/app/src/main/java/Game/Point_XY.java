package Game;

/**
 * Point_XY.java
 * Author: Tyler Holland
 * Represents a coordinate point in the graphic space.
 */
public class Point_XY {

    private int x ,y;

    public Point_XY(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int x() { return x; }
    public int y() { return y; }

    public Point_XY jump_linear(int degrees, int distance)
    {
        int new_x = x + (int)(distance * Math.cos(Math.toRadians(degrees)));
        int new_y = y + (int)(distance * Math.sin(Math.toRadians(degrees)));
        return new Point_XY(new_x, new_y);
    }

    public Point_XY jump_hex(int q, int r, int hex_size)
    {
        int qr_x = (r * hex_size) + (q * hex_size / 2);  // q * sin(30)
        int qr_y = (int)(q * hex_size * Math.sqrt(3)/2);  // q * cos(30)
        return new Point_XY(x + qr_x, y + qr_y);
    }


    public Point_QR toHex(Point_XY center, int size)
    {
        int cx = x - center.x();
        int cy = y - center.y();

        double _q = (y * 2./3) / size;
        double _r = (x * Math.sqrt(3)/3 - y / 3.) / size;

        return hex_round(_q, _r);
    }
    private Point_QR hex_round(double q, double r)
    {
        // cube_x, cube_y, cube_z = hex_to_cube(q, r)
        double _x = r,_z = q, _y = -_x-_z;  // convert to cubic coordinates

        // return cube_to_hex(cube_round(cube_x, cube_y, cube_z))
        return cube_round(_x, _y, _z);  // round it to nearest hex

    }
    private Point_QR cube_round(double x, double y, double z)
    {
        int rx = (int)Math.round(x);
        int ry = (int)Math.round(y);
        int rz = (int)Math.round(z);

        double x_diff = Math.abs(rx - x);
        double y_diff = Math.abs(ry - y);
        double z_diff = Math.abs(rz - z);

        if (x_diff > y_diff && x_diff > z_diff) {
            rx = -ry-rz;
        }
        else if (y_diff > z_diff) {
            ry = -rx-rz;
        }
        else {
            rz = -rx-ry;
        }
        int r = rx;
        int q = rz;
        return new Point_QR(q, r);
    }

    public String toString()
    {
        return String.format("(%1$2d,%2$2d)", x, y);

    }
}
