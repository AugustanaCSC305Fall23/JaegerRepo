package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String imagesFilePath = System.getProperty("user.dir") + "/src/main/resources/edu/augustana/staticFiles";
    // relative path didn't work so this finds the absolute path to the images folder regardless of the device
    private static Scene scene;
    private static boolean selected;
    private static CardDatabase cardDatabase;
    private static FilterDatabase filterDatabase;
    private static HashMap<Integer, Lesson> lessons;

    @Override
    public void start(Stage stage) throws IOException {
        selected = false;
        cardDatabase = new CardDatabase();
        cardDatabase.addCardPack(imagesFilePath + "/Demo1/DEMO1.csv");
        filterDatabase = new FilterDatabase(cardDatabase);

        scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(imagesFilePath + "/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    public static HashMap<Integer, Lesson> getLessons() {
        return lessons;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String restOfWord = word.substring(1).toLowerCase();
                result.append(firstLetter).append(restOfWord).append(" ");
            } else {
                result.append(word);
            }
        }

        // Remove the trailing space
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    public static HashMap<String, TreeSet<String>> getFilterOptions() {
        return filterDatabase.getFilterOptions();
    }

    public static HashMap<String, Card> getCardDatabase() {
        return cardDatabase.getCards();
    }

    public static void lessonSelected(boolean selected){
        App.selected = selected;
    }

    public static boolean isLessonSelected(){
        return selected;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
