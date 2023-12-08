package edu.augustana;

import com.google.gson.*;
import javafx.scene.layout.HBox;

import java.lang.reflect.Type;

public class HBoxSerializer implements JsonSerializer<HBox>, JsonDeserializer<HBox> {
    @Override
    public JsonElement serialize(HBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add any necessary properties of HBox to the jsonObject
        // For example, you might want to store the spacing property:
        jsonObject.addProperty("spacing", src.getSpacing());
        return jsonObject;
    }

    @Override
    public HBox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Retrieve necessary properties from jsonObject
        double spacing = jsonObject.get("spacing").getAsDouble();

        // Create a new HBox and set its properties
        HBox hBox = new HBox();
        // For example, you might want to set the spacing property:
        hBox.setSpacing(spacing);
        return hBox;
    }
}
