package Game;

import java.util.Random;

/**
 * Board.java
 * Author: Tyler Holland
 * The board controller. Holds hexagons, vertices(, other things).
 */
public class Board {
    private boolean debug = true;

    private static final int MIN_SIZE = 80;
    private static final int MAX_SIZE = 840;

    private int rings, arraySize;
    private int[][] extra_vertices;
    private ShapeDrawable[] shapes;

    private Point_XY center;
    private int hex_size, board_size;


    private Shape[][] vertices;
    private Edge[][][] edges;

    private boolean update;

    public Board(int hex_size, Point_XY center)
    {
        rings = 4;  // Two rings of full hexes, but vertices take more rings
        arraySize = rings*2+1;
        vertices = new Shape[arraySize][arraySize];
        edges = new Edge[(arraySize)/3][arraySize][3];
        this.center = center;
        this.hex_size = hex_size;
        //                          vertices/hexes     +        edges
        shapes = new ShapeDrawable[arraySize*arraySize + ((arraySize)/3)*(arraySize)*3];
        extra_vertices = new int[12][];
        initBoard();
        fillTiles();
        update = true;
    }

    public ShapeDrawable[] getShapeDrawables() { if (update) update(); return shapes; }

    public void update() {
        if(debug)System.out.println("BOARD Updating path...");
        int i = 0;
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if (s != null) {
                    s.update(hex_size, center);
                    int color = isHex(q,r)? (Game.RESOURCES.getColor(((Hexagon)s).getResource())):
                            Game.PLAYERS.getColor(((Vertex)s).getOwner());
                    shapes[i++] = new ShapeDrawable(s.getPath(), color);
                }
            }
        }
        Edge e;
        for (int q = 0; q < (arraySize)/3; q++) {
            for (int r = 0; r < (arraySize); r++) {
                for (int k = 0; k < 3; k++) {
                    e = edges[q][r][k];
                    if (e != null) {
                        e.update(hex_size, center);
                        int color = Game.PLAYERS.getColor(e.getOwner());
                        shapes[i++] = new ShapeDrawable(e.getPath(), color);
                    }
                }
            }
        }
        update = false;
    }


    public void move(int dx, int dy)
    {
        int new_x = center.x() + dx;
        int new_y = center.y() + dy;
        center = new Point_XY(new_x, new_y);
        update = true;
    }
    public void resize(int ds)
    {
        if (hex_size != MIN_SIZE && hex_size != MAX_SIZE) {
            update = true;  // the size is gonna actually change. do update
        }
        // enforce min/max boundaries
        hex_size += ds;
        hex_size = hex_size < MIN_SIZE ? MIN_SIZE : (hex_size > MAX_SIZE ? MAX_SIZE : hex_size);

    }
    public Point_XY getCenter() { return center; }
    public void setCenter(int x, int y) { center = new Point_XY(x, y); update = true; }
    public void setCenter(Point_XY new_center) { center = new_center; update = true; }

    /**
     * Gets the polygon size of the region used in locating mouse clicks
     * @return int: size of clickable area
     */
    public int getClickableSize() { return (int)(hex_size * Math.sqrt(3)/ 3); }
    public int getSize() { return 8 * getClickableSize(); }
    public int getHexSize() { return hex_size; }
    public void setHexSize(int size)
    {
        // enforce min/max boundaries
        hex_size = size < MIN_SIZE ? MIN_SIZE : (size > MAX_SIZE ? MAX_SIZE : size);
        update = true;
    }


    public boolean buildSettlement(int q, int r, int player)
    {
        Shape s = vertices[aib(q)][aib(r)];
        if (isHex(q,r) || ((Vertex)s).isOwned()) {  // is a Hexagon, or is already owned
            if (debug) System.out.println("BOARD cannot settle there!");
            return false;
        }

        if (debug) System.out.printf("BOARD setting ownership of (%1$2d,%2$2d) to player %3$d\n", q, r, player);

        // TODO check that none of Vertex(q, r)'s neighbors are occupied

        ((Vertex)s).setOwner(player);
        ((Vertex)s).setLevel(1);
        update = true;
        return true;
    }


    public boolean buildCity(int q, int r, int player)
    {
        Shape s = vertices[aib(q)][aib(r)];
        if (isHex(q,r) || ((Vertex)s).getLevel() != 1) {  // is a Hexagon, or is not a settlement
            if (debug) System.out.println("BOARD cannot settle there!");
            return false;
        }
        if (((Vertex)s).getOwner() != player) { // settlement is not owned by building player
            if (debug) System.out.println("BOARD someone else has settled there!");
            return false;
        }
        if (debug) System.out.printf("BOARD upgrading ownership of (%1$2d,%2$2d) to player %3$d\n", q, r, player);

        ((Vertex)s).setLevel(2);  // level up to a city!
        update = true;
        return true;
    }

    private Shape getShape(Point_QR pt){
        if(!isValid(pt))
            return null;
        return vertices[aib(pt.q())][aib(pt.r())];
    }

    public boolean buildRoad(Point_QR src, Point_QR dst, int player)
    {
        Edge e = getEdge(src, dst);

        if (e == null) // no such edge
            return false;

        if(e.isOwned()) {
            if (debug) System.out.println("BOARD someone else already built a road there!");
            return false;
        }

        // TODO allow player to build a road through an unsettled vertex if they have an adjacent road
        if(getShape(src) != null && (((Vertex)getShape(src)).isOwned() || ((Vertex)getShape(dst)).isOwned()) == false) {
            if (debug) System.out.println("BOARD player does not own an vertex!");
            return false;
        }

        e.setOwner(player);
        if(debug) System.out.println("BOARD setting ownership of road " + e + " to " + player);
        update = true;
        return true;
    }

    public boolean isValid(Point_QR hex) { return isHex(hex.q(), hex.r()); }
    public boolean isValid(int q, int r)
    {
        if (Math.max(Math.max(Math.abs(q), Math.abs(r)), Math.abs((-q-r))) <= rings)
            return true;
        for (int[] pair: extra_vertices)
            if (q == pair[0] && r == pair[1])
                return true;
        return false;
    }

    public String toString()
    {
        String prt = "";
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                prt += (s == null? "   ..   ": s + " ");
            }
            prt += '\n';
        }
        prt += '\n';
        Edge e;
        int numEdges = 0;
        for (int q = 0; q < arraySize/3; q++) {
            prt += "q:" + q;
            for (int r = 0; r < arraySize; r++) {
                prt += "\tr:" + r;
                for (int i = 0; i < 3; i++) {
                    prt += "[";
                    e = edges[q][r][i];
                    prt += (e == null ? "       .   .       " : e + " ");
                    numEdges += e == null? 0: 1;
                    prt += "]";
                }
                prt += '\n';
            }
            prt += '\n';
        }
        return prt + "Edges: " + numEdges + '\n';
    }

    public String serialize()
    {
        Shape s;
        String json = "{\"board\":{";
        json += "\"vertices\":[";
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if (s != null) {
                    json += s.serialize() + ",";
                }
            }
        }
        // remove trailing comma
        json = json.substring(0, json.length()-1) + "],";
        Edge e;
        json += "\"edges\":[";
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                for (int k = 0; k < 3; k++) {
                    e = edges[q][r][k];
                    if (e != null) {
                        json += e.serialize() + ",";
                    }
                }
            }
        }
        // remove trailing comma
        json = json.substring(0, json.length()-1) + "]}}";

        return json;
    }
    public static Board deserialize(String json)
    {
        return null;
    }

