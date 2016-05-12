package Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import GsonRuntimeAdapter.RuntimeTypeAdapterFactory;

/**
 * Test class for Game classes.
 * Author: Tyler Holland
 */
public class BoardTest {

//    static class ShapeAdapter implements JsonSerializer<Shape>, JsonDeserializer<Shape> {
//
//        @Override public JsonElement serialize(Shape shape, Type typeOfSrc,
//                                               JsonSerializationContext context) {
//            JsonObject result = new JsonObject();
//            result.add("coord", context.serialize(shape.coord, Point_QR.class));
//            result.add("type", context.serialize(shape.type(), String.class));
//            if (shape instanceof Hexagon) {
//                result.add("resource", context.serialize(((Hexagon) shape).getResource(), Shape.class));
//                result.add("die", context.serialize(((Hexagon) shape).getDie(), Shape.class));
//            }
//            if (shape instanceof Vertex) {
//                result.add("owner", context.serialize(((Vertex) shape).getOwner(), Shape.class));
//                result.add("level", context.serialize(((Vertex) shape).getLevel(), Shape.class));
//            }
//            return result;
//        }
//
//        @Override public Shape deserialize(JsonElement json, Type typeOfT,
//                                              JsonDeserializationContext context) throws JsonParseException {
//            JsonObject object = json.getAsJsonObject();
//            Shape result = null;
//
//            String type = context.deserialize(object.get("type"), String.class);
//            Point_QR coord = context.deserialize(object.get("coord"), Point_QR.class);
//
//            if (type.equals("hexagon")) {
//                result = new Hexagon(coord.q(), coord.r());
//                ((Hexagon)result).setResource(object.get("resource").getAsInt());
//                ((Hexagon)result).setDie(object.get("die").getAsInt());
//            }
//            else if (type.equals("vertex")) {
//                result = new Vertex(coord.q(), coord.r());
//                ((Vertex)result).setOwner(object.get("owner").getAsInt());
//                ((Vertex)result).setLevel(object.get("level").getAsInt());
//            }
//
//            return result;
//        }
//    }

    static class Collect
    {
        Shape[] shapes;
    }


    public void testJSON()
    {
        System.out.println("Testing gson de/serialization\n");
        Gson gson = new GsonBuilder()
//                .registerTypeHierarchyAdapter(Shape.class, new ShapeAdapter())
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

        new BoardTest().testJSON();

        System.out.println("\ntests complete");
    }

}
