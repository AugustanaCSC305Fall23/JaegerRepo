package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
    private static CardDatabase cardDatabase;
    private static FilterDatabase filterDatabase;
    private static ArrayList<Lesson> lessons;
    private static HashMap<String, Course> courses;
    private static Course currentSelectedCourse = null;
    private  static Lesson currentSelectedLesson = null;
    public static GridPane allCardsLoadedGridPane = null;
    public static HashMap<String, HashSet<CardView>> filteredData = new HashMap<>();
    private static File currentLessonLogFile = null;
    public static ArrayList<SubCategoryButton> currentSelectedButtons = new ArrayList<>();
    public static Stage primaryStage;
    public static final String[] OS = System.getProperty("os.name").split(",");
    private static Label selectedCourseLabel;
    private static Label selectedLessonLabel;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        pathToTargetFolder = Objects.requireNonNull(App.class.getResource("")).toExternalForm().substring(6);
        if (!OS[0].equals("Windows")){
            pathToTargetFolder = "/" + pathToTargetFolder;
        }
        pathToResourcesFolder = pathToTargetFolder + "../../../../src/main/resources/edu/augustana/";
        selected = false;
        cardDatabase = new CardDatabase();
        cardDatabase.addCardPack(pathToTargetFolder + "staticFiles/Demo1/DEMO1.csv");
        filterDatabase = new FilterDatabase(cardDatabase);

        courses = new HashMap<>();
        courses.put("demo course 1", new Course("demo Course 1"));
        courses.get("demo course 1").addData(new Lesson("Demo Lesson 1"));
        courses.get("demo course 1").addData(new Lesson("Demo Lesson 2"));
        courses.put("demo course 2", new Course("demo Course 2"));
        courses.get("demo course 2").addData(new Lesson("Demo Lesson 1"));
        courses.get("demo course 2").addData(new Lesson("Demo Lesson 2"));
        courses.get("demo course 2").addData(new Lesson("Demo Lesson 3"));

        Scene scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(pathToTargetFolder + "staticFiles/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
        VBox labels = ((VBox)((VBox)(scene.getRoot().getChildrenUnmodifiable().get(1))).getChildren().get(0));
        selectedCourseLabel = (Label) ((HBox) labels.getChildren().get(0)).getChildren().get(0);
        selectedLessonLabel = (Label) ((HBox) labels.getChildren().get(1)).getChildren().get(0);
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

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static HashMap<Integer, Card> getCardDatabase() {
        return cardDatabase.getCards();
    }

    public static FilterDatabase getFilterDatabase() {
        return filterDatabase;
    }

    public static HashMap<String, Course> getCourses() {
        return courses;
    }

    public static boolean isLessonSelected(){
        return currentSelectedLesson != null;
    }

    public static boolean isCourseSelected(){
        return currentSelectedCourse != null;
    }

    public static void setCurrentSelectedLesson(Lesson lesson){
        currentSelectedLesson = lesson;
        selected = true;
        selectedLessonLabel.setText(lesson.getName());
    }

    public static Lesson getCurrentSelectedLesson() {
        return currentSelectedLesson;
    }

    public static void saveCurrentCourseLogToFile(File chosenFile) throws IOException {
        currentSelectedCourse.saveToFile(chosenFile);
        currentLessonLogFile = chosenFile;
    }

    public static void addCourseToCourses(Course courseToAdd){
        courses.putIfAbsent(courseToAdd.getName(), courseToAdd);
    }

    public static void setCurrentSelectedCourse(Course course){
        currentSelectedCourse = course;
        lessons = course.getLessons();
        selected = true;
        selectedCourseLabel.setText(course.getName());
    }

    public static Course getCurrentSelectedCourse() {
        return currentSelectedCourse;
    }

    public static void main(String[] args) {
        launch();
    }
}