// *********** PRIVATE FUNCTIONS ***********

    // array-index boundary adjuster (transforms negative coordinates)
    private int aib(int i)
    {  // array-index boundary
        return (i+arraySize) % arraySize;
    }
    private boolean isHex(int q, int r) { return isValid(q, r) && Math.abs(q-r) % 3 == 0; }
    private boolean isHex(Point_QR hex) { return isValid(hex) && Math.abs(hex.q()-hex.r()) % 3 == 0; }
    private Edge getEdge(Point_QR a, Point_QR b)
    {
        if (isHex(a) || isHex(b))
            return null;
        if (((a.q() - a.r()) + 1) % 3 == 0) {
            // a is the source
            for (Edge e: edges[aib(a.q())/3][aib(a.r())])
                if (e != null && e.getDestination().equals(b))
                    return e;
        }
        else if (((b.q() - b.r()) + 1) % 3 == 0) {
            // b is the source
            for (Edge e: edges[aib(b.q())/3][aib(b.r())])
                if (e != null && e.getDestination().equals(a))
                    return e;
        }
        return null;  // a and b are not adjacent
    }

    private boolean hasRoadOwnedBy(Point_QR vertex, int player_owner)
    {
        Shape s, centre = getShape(vertex);
        if (centre == null) return false;
        Edge e;
        for (int i = 0; i < 6; i++) {
            // if is valid point and isn't a hex (therefore a valid vertex)
            if (isValid(centre.getNeighbor(i)) && !isHex(centre.getNeighbor(i))) {
                e = getEdge(vertex, centre.getNeighbor(i));
                if (e != null && e.getOwner() == player_owner)
                    return true;
            }
        }
        return false;
    }

    private Shape getShape(Point_QR pt)
    {
        if (!isValid(pt))
            return null;
        return vertices[aib(pt.q())][aib(pt.r())];
    }

    private void initBoard()
    {
        for (int q = -rings; q <= rings; q++) {
            for (int r = Math.max(-rings, -q - rings); r <= Math.min(rings, -q + rings); r++) {
                if (isHex(q, r)) {  // a full Hexagon
                    vertices[aib(q)][aib(r)] = new Hexagon(q, r);
                }
                else {  // otherwise a Vertex
                    vertices[aib(q)][aib(r)] = new Vertex(q, r);
                }
            }
        }
        // The above algorithm misses 12 vertices on the outermost edges of the board.
        // Here they come.
        int rings = this.rings / 2, i = 0;
        int[][] more_coords = {{rings, rings+1}, {-rings, 2*rings+1}, {-rings-1, 2*rings+1}};
        for (int[] pair : more_coords) {
            extra_vertices[i++] = new int[]{ pair[0],  pair[1]};
            extra_vertices[i++] = new int[]{ pair[1],  pair[0]};
            extra_vertices[i++] = new int[]{-pair[0], -pair[1]};
            extra_vertices[i++] = new int[]{-pair[1], -pair[0]};
        }
        for (int[] pair: extra_vertices) {
            vertices[aib(pair[0])][aib(pair[1])] = new Vertex(pair[0], pair[1]);
        }
        initEdges();
    }
    private void initEdges()
    {
        Shape v;
        for (int q = -rings; q < rings+1; q++) {
            for (int r = Math.max(-rings, -q-rings); r < Math.min(rings, -q+rings)+1; r++) {
                v = vertices[aib(q)][aib(r)];
                if (((q - r) + 1) % 3 == 0 && v != null) {  // necessary for larger boards
                    if (isValid(v.getNeighbor(0))) {
                        edges[aib(q) / 3][aib(r)][0] = new Edge(new Point_QR(q, r), v.getNeighbor(0), 0);
                    }
                    if (isValid(v.getNeighbor(2))) {
                        edges[aib(q) / 3][aib(r)][1] = new Edge(new Point_QR(q, r), v.getNeighbor(2), 4);
                    }
                    if (isValid(v.getNeighbor(4))) {
                        edges[aib(q) / 3][aib(r)][2] = new Edge(new Point_QR(q, r), v.getNeighbor(4), 2);
                    }
                }
            }
        }
        for (int[] pair: extra_vertices) {
            int q = pair[0], r = pair[1];
            if (((q - r) + 1) % 3 == 0) {
                v = vertices[aib(q)][aib(r)];
                if (v != null) {
                    if (isValid(v.getNeighbor(0))) {
                        edges[aib(q) / 3][aib(r)][0] = new Edge(new Point_QR(q, r), v.getNeighbor(0), 0);
                    }
                    if (isValid(v.getNeighbor(2))) {
                        edges[aib(q) / 3][aib(r)][1] = new Edge(new Point_QR(q, r), v.getNeighbor(2), 4);
                    }
                    if (isValid(v.getNeighbor(4))) {
                        edges[aib(q) / 3][aib(r)][2] = new Edge(new Point_QR(q, r), v.getNeighbor(4), 2);
                    }
                }
            }
        }
    }

    private void fillTiles()
    {
        Game.RESOURCES shuffle[] = { Game.RESOURCES.BLANK, // one desert
                Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT,  // 4 wheat
                Game.RESOURCES.WOOD, Game.RESOURCES.WOOD, Game.RESOURCES.WOOD, Game.RESOURCES.WOOD,  // 4 wood
                Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP,  // 4 sheep
                Game.RESOURCES.ORE, Game.RESOURCES.ORE, Game.RESOURCES.ORE,
                Game.RESOURCES.BRICK, Game.RESOURCES.BRICK, Game.RESOURCES.BRICK};  // 3 ore and brick
        int shufsize = shuffle.length;  // 4+4+4+3+3+1 = 19
        Random rand = new Random();
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if (isHex(q, r) && s != null) {
                    int which = rand.nextInt(shufsize);
                    ((Hexagon)s).setResource(Game.RESOURCES.index(shuffle[which]));
                    shuffle[which] = shuffle[--shufsize];
                }
            }
        }
    }
}