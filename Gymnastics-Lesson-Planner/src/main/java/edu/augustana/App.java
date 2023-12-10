package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String[] OS = System.getProperty("os.name").split(",");
    public static String pathToTargetFolder;
    public static String pathToResourcesFolder;
    public static HashMap<String, List<String>> filteredData = new HashMap<>();
    public static File currentLoadedCourseFile = null;
    public static ArrayList<SubCategoryButton> currentSelectedButtons = new ArrayList<>();
    public static Stage primaryStage;
    public static HashMap<String, String> historyPaths;
    private static FilterDatabase filterDatabase;
    private static HashMap<String, Course> courses;
    private static Course currentSelectedCourse = null;
    private static Lesson currentSelectedLesson = null;
    private static Label selectedCourseLabel;
    private static Label selectedLessonLabel;

    public static HashMap<String, TreeMap<String, Collection<CardView>>> getFilterOptions() {
        return filterDatabase.getFilterOptions();
    }

    public static void resetFilteredData() {
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

    public static FilterDatabase getFilterDatabase() {
        return filterDatabase;
    }

    public static HashMap<String, Course> getCourses() {
        return courses;
    }

    public static boolean isLessonSelected() {
        return currentSelectedLesson != null;
    }

    public static boolean isCourseSelected() {
        return currentSelectedCourse != null;
    }

    public static Lesson getCurrentSelectedLesson() {
        return currentSelectedLesson;
    }

    public static void setCurrentSelectedLesson(Lesson lesson) {
        currentSelectedLesson = lesson;
        selectedLessonLabel.setText(" ");
        selectedLessonLabel.setText(lesson.getName());
    }

    public static void saveCurrentCourseLogToFile(File chosenFile) throws IOException {
        currentSelectedCourse.saveToFile(chosenFile);
        currentLoadedCourseFile = chosenFile;
    }

    public static void addCourseToCourses(Course courseToAdd) {
        courses.putIfAbsent(courseToAdd.getName(), courseToAdd);
    }

    public static void launchPrinting() {
        Printing.start();
    }

    public static void saveCourseHistory() throws IOException {
        File file = new File("src/main/resources/edu/augustana/staticFiles/loadFiles.txt");
        for (String path: historyPaths.values()){
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(path + "\n");
            }
        }
    }

    public static void clearHistory() {
        try (FileWriter writer = new FileWriter("src/main/resources/edu/augustana/staticFiles/loadFiles.txt", false)) {
            // Opening the file in write mode with 'false' as the second parameter truncates the file
            // This effectively clears its contents
            writer.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadCourseHistory() throws IOException {
        // Read all lines from the file into a Set of Strings
        historyPaths = new HashMap<>();
        for (String path : Files.readAllLines(Paths.get("src/main/resources/edu/augustana/staticFiles/loadFiles.txt"))) {
            try {
                File f = new File(path);
                Course loadedCourse = Course.loadFromFile(f);
                loadedCourse.setCourseName(removeFileExtension(f.getName()));
                courses.put(removeFileExtension(f.getName()), loadedCourse);
                historyPaths.put(loadedCourse.getName(), path);
            } catch (Exception e) {
                System.out.println("file not found");
            }
        }
        saveCourseHistory();
    }

    public static Course getCurrentSelectedCourse() {
        return currentSelectedCourse;
    }

    public static void setCurrentSelectedCourse(Course course) {
        currentSelectedCourse = course;
        selectedCourseLabel.setText(course.getName());
    }

    public static void main(String[] args) {
        launch();
    }

    public static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        pathToTargetFolder = Objects.requireNonNull(App.class.getResource("")).toExternalForm().substring(6);
        if (!OS[0].equals("Windows")) {
            pathToTargetFolder = "/" + pathToTargetFolder;
        }
        pathToResourcesFolder = pathToTargetFolder + "../../../../src/main/resources/edu/augustana/";

        CardDatabase cardDatabase = new CardDatabase();
        cardDatabase.addCardPack(pathToTargetFolder + "staticFiles/Demo1/DEMO1.csv");

        filterDatabase = new FilterDatabase();
        filterDatabase.addFilterOptions(cardDatabase.getCards());

        courses = new HashMap<>();
        loadCourseHistory();

        courses = new HashMap<>();
        courses.put("demonew", new Course("demonew"));
        courses.get("demonew").addData(new Lesson("this is new"));

        filteredData = FilterDatabase.allData;
        Scene scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(pathToTargetFolder + "staticFiles/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        VBox labels = ((VBox) ((VBox) (scene.getRoot().getChildrenUnmodifiable().get(1))).getChildren().get(0));
        selectedCourseLabel = (Label) ((HBox) labels.getChildren().get(0)).getChildren().get(0);
        selectedLessonLabel = (Label) ((HBox) labels.getChildren().get(1)).getChildren().get(0);
    }
}

