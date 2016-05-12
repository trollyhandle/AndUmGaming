package GsonRuntimeAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import Game.Hexagon;
import Game.Point_QR;
import Game.Shape;
import Game.Vertex;

/**
 * Created by Tyler on 5/11/16.
 */
class ShapeAdapter implements JsonSerializer<Shape>, JsonDeserializer<Shape> {

    @Override public JsonElement serialize(Shape shape, Type typeOfSrc,
                                           JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("coord", context.serialize(shape.getCoord(), Point_QR.class));
        result.add("type", context.serialize(shape.type(), String.class));
        if (shape instanceof Hexagon) {
            result.add("resource", context.serialize(((Hexagon) shape).getResource(), Shape.class));
            result.add("die", context.serialize(((Hexagon) shape).getDie(), Shape.class));
        }
        if (shape instanceof Vertex) {
            result.add("owner", context.serialize(((Vertex) shape).getOwner(), Shape.class));
            result.add("level", context.serialize(((Vertex) shape).getLevel(), Shape.class));
        }
        return result;
    }

    @Override public Shape deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Shape result = null;

        String type = context.deserialize(object.get("type"), String.class);
        Point_QR coord = context.deserialize(object.get("coord"), Point_QR.class);

        if (type.equals("hexagon")) {
            result = new Hexagon(coord.q(), coord.r());
            ((Hexagon)result).setResource(object.get("resource").getAsInt());
            ((Hexagon)result).setDie(object.get("die").getAsInt());
        }
        else if (type.equals("vertex")) {
            result = new Vertex(coord.q(), coord.r());
            ((Vertex)result).setOwner(object.get("owner").getAsInt());
            ((Vertex)result).setLevel(object.get("level").getAsInt());
        }

        return result;
    }
}