package edu.augustana;

import com.google.gson.*;
import javafx.scene.image.ImageView;

import java.lang.reflect.Type;

public class ImageViewSerializer implements JsonSerializer<ImageView>, JsonDeserializer<ImageView> {
    @Override
    public JsonElement serialize(ImageView src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add any necessary properties of ImageView to the jsonObject
        // For example, you might want to store the URL of the image:
        jsonObject.addProperty("imageUrl", src.getImage().getUrl());
        return jsonObject;
    }

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