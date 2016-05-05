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

    // RESOURCE PAINT_COLOR LOOKUP
    private enum RES {
        BLANK   (0xffffffff), WHEAT   (0xffFFDF00), WOOD    (0xff014421),
        ORE     (0xff8A7F80), BRICK   (0xffCB4154), SHEEP   (0xff98FF98);
        private int val; RES(int color) {val=color;}
        static int get(int i) { switch(i) {
            case 1: return WHEAT.val; case 2: return WOOD.val;
            case 3: return ORE.val; case 4: return BRICK.val;
            case 5: return SHEEP.val; } return BLANK.val; }
        static int index(RES r) { switch(r) {
            case WHEAT: return 1; case WOOD: return 2;
            case ORE: return 3; case BRICK: return 4;
            case SHEEP: return 5; } return 0; }
    }
    // PLAYER PAINT_COLOR LOOKUP
    private enum PLAYERS {
        NONE (0xffFFFFFF)/*WHITE*/, ONE (0xffFF0800)/*RED*/, TWO(0xff00FF00)/*GREEN*/,
        THREE(0xff1C1CF0)/*BLUE*/,  FOUR(0xffBF00FF)/*VIOLET*/;
        private int val; PLAYERS(int value) {val=value;}
        static int get(int i) { switch(i) {
            case 1: return ONE.val; case 2: return TWO.val;
            case 3: return THREE.val; case 4: return FOUR.val;
        } return NONE.val; }
    }

    private int rings, arraySize;
    private int[][] extra_vertices;
    private ShapeDrawable[] shapes;

    private Point_XY center;
    private int hex_size, board_size;


    private Shape[][] vertices;
    private Edge[][] edges;

    private boolean update;

    public Board(int hex_size, Point_XY center)
    {
        rings = 4;  // Two rings of full hexes, but vertices take more rings
        arraySize = rings*2+1;
        vertices = new Shape[arraySize][arraySize];
        this.center = center;
        this.hex_size = hex_size;
        shapes = new ShapeDrawable[arraySize*arraySize];
        extra_vertices = new int[12][];
        initBoard();
        fillTiles();
        update = false;
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
                    s.setBoardCenter(center);
                    s.setHexSize(hex_size);
                    s.makeDrawable();

                    int color = (q-r)%3 == 0? (RES.get(((Hexagon)s).getResource())) :
                            PLAYERS.get(((Vertex)s).getOwner());
                    shapes[i++] = new ShapeDrawable(s.getPath(), color);
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


    public boolean setOwner(int q, int r, int player)
    {
        Shape s = vertices[(q+arraySize)%arraySize][(r+arraySize)%arraySize];
        if ((q-r)%3 == 0 || ((Vertex)s).isOwned())  // is a Hexagon, or is already owned
            return false;
        if (debug) System.out.printf("BOARD setting ownership of %1$2d %2$2d to player %3$d\n", q, r, player);

        ((Vertex)s).setOwner(player);
        update = true;
        return true;
    }

    public boolean isValidHex(Point_QR hex)
    {
        int q = hex.q(), r = hex.r();
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
        return prt;
    }

// *********** PRIVATE FUNCTIONS ***********

    private void initBoard()
    {
        for (int q = -rings; q <= rings; q++) {
            for (int r = Math.max(-rings, -q - rings); r <= Math.min(rings, -q + rings); r++) {
                int q_index = (q+arraySize)%arraySize;  // adjust for negative indices
                int r_index = (r+arraySize)%arraySize;  // adjust for negative indices
                if (Math.abs(q-r) % 3 == 0) {  // a full Hexagon
                    vertices[q_index][r_index] = new Hexagon(q, r, hex_size, center);
                }
                else {  // otherwise a Vertex
                    vertices[q_index][r_index] = new Vertex(q, r, hex_size, center);
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
            System.out.printf("Extra vertex: (%1$2d,%2$2d)\n", pair[0], pair[1]);
            vertices[(pair[0]+arraySize)%arraySize][(pair[1]+arraySize)%arraySize] =
                    new Vertex(pair[0], pair[1], hex_size, center);
        }
        initEdges();
    }
    private void initEdges()
    {
        // pass for now...
    }

    private void fillTiles()
    {
        RES shuffle[] = { RES.BLANK, // one desert
                RES.WHEAT,RES.WHEAT,RES.WHEAT,RES.WHEAT,  // 4 wheat
                RES.WOOD, RES.WOOD, RES.WOOD, RES.WOOD,  // 4 wood
                RES.SHEEP, RES.SHEEP, RES.SHEEP, RES.SHEEP,  // 4 sheep
                RES.ORE, RES.ORE, RES.ORE, RES.BRICK, RES.BRICK, RES.BRICK};  // 3 ore and brick
        int shufsize = shuffle.length;  // 4+4+4+3+3+1 = 19
        Random rand = new Random();
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if ((q-r)%3==0 && s != null) {
                    int which = rand.nextInt(shufsize);
                    ((Hexagon)s).setResource(RES.index(shuffle[which]));
                    shuffle[which] = shuffle[--shufsize];
                }
            }
        }
    }

}