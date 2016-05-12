package Game;

import com.google.gson.Gson;

/**
 * Test class for Game classes.
 * Author: Tyler Holland
 */
public class BoardTest {

    public void testJSON()
    {
        System.out.println("Testing gson de/serialization\n");
        Gson gson = new Gson();

        Point_QR pt = new Point_QR(2, 3);
        String pt_json = gson.toJson(pt);
        System.out.println("Point_QR     : " + pt);
        System.out.println("Point_QR JSON: " + pt_json);
        System.out.println("Point_QR     : " + gson.fromJson(pt_json, Point_QR.class));

//        Vertex v = new Vertex(2, 3);
//        String v_json = gson.toJson(v);
//        System.out.println("Vertex     : " + v);
//        System.out.println("Vertex JSON: " + v_json);
//        System.out.println("Vertex     : " + gson.fromJson(v_json, Vertex.class));




    }


    public static void main(String args[])
    {
        System.out.println("tests begin");
//        System.out.println("creating board...");
//
//        Board testBoard = new Board(125, new Point_XY(0,0));
//
//        System.out.println("printing board:\n" + testBoard);

        System.out.println("Testing gson de/serialization\n");
        Gson gson = new Gson();

        Point_QR pt = new Point_QR(2, 3);
        String pt_json = gson.toJson(pt);
        System.out.println("Point_QR     : " + pt);
        System.out.println("Point_QR JSON: " + pt_json);
        System.out.println("Point_QR     : " + gson.fromJson(pt_json, Point_QR.class));

        // CANNOT instantiate a Path (in Shape) outside of actual android jvm
//        Vertex v = new Vertex(2, 3);
//        String v_json = gson.toJson(v);
//        System.out.println("Vertex     : " + v);
//        System.out.println("Vertex JSON: " + v_json);
//        System.out.println("Vertex     : " + gson.fromJson(v_json, Vertex.class));


        System.out.println("\ntests complete");
    }

}
