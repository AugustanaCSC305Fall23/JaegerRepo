package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String imagesFilePath = System.getProperty("user.dir") + "/src/main/resources/edu/augustana/staticFiles";
    // relative path didn't work so this finds the absolute path to the images folder regardless of the device
    private static Scene scene;
    private static boolean selected;
    private static HashMap<String, Card> cardHashMap;
    private static HashMap<String, TreeSet<String>> filterOptions;

    @Override
    public void start(Stage stage) throws IOException {
        selected = false;
        cardHashMap = new HashMap<>();
        filterOptions = new HashMap<>();
        filterOptions.put("Event", new TreeSet<>());
        filterOptions.put("Gender", new TreeSet<>());
        filterOptions.put("ModelSex", new TreeSet<>());
        filterOptions.put("Level", new TreeSet<>());
        filterOptions.put("Equipments", new TreeSet<>());

        readDataFromFile();
        scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(imagesFilePath + "/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * @throws IOException
     *
     * Reads the csv file and creates card objects and stores it into a hashmap
     */
    public void readDataFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(imagesFilePath + "/Demo1/DEMO1.csv"));
        String line = br.readLine();
        while ((line = br.readLine()) != null)
        {
            String[] cardData = line.split(",");    // "spliting using comma cause it's a spece"
            Card newCard = new Card(cardData);
            addCardToFilterOptions(newCard);
            cardHashMap.put(cardData[0], newCard);
        }
    }

    private void addCardToFilterOptions(Card card){
        filterOptions.get("Event").add(card.getEvent());
        filterOptions.get("Gender").add(card.getGender());
        filterOptions.get("ModelSex").add(card.getModelSex());
        filterOptions.get("Level").add(card.getLevel());
        String[] equipments = card.getEquipment();
        for (String equipment : equipments) {
            if (equipment.contains("/")){
                for (String e : equipment.split("/")){
                    e = e.trim().replaceAll("[\"]", "");
                    filterOptions.get("Equipments").add(toTitleCase(e));
                }
            }else {
                equipment = equipment.trim().replaceAll("[\"]", "");
                filterOptions.get("Equipments").add(toTitleCase(equipment));
            }
        }
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
        return filterOptions;
    }

    public static HashMap<String, Card> getCardHashMap() {
        return cardHashMap;
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
