package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeMap;

/**
 * JavaFX App
 */
public class App extends Application {
    public static String pathToTargetFolder;
    public static String pathToResourcesFolder;
    private static boolean selected;
    private  static Lesson currentSelectedLesson = null;
    private static CardDatabase cardDatabase;
    private static FilterDatabase filterDatabase;
    private static HashMap<String, Lesson> lessons;
    public static SubCategoryButton equipmentFilterValue;
    public static SubCategoryButton eventFilterValue;
    public static SubCategoryButton modelSexFilterValue;
    public static SubCategoryButton levelFilterValue;
    public static SubCategoryButton genderFilterValue;
    public static GridPane allCardsLoadedGridPane = null;
    public static HashMap<String, HashSet<CardView>> filteredData = new HashMap<>();
    private static File currentLessonLogFile = null;
    public static SubCategoryButton currentSelectedEquipment;
    public static ArrayList<SubCategoryButton> currentSelectedButtons = new ArrayList<>();

    public static final String[] OS = System.getProperty("os.name").split(",");

    @Override
    public void start(Stage stage) throws IOException {
        pathToTargetFolder = Objects.requireNonNull(App.class.getResource("")).toExternalForm().substring(6);
        if (!OS[0].equals("Windows")){
            pathToTargetFolder = "/" + pathToTargetFolder;
        }
        pathToResourcesFolder = pathToTargetFolder + "../../../../src/main/resources/edu/augustana/";
        selected = false;
        cardDatabase = new CardDatabase();
        cardDatabase.addCardPack(pathToTargetFolder + "staticFiles/Demo1/DEMO1.csv");
        filterDatabase = new FilterDatabase(cardDatabase);

        lessons = new HashMap<>();
        lessons.put("demo lesson 1", new Lesson("demo lesson 1"));
        lessons.put("demo lesson 2", new Lesson("demo lesson 2"));

        Scene scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(pathToTargetFolder + "staticFiles/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    public static HashMap<String, Lesson> getLessons() {
        return lessons;
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

    public static HashMap<String, TreeMap<String, HashSet<CardView>>> getFilterOptions() {
        return filterDatabase.getFilterOptions();
    }

    public static HashMap<Integer, Card> getCardDatabase() {
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
    public static void addLessonToLessons(Lesson lessonToAdd){
        if (lessons == null){
            App.lessons = new HashMap<>();
        }
        lessons.put(lessonToAdd.getLessonName(), lessonToAdd);

    }
    public static void setCurrentSelectedLesson(Lesson lesson){
        currentSelectedLesson = lesson;
        selected = true;
    }

    public static Lesson getCurrentSelectedLesson() {
        return currentSelectedLesson;
    }

    public static FilterDatabase getFilterDatabase() {
        return filterDatabase;
    }
    public static void saveCurrentLessonLogToFile(File chosenFile) throws IOException {
        currentSelectedLesson.saveToFile(chosenFile);
        currentLessonLogFile = chosenFile;
    }

    public static void main(String[] args) {
        launch();
    }
}

