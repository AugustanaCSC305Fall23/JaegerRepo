package edu.augustana;

import com.google.gson.*;
import javafx.scene.image.ImageView;

import java.lang.reflect.Type;

public class ImageViewSerializer implements JsonSerializer<ImageView>, JsonDeserializer<ImageView> {
    /**
     * Converts a ImageView object to Json
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context: The context for serialization that should be used to build JsonElements.
     * @return: JsonElement converted from ImageView object
     */
    @Override
    public JsonElement serialize(ImageView src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add any necessary properties of ImageView to the jsonObject
        // For example, you might want to store the URL of the image:
        jsonObject.addProperty("imageUrl", src.getImage().getUrl());
        return jsonObject;
    }

    /**
     * Converts Json to a ImageView object
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context: The context for deserialization
     * @return: ImageView object converted from Json
     * @throws JsonParseException: if json is not in the expected format of ImageView
     */
    @Override
    public ImageView deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Retrieve necessary properties from jsonObject
        String imageUrl = jsonObject.get("imageUrl").getAsString();

        // Create a new ImageView and set its properties
        ImageView imageView = new ImageView();
        // For example, you might want to set the image using the URL:
        imageView.setImage(new javafx.scene.image.Image(imageUrl));
        return imageView;
    }
}