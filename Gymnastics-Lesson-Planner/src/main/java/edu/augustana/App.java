package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static final Image trashCan = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/trashCan.png")).toExternalForm());
    private static final Image redTrashCan = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/redTrashCan.png")).toExternalForm());

    public static String pathToTargetFolder;
    public static String pathToResourcesFolder;
    public static HashMap<String, List<String>> filteredData = new HashMap<>();
    public static File currentLoadedCourseFile = null;
    public static ArrayList<SubCategoryButton> currentSelectedButtons = new ArrayList<>();
    public static Stage primaryStage;
    public static HashMap<String, String> historyPaths;
    private static FilterDatabase filterDatabase;
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

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static FilterDatabase getFilterDatabase() {
        return filterDatabase;
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
        if (lesson == null){
            selectedLessonLabel.setText("No lesson selected");
        }else {
            selectedLessonLabel.setText(" ");
            selectedLessonLabel.setText(lesson.getName());
        }
    }

    public static void saveCurrentCourseLogToFile(File chosenFile) throws IOException {
        currentSelectedCourse.saveToFile(chosenFile);
        currentLoadedCourseFile = chosenFile;
    }

    public static boolean addCourseToCourses(Course courseToAdd) {
        if (historyPaths.get(courseToAdd.getName()) == null){
            setCurrentSelectedCourse(courseToAdd);
            return true;
        }else {
            return false;
        }
    }

    public static void addLessonToLessons(Lesson lessonToAdd){
        getCurrentSelectedCourse().addData(lessonToAdd);
        setCurrentSelectedLesson(lessonToAdd);
    }

    public static void launchPrinting() {
        Printing.start();
    }

    public static void saveCourseHistory() throws IOException {
        clearHistory();
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
    public static void addToHistory(String fileName, File logFile) throws IOException {
        if (App.historyPaths.get(fileName) != null){
            if (!App.historyPaths.get(fileName).equalsIgnoreCase(logFile.getAbsolutePath())){
                App.historyPaths.put(fileName + "_new", logFile.getAbsolutePath());
            }
        }else{
            App.historyPaths.put(fileName, logFile.getAbsolutePath());
        }
        saveCourseHistory();
    }

    public static void loadCourseHistory() throws IOException {
        // Read all lines from the file into a Set of Strings
        historyPaths = new HashMap<>();
        for (String path : Files.readAllLines(Paths.get("src/main/resources/edu/augustana/staticFiles/loadFiles.txt"))) {
            try {
                File f = new File(path);
                String fileName = removeFileExtension(f.getName());
                Course loadedCourse = Course.loadFromFile(f);
                addToHistory(fileName, f);
                loadedCourse.setCourseName(fileName);
                historyPaths.put(removeFileExtension(loadedCourse.getName()), path);
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
        if (course == null){
            selectedCourseLabel.setText("No course selected");
            setCurrentSelectedLesson(null);
        }else {
            selectedCourseLabel.setText(course.getName());
        }
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
    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder titleCase = new StringBuilder();
        boolean convertNext = true;

        for (char ch : input.toCharArray()) {
            titleCase.append(convertNext ? Character.toTitleCase(ch) : Character.toLowerCase(ch));
            convertNext = Character.isSpaceChar(ch);
        }

        return titleCase.toString();
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

        loadCourseHistory();
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
    public static ImageView getDeleteIcon(){
        ImageView deleteIcon = new ImageView(trashCan);
        deleteIcon.setPreserveRatio(true);
        deleteIcon.setFitWidth(30);
        deleteIcon.setOnMouseEntered(event -> deleteIcon.setImage(redTrashCan));
        deleteIcon.setOnMouseExited(event -> deleteIcon.setImage(trashCan));

        return deleteIcon;
    }
}

