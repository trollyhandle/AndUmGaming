package Game;

import com.google.gson.annotations.Expose;

import Interface.ToastListener;

import java.util.Random;

/**
 * Board.java
 * Author: Tyler Holland
 * The board controller. Holds hexagons, vertices(, other things).
 */
public class Board {

    private boolean debug = false;
    private boolean DEBUG = false;  // for serious debugging time

    private static final int MIN_SIZE = 80;
    private static final int MAX_SIZE = 940;

    private int rings, arraySize;
    private int[][] extra_vertices;
    private ShapeDrawable[] shapes;

    private Point_XY center;
    private int hex_size;

    // TO SERIALIZE
    @Expose
    private Shape[][] vertices;
    @Expose
    private Edge[][][] edges;

    private boolean update;

    private ToastListener listener;


    public boolean getUpdate() {
        return update;
    }
    public Board()
    {
        rings = 4;  // Two rings of full hexes, but vertices take more rings
        arraySize = rings*2+1;
        vertices = new Shape[arraySize][arraySize];
        edges = new Edge[(arraySize)/3][arraySize][3];
        //                          vertices/hexes     +        edges
        shapes = new ShapeDrawable[arraySize*arraySize + ((arraySize)/3)*(arraySize)*3];
        extra_vertices = new int[12][];
        initBoard();
        fillTiles();
        update = true;
        // init with some default values
        init(40, new Point_XY(0, 0));
    }
    public void init(int hex_size, Point_XY center)
    {
        this.hex_size = hex_size;
        this.center = center;
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
                    s.updatePath(hex_size, center);

                    if (isHex(q,r)){
                        int color = (Game.RESOURCES.getColor(((Hexagon)s).getResource()));
                        int txt_size = ((Hexagon)s).fontSize(hex_size);
                        String Die = "" + ((Hexagon)s).getDie();
                        shapes[i++] = new ShapeDrawable(s.getPath(),color, Die,((Hexagon)s).getCenter(hex_size,center), txt_size);
                    }
                    else {
                        int color = Game.PLAYERS.getColor(((Vertex)s).getOwner());
                        shapes[i++] = new ShapeDrawable(s.getPath(), color);

                    }
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


    /**
     * Places a settlement at (q, r) owned by player.
     * WARNING: Use buildSettlement() instead. placeSettlement() employs reduced validity checking.
     * This should only be used at beginning of a new game.
     * @param q q-coordinate of build request site
     * @param r r-coordinate of build request site
     * @param player id of player requesting build
     * @return true if settlement successfully built
     */
    public boolean placeSettlement(int q, int r, int player)
    {
        Shape stl = getShape(q, r);
        if (stl == null || isHex(q,r) || ((Vertex)stl).isOwned()) {  // is a Hexagon, or is already owned
            if (debug) System.out.println("BOARD cannot settle there!");
            if (listener != null) {
                listener.ToastMessage("You cannot settle there!");
            }
            return false;
        }
        //this checks the neighbors to make sure no other settlement is within 1 tile.
        for (int i = 0; i < 6; i++){
            // if the next Q,R is invalid or a hex, just ignore!
            if (!isValid(stl.getNeighbor(i)) || isHex(stl.getNeighbor(i)))
                continue;
            // we now cast the next QR onto a vertex, if it is null ignore, otherwise see if it's owned.
            Shape s = getShape(stl.getNeighbor(i));
            if (s != null && ((Vertex)s).isOwned()) {
                if (debug) System.out.println("BOARD too close to another settlement!");
                if (listener != null) {
                    listener.ToastMessage("You cannot settle too close to another settlement!");
                }
                return false;
            }
        }
        if (debug) System.out.printf("BOARD setting ownership of (%1$2d,%2$2d) to player %3$d\n", q, r, player);

        ((Vertex)stl).setOwner(player);
        ((Vertex)stl).setLevel(1);
        update = true;
        return true;
    }


    public boolean buildSettlement(int q, int r, int player)
    {
        Shape s = getShape(q, r);
        if (s == null || isHex(q,r) || ((Vertex)s).isOwned()) {  // is a Hexagon, or is already owned
            if (debug) System.out.println("BOARD cannot settle there!");
            if (listener != null) {
                listener.ToastMessage("You cannot settle there!");
            }
            return false;
        }
        //this checks the neighbors to make sure no other settlement is within 1 tile.
        for (int i = 0; i < 6; i++){
            // if the next Q,R is invalid or a hex, just ignore!
            if (!isValid(s.getNeighbor(i)) || isHex(s.getNeighbor(i)))
                continue;
            // we now cast the next QR onto a vertex, if it is null ignore, otherwise see if it's owned.
            Shape shape = getShape(s.getNeighbor(i));
            if (shape != null && ((Vertex)shape).isOwned()){
                if (debug) System.out.println("BOARD too close to another settlement!");
                if (listener != null) {
                    listener.ToastMessage("You cannot settle too close to another settlement!");
                }
                return false;
            }
        }
        if (!hasRoadOwnedBy(new Point_QR(q, r), player)) {
            if (debug) System.out.println("BOARD no connection!");
            if (listener != null) {
                listener.ToastMessage("You do not have any connecting roads to this location!");
            }
            return false;
        }
        if (debug) System.out.printf("BOARD setting ownership of (%1$2d,%2$2d) to player %3$d\n", q, r, player);

        ((Vertex)s).setOwner(player);
        ((Vertex)s).setLevel(1);
        update = true;
        return true;
    }
    public void selectSettlement(Point_QR hex) { selectSettlement(hex.q(), hex.r()); }
    public void selectSettlement(int q, int r)
    {
        if (isValid(q, r) && !isHex(q, r)) {
            ((Vertex)vertices[aib(q)][aib(r)]).setSelected(true);
            update = true;
        }
    }
    public void deselectSettlement(Point_QR hex) { deselectSettlement(hex.q(), hex.r()); }
    public void deselectSettlement(int q, int r)
    {
        if (isValid(q, r) && !isHex(q, r)) {
            ((Vertex)vertices[aib(q)][aib(r)]).setSelected(false);
            update = true;
        }
    }

    public boolean buildCity(int q, int r, int player)
    {
        Shape s = getShape(q, r);
        if (s == null || isHex(q,r) || ((Vertex)s).getLevel() != 1) {  // is a Hexagon, or is not a settlement
            if (debug) System.out.println("BOARD cannot settle there!");
            if (listener != null) {
                listener.ToastMessage("You can only build a city on a settlement you posses!");
            }
            return false;
        }
        if (((Vertex)s).getOwner() != player) { // settlement is not owned by building player
            if (debug) System.out.println("BOARD someone else has settled there!");
            if (listener != null) {
                listener.ToastMessage("Someone else has settled there!");
            }
            return false;
        }
        if (debug) System.out.printf("BOARD upgrading ownership of (%1$2d,%2$2d) to player %3$d\n", q, r, player);

        ((Vertex)s).setLevel(2);  // level up to a city!
        update = true;
        return true;
    }

    public boolean buildRoad(Point_QR src, Point_QR dst, int player) {
        Edge e = getEdge(src, dst);
        if (e == null) // no such edge
            return false;
        if (e.isOwned()) {
            if (debug) System.out.println("BOARD someone else already built a road there!");
            if (listener != null) {
                listener.ToastMessage("A road has already been built there!");
            }
            return false;
        }

        Shape s = getShape(src), d = getShape(dst);
        // either vertex is (not null and owned by player)
        boolean hasSettlement = ((s != null && ((Vertex) s).getOwner() == player)
                || (d != null && ((Vertex) d).getOwner() == player));
        // no settlement, and no road reaches source, and no road reaches destination
        if (!hasSettlement && !hasRoadOwnedBy(src, player) && !hasRoadOwnedBy(dst, player)) {
            if (debug) System.out.println("BOARD player does not own a settlement or road nearby!");

            //listener stuff
                if (listener != null) {
                    listener.ToastMessage("You do not own an adjacent settlement, or road!");
                }

            return false;
        }
        e.setOwner(player);
        if(debug) System.out.println("BOARD setting ownership of road " + e + " to " + player);
        update = true;
        return true;
    }

    public int[] getGeneratedResForPlayer(int player, int die)
    {
        int[] newres = {0, 0, 0, 0, 0};
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {  // through all vertices
                Shape s = vertices[aib(q)][aib(r)];
                if (s != null && !isHex(q, r)) {  // isnt null and isnt a hex
                    Vertex v = (Vertex)s;
                    if (v.getOwner() == player) {  // this vertex is owned by the player
                        for (int k = 0; k < 6; k++) {  // check neighboring hexes
                            if (isHex(q, r)) {
                                Hexagon hx = (Hexagon) getShape(v.getNeighbor(k));
                                if (hx != null && hx.getDie() == die) {
                                    newres[hx.getResource()] += v.getLevel();  // cites generate 2 res
                                }
                            }
                        }
                    }
                }
            }
        }
        return newres;
    }
    public int[] getGeneratedResByHexForPlayer(int player, int die)
    {
        int[] newres = {0, 0, 0, 0, 0, 0};
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {  // through all vertices
                Shape s = vertices[aib(q)][aib(r)];
                if (s != null && isHex(q, r) && ((Hexagon)s).getDie() == die) {
                    for (int k = 0; k < 6; k++) {
                        Shape n = getShape(s.getNeighbor(k));
                        if (n != null && !isHex(s.getNeighbor(k))) {
                            if (((Vertex)n).getOwner() == player) {
                                newres[((Hexagon) s).getResource()] += ((Vertex) n).getLevel();
                            }
                        }
                    }
                }
            }
        }
        return newres;
    }

    public int countVictoryPoints(int player)
    {
        if(player==0) //just to be sure
            return 0;
        int points = 0;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                Shape s = vertices[aib(q)][aib(r)];
                if (s != null && !isHex(q, r)) {
                    Vertex v = (Vertex)s;
                    if (v.getOwner() == player)
                        points += v.getLevel();
                    //if(debug) System.out.println(v.getLevel() + " points from " + v.getCoord());
                }
                //if(debug) System.out.println("Examined a vertex from" +q + " "+ r);
            }
        }
        return points;
    }

    public void setListener(ToastListener listener) {
        this.listener = listener;
    }

    public boolean isValid(Point_QR hex) { return isValid(hex.q(), hex.r()); }
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
                prt += (s == null? "      .    .      ": s + " ");
            }
            prt += '\n';
        }
        prt += '\n';
        Edge e;
        int numEdges = 0;
        for (int q = 0; q < arraySize/3; q++) {
            prt += "q:" + q;
            for (int r = 0; r < arraySize; r++) {
                if (r != 0) prt += "\t";
                prt += "r:" + r;
                for (int i = 0; i < 3; i++) {
                    prt += "[";
                    e = edges[q][r][i];
                    prt += (e == null ? "       .    .       " : e + " ");
                    numEdges += e == null? 0: 1;
                    prt += "]";
                }
                prt += '\n';
            }
            prt += '\n';
        }
        return prt + "Edges: " + numEdges + '\n';
    }




