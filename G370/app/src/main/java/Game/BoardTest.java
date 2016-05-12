package Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test class for Game classes.
 * Author: Tyler Holland
 */
public class BoardTest {

    static class Collect
    {
        Shape[] shapes;
    }


    public void boardTestJSON()
    {
        System.out.println("Testing gson de/serialization\n");
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Shape.class, new ShapeAdapter())
                .setPrettyPrinting()
                .create();

        Shape x1 = new Hexagon(6, 3);
        ((Hexagon)x1).setResource(4);
        ((Hexagon)x1).setDie(9);
        Shape v1 = new Vertex(1, 2);
        ((Vertex)v1).setLevel(1);
        ((Vertex)v1).setOwner(2);
        Shape x2 = new Hexagon(3, 0);
        ((Hexagon)x2).setResource(2);
        ((Hexagon)x2).setDie(4);
        Shape v2 = new Vertex(2, 3);
        ((Vertex)v2).setLevel(0);
        ((Vertex)v2).setOwner(1);

        Collect miniboard = new Collect();
        miniboard.shapes = new Shape[] { x1, v1, v2, x2 };

        String json = gson.toJson(miniboard);
        System.out.println(json);
        Collect dupe = gson.fromJson(json, Collect.class);

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
//        Gson gson = new Gson();

//        Point_QR pt = new Point_QR(2, 3);
//        String pt_json = gson.toJson(pt);
//        System.out.println("Point_QR     : " + pt);
//        System.out.println("Point_QR JSON: " + pt_json);
//        System.out.println("Point_QR     : " + gson.fromJson(pt_json, Point_QR.class));

        // CANNOT instantiate a Path (in Shape) outside of actual android jvm
//        Vertex v = new Vertex(2, 3);
//        String v_json = gson.toJson(v);
//        System.out.println("Vertex     : " + v);
//        System.out.println("Vertex JSON: " + v_json);
//        System.out.println("Vertex     : " + gson.fromJson(v_json, Vertex.class));

        new BoardTest().boardTestJSON();

        System.out.println("\ntests complete");
    }



    public void boardTestFullJSON()
    {
        System.out.println("JSON Testing gson de/serialization");
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeHierarchyAdapter(Shape.class, new ShapeAdapter())
                .setPrettyPrinting()
                .create();

//        testBaseClassJSON(gson);
//        testPolymorphicJSON(gson);
//        testArrayGeneric(gson);
//        testArrayJSON(gson);

//        String b_json = gson.toJson(board, Board.class);
//        System.out.println("JSON Board JSON   :\n " + b_json);



        System.out.println("JSON Testing done\n");
    }
    public void testBaseClassJSON(Gson gson)
    {
        System.out.println("JSON testing base classes");
        Point_QR pt = new Point_QR(2, 3);
        String pt_json = gson.toJson(pt);
        System.out.println("JSON Point_QR     : " + pt);
        System.out.println("JSON Point_QR JSON: " + pt_json);
        System.out.println("JSON Point_QR     : " + gson.fromJson(pt_json, Point_QR.class));

        Vertex v = new Vertex(2, 3);
        v.setLevel(1);
        v.setOwner(2);
        v.setSelected(true);
        String v_json = gson.toJson(v);
        System.out.println("JSON Vertex       : " + v);
        System.out.println("JSON Vertex JSON  : " + v_json);
        System.out.println("JSON Vertex       : " + gson.fromJson(v_json, Vertex.class));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(v_json, Vertex.class)));

        Hexagon x = new Hexagon(6, 3);
        x.setResource(4);
        x.setDie(9);
        String x_json = gson.toJson(x);
        System.out.println("JSON Hexagon      : " + x);
        System.out.println("JSON Hexagon JSON : " + x_json);
        System.out.println("JSON Hexagon      : " + gson.fromJson(x_json, Hexagon.class));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(x_json, Hexagon.class)));

        Edge e = new Edge(new Point_QR(1, -1), new Point_QR(1, 0), 0);
        e.setOwner(1);
        String e_json = gson.toJson(e);
        System.out.println("JSON Edge         : " + e);
        System.out.println("JSON Edge JSON    : " + e_json);
        System.out.println("JSON Edge         : " + gson.fromJson(e_json, Edge.class));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(e_json, Edge.class)));
    }
    public void testPolymorphicJSON(Gson gson)
    {
        System.out.println("JSON testing polymorphism");
        Shape v = new Vertex(2, 3);
        ((Vertex)v).setLevel(1);
        ((Vertex)v).setOwner(2);
        String v_json = gson.toJson(v);
        System.out.println("JSON Vertex       : " + v);
        System.out.println("JSON Vertex JSON  : " + v_json);
        System.out.println("JSON Vertex       : " + gson.fromJson(v_json, Shape.class));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(v_json, Shape.class)));

        Shape x = new Hexagon(6, 3);
        ((Hexagon)x).setResource(4);
        ((Hexagon)x).setDie(9);
        String x_json = gson.toJson(x);
        System.out.println("JSON Hexagon      : " + x);
        System.out.println("JSON Hexagon JSON : " + x_json);
        System.out.println("JSON Hexagon      : " + gson.fromJson(x_json, Shape.class));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(x_json, Shape.class)));
    }
    public void testArrayJSON(Gson gson)
    {
        System.out.println("JSON testing arrays");
        // init shapes for array
        Shape x1 = new Hexagon(6, 3);
        ((Hexagon)x1).setResource(4);
        ((Hexagon)x1).setDie(9);
        Shape v1 = new Vertex(1, 2);
        ((Vertex)v1).setLevel(1);
        ((Vertex)v1).setOwner(2);
        Shape x2 = new Hexagon(3, 0);
        ((Hexagon)x2).setResource(2);
        ((Hexagon)x2).setDie(4);
        Shape v2 = new Vertex(2, 3);
        ((Vertex)v2).setLevel(0);
        ((Vertex)v2).setOwner(1);

        Shape test[] = new Shape[] { x1, v1, v2, x2 };

        String test_json = gson.toJson(test);
        System.out.println("JSON Array        :\n " + stringifyArray(test));
        System.out.println("JSON Array JSON   :\n " + test_json);
        System.out.println("JSON Array        :\n " + stringifyArray(gson.fromJson(test_json, Shape[].class)));
        System.out.println("JSON re-gson JSON :\n " + gson.toJson(gson.fromJson(test_json, Shape[].class)));
    }
    public void testArrayGeneric(Gson gson)
    {
        System.out.println("JSON testing generic arrays");

        String test[] = new String[] { "shape", "hexagon", "vertex", "edge" };

        String test_json = gson.toJson(test);
        System.out.println("JSON Array        : " + stringifyArray(test));
        System.out.println("JSON Array JSON   : " + test_json);
        System.out.println("JSON Array        : " + stringifyArray(gson.fromJson(test_json, String[].class)));
        System.out.println("JSON re-gson JSON : " + gson.toJson(gson.fromJson(test_json, String[].class)));
    }
    private String stringifyArray(Shape[] a)
    {
        String result = "[";
        for (Shape s: a) {
            if (s != null)
                result += s + ",";
        }
        return result.substring(0, result.length()-1) + "]";
    }
    private String stringifyArray(String[] a)
    {
        String result = "[";
        for (String s: a) {
            if (s != null)
                result += s + ",";
        }
        return result.substring(0, result.length()-1) + "]";
    }
}
