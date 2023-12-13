package edu.augustana;

import com.google.gson.*;
import javafx.scene.layout.HBox;

import java.lang.reflect.Type;

public class HBoxSerializer implements JsonSerializer<HBox>, JsonDeserializer<HBox> {
    /**
     * Converts a HBox object to Json
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context: The context for serialization that should be used to build JsonElements.
     * @return: JsonElement converted from HBox object
     */
    @Override
    public JsonElement serialize(HBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add any necessary properties of HBox to the jsonObject
        // For example, you might want to store the spacing property:
        jsonObject.addProperty("spacing", src.getSpacing());
        return jsonObject;
    }

    /**
     * Converts Json to a HBox object
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context: The context for deserialization
     * @return: HBox object converted from Json
     * @throws JsonParseException: if json is not in the expected format of HBox
     */
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
