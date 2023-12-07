package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.HashMap;
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
    private static HashMap<String, Course> courses;
    private static Course currentSelectedCourse = null;
    private  static Lesson currentSelectedLesson = null;

    public static HashMap<String, List<String>> filteredData = new HashMap<>();
    public static File currentLoadedCourseFile = null;
    public static ArrayList<SubCategoryButton> currentSelectedButtons = new ArrayList<>();
    public static Stage primaryStage;
    public static final String[] OS = System.getProperty("os.name").split(",");
    private static Label selectedCourseLabel;
    private static Label selectedLessonLabel;
    public static HashSet<String> historyPaths = new HashSet<>();
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

        filteredData = FilterDatabase.allData;
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

    public static HashMap<String, TreeMap<String, Collection<CardView>>> getFilterOptions() {
        return filterDatabase.getFilterOptions();
    }

    public static void resetFilteredData(){
        filteredData = new HashMap<>();
        filteredData.put("Event", new ArrayList<>());
        filteredData.put("Gender", new ArrayList<>());
        filteredData.put("ModelSex", new ArrayList<>());
        filteredData.put("Level", new ArrayList<>());
        filteredData.put("Equipments", new ArrayList<>());
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
        selectedLessonLabel.setText(lesson.getName());
    }

    public static Lesson getCurrentSelectedLesson() {
        return currentSelectedLesson;
    }

    public static void saveCurrentCourseLogToFile(File chosenFile) throws IOException {
        currentSelectedCourse.saveToFile(chosenFile);
        currentLoadedCourseFile = chosenFile;
    }

    public static void addCourseToCourses(Course courseToAdd){
        courses.putIfAbsent(courseToAdd.getName(), courseToAdd);
    }

    public static void setCurrentSelectedCourse(Course course){
        currentSelectedCourse = course;
        selectedCourseLabel.setText(course.getName());
    }
    public static void launchPrinting(){
        Printing.start();
    }


    public static void saveCourseHistory(String loadPath) throws IOException {
        if (!historyPaths.contains(loadPath)) {
            File file = new File("src/main/resources/edu/augustana/staticFiles/loadFiles.txt");
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(loadPath + "\n");
                historyPaths.add(loadPath);
            }
        }
    }


private static void loadCourseHistory() throws IOException{
    try {
        // Read all lines from the file into a Set of Strings
        historyPaths = new HashSet<>(Files.readAllLines(Paths.get("src/main/resources/edu/augustana/staticFiles/loadFiles.txt")));
    } catch (NoSuchFileException e) {
        // Handle the case where the file does not exist
        System.err.println("File does not exist: ");
        // Initialize historyPaths as an empty set
        historyPaths = new HashSet<>();
    }
}
    public static Course getCurrentSelectedCourse() {
        return currentSelectedCourse;
    }

    public static void main(String[] args) {
        launch();
    }
}