// *********** PRIVATE FUNCTIONS ***********

    // array-index boundary adjuster (transforms negative coordinates)
    private int aib(int i)
    {  // array-index boundary
        return (i+arraySize) % arraySize;
    }
    private boolean isHex(int q, int r) { return Math.abs(q-r) % 3 == 0; }
    private boolean isHex(Point_QR hex) { return Math.abs(hex.q()-hex.r()) % 3 == 0; }
    private Edge getEdge(Point_QR a, Point_QR b)
    {
        // if either is invalid or a hex, there's no edge
        if (!isValid(a) || !isValid(b) || isHex(a) || isHex(b))
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
        Shape s, src = getShape(vertex);
        if (src == null) return false;
        for (int i = 0; i < 6; i++) {
            Edge e = getEdge(vertex, src.getNeighbor(i));
            if (e != null && e.getOwner() == player_owner)
                return true;
        }
        return false;
    }

    private Shape getShape(Point_QR pt){
        if(!isValid(pt))
            return null;
        return vertices[aib(pt.q())][aib(pt.r())];
    }
    private Shape getShape(int q, int r){
        if(!isValid(q, r))
            return null;
        return vertices[aib(q)][aib(r)];
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
        int edgesMade = 0;
        if(DEBUG) System.out.println("EDGEINIT Making edges...");
        for (int q = -rings; q < rings+1; q++) {
            for (int r = Math.max(-rings, -q-rings); r < Math.min(rings, -q+rings)+1; r++) {
                v = vertices[aib(q)][aib(r)];
                if(DEBUG) System.out.println("EDGEINIT selected a vertex");
                if (((q - r) + 1) % 3 == 0 && v != null) {  // necessary for larger boards
                    if (isValid(v.getNeighbor(0))) {
                        edges[aib(q) / 3][aib(r)][0] = new Edge(new Point_QR(q, r), v.getNeighbor(0), 0); edgesMade++;
                    }
                    if (isValid(v.getNeighbor(2))) {
                        edges[aib(q) / 3][aib(r)][1] = new Edge(new Point_QR(q, r), v.getNeighbor(2), 4); edgesMade++;
                    }
                    if (isValid(v.getNeighbor(4))) {
                        edges[aib(q) / 3][aib(r)][2] = new Edge(new Point_QR(q, r), v.getNeighbor(4), 2); edgesMade++;
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
                        edges[aib(q) / 3][aib(r)][0] = new Edge(new Point_QR(q, r), v.getNeighbor(0), 0); edgesMade++;
                    }
                    if (isValid(v.getNeighbor(2))) {
                        edges[aib(q) / 3][aib(r)][1] = new Edge(new Point_QR(q, r), v.getNeighbor(2), 4); edgesMade++;
                    }
                    if (isValid(v.getNeighbor(4))) {
                        edges[aib(q) / 3][aib(r)][2] = new Edge(new Point_QR(q, r), v.getNeighbor(4), 2); edgesMade++;
                    }
                }
            }
        }
        if(DEBUG) System.out.println("EDGEINIT Edges made: " + edgesMade);
    }

    private void fillTiles()
    {
        Game.RESOURCES shuffle[] = { Game.RESOURCES.BLANK, // one desert
                Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT, Game.RESOURCES.WHEAT,  // 4 wheat
                Game.RESOURCES.WOOD, Game.RESOURCES.WOOD, Game.RESOURCES.WOOD, Game.RESOURCES.WOOD,  // 4 wood
                Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP, Game.RESOURCES.SHEEP,  // 4 sheep
                Game.RESOURCES.ORE, Game.RESOURCES.ORE, Game.RESOURCES.ORE,
                Game.RESOURCES.BRICK, Game.RESOURCES.BRICK, Game.RESOURCES.BRICK};  // 3 ore and brick
        int dies[] = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        int diesize = dies.length, shufsize = shuffle.length;  // 4+4+4+3+3+1 = 19
        Random rand = new Random();
        Shape s;
        for (int q = 0; q < arraySize; q++) {
            for (int r = 0; r < arraySize; r++) {
                s = vertices[q][r];
                if (isHex(q, r) && s != null) {
                    int which = rand.nextInt(shufsize);
                    ((Hexagon)s).setResource(Game.RESOURCES.index(shuffle[which]));
                    shuffle[which] = shuffle[--shufsize];

                    if (((Hexagon)s).getResource() == 0) // desert
                        ((Hexagon)s).setDie(0);
                    else {
                        int whichdie = rand.nextInt(diesize);
                        ((Hexagon)s).setDie(dies[whichdie]);
                        dies[whichdie] = dies[--diesize];
                    }
                }
            }
        }
        // swap desert into middle hex

    }


}
