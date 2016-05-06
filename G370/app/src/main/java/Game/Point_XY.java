package Game;

/**
 * Point_XY.java
 * Author: Tyler Holland
 * Represents a coordinate point in the graphic space.
 */
public class Point_XY {

    private int _x, _y;

    public Point_XY(int x, int y)
    {
        _x = x;
        _y = y;
    }
    public Point_XY(double x, double y)
    {
        _x = (int)x;
        _y = (int)y;
    }

    public int x() { return _x; }
    public int y() { return _y; }

    public int distance(Point_XY other)
    {
        return (int)Math.sqrt(Math.pow(_x-other.x(), 2) + Math.pow(_y-other.y(),2));
    }

    public Point_XY jump_linear(int degrees, int distance)
    {
        int new_x = _x + (int)(distance * Math.cos(Math.toRadians(degrees)));
        int new_y = _y + (int)(distance * Math.sin(Math.toRadians(degrees)));
        return new Point_XY(new_x, new_y);
    }

    public Point_XY jump_hex(int q, int r, int hex_size)
    {
        int qr_x = (r * hex_size) + (q * hex_size / 2);   // q * sin(30)
        int qr_y = (int)(q * hex_size * Math.sqrt(3)/2);  // q * cos(30)
        return new Point_XY(_x + qr_x, _y + qr_y);
    }


    public Point_QR toHex(Point_XY center, int size)
    {
        int cx = _x - center.x();
        int cy = _y - center.y();

        double _q = (cy * 2./3) / size;
        double _r = (cx * Math.sqrt(3)/3 - cy / 3.) / size;

        return hex_round(_q, _r);
    }
    private Point_QR hex_round(double q, double r)
    {
        // cube_x, cube_y, cube_z = hex_to_cube(q, r)
        double x = r, z = q, y = -x-z;  // convert to cubic coordinates

        // return cube_to_hex(cube_round(cube_x, cube_y, cube_z))
        return cube_round(x, y, z);  // round it to nearest hex

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
        return String.format("(%1$4d,%2$4d)", _x, _y);

    }
    public String serialize()
    {
        String json = "\"pointxy\":";
        json += "{\"x\":" + _x + ",\"y\":" + _y + "}}";
        return json;
    }
    public static Point_XY deserialize(String json)
    {
        return null;
    }
    public static String point_format(int x, int y) {
        return String.format("(%1$4d,%2$4d)", x, y);
    }
}
