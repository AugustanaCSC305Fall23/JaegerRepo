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
    public static final Image trashCan = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/trashCan.png")).toExternalForm());
    public static final Image redTrashCan = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/redTrashCan.png")).toExternalForm());
    public static Image addButton = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm());
    public static Image heartButton = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/heart.png")).toExternalForm());
    public static Image fillAddButton = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/fillAdd.png")).toExternalForm());
    public static Image fillHeartButton = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/fillHeart.png")).toExternalForm());
    public static Image selected = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/selected.png")).toExternalForm());

    public static String pathToTargetFolder;
    public static String pathToResourcesFolder;
    public static String pathToCardDataFolder;
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

    /**
     * Resets the filteredData HashMap to an empty HashMap
     */
    public static void resetFilteredData() {
        filteredData = new HashMap<>();
        filteredData.put("Event", new ArrayList<>());
        filteredData.put("Gender", new ArrayList<>());
        filteredData.put("ModelSex", new ArrayList<>());
        filteredData.put("Level", new ArrayList<>());
        filteredData.put("Equipments", new ArrayList<>());
    }

    /**
     * Loads the FXML file with the given name
     *
     * @param fxml the name of the FXML file to load
     * @return the Parent object of the FXML file
     * @throws IOException if the FXML file is not found
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Returns the FilterDatabase object
     *
     * @return the FilterDatabase object
     */
    public static FilterDatabase getFilterDatabase() {
        return filterDatabase;
    }

    /**
     * Returns whether a course is selected
     * @return whether a course is selected
     */
    public static boolean isCourseSelected() {
        return currentSelectedCourse != null;
    }

    /**
     *  Returns the current selected buttons
     * @return the current selected buttons
     */
    public static Lesson getCurrentSelectedLesson() {
        return currentSelectedLesson;
    }

    /**
     * Sets the current selected lesson
     * @param lesson the lesson to set as the current selected lesson
     */
    public static void setCurrentSelectedLesson(Lesson lesson) {
        currentSelectedLesson = lesson;
        if (lesson == null) {
            selectedLessonLabel.setText("No lesson selected");
        } else {
            selectedLessonLabel.setText(" ");
            selectedLessonLabel.setText(lesson.getName());
        }
    }

    /**
     * Saves the current course log to the given file
     * @param chosenFile: the file to save the course log to
     */
    public static void saveCurrentCourseLogToFile(File chosenFile){
        currentSelectedCourse.saveToFile(chosenFile);
        currentLoadedCourseFile = chosenFile;
    }

    /**
     * Adds the given course to the courses
     * @param courseToAdd: the course to add to the courses
     * @return: whether the course was added
     */
    public static boolean addCourseToCourses(Course courseToAdd) {
        if (historyPaths.get(courseToAdd.getName()) == null) {
            setCurrentSelectedCourse(courseToAdd);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the given lesson to the lessons
     * @param lessonToAdd: the lesson to add to the lessons
     */
    public static void addLessonToLessons(Lesson lessonToAdd) {
        getCurrentSelectedCourse().addData(lessonToAdd);
        setCurrentSelectedLesson(lessonToAdd);
    }

    /**
     * Launches the printing
     */
    public static void launchPrinting() {
        Printing.start();
    }

    /**
     * Saves the course history to a file
     * @throws IOException: if the file is not found
     */
    public static void saveCourseHistory() throws IOException {
        clearHistory();
        File file = new File("src/main/resources/edu/augustana/staticFiles/loadFiles.txt");
        for (String path : historyPaths.values()) {
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(path + "\n");
            }
        }
    }

    /**
     * Clears the history
     */
    public static void clearHistory() {
        try (FileWriter writer = new FileWriter("src/main/resources/edu/augustana/staticFiles/loadFiles.txt", false)) {
            // Opening the file in write mode with 'false' as the second parameter truncates the file
            // This effectively clears its contents
            writer.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the given file to the history
     * @param fileName: the name of the file to add to the history
     * @param logFile: the file to add to the history
     * @throws IOException: if the file is not found
     */
    public static void addToHistory(String fileName, File logFile) throws IOException {
        if (App.historyPaths.get(fileName) != null) {
            if (!App.historyPaths.get(fileName).equalsIgnoreCase(logFile.getAbsolutePath())) {
                App.historyPaths.put(fileName + "_new", logFile.getAbsolutePath());
            }
        } else {
            App.historyPaths.put(fileName, logFile.getAbsolutePath());
        }
        saveCourseHistory();
    }

    /**
     * Loads the course history
     * @throws IOException: if the file is not found
     */
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

    /**
     * Returns the current selected course
     * @return: the current selected course
     */
    public static Course getCurrentSelectedCourse() {
        return currentSelectedCourse;
    }

    /**
     * Sets the current selected course
     * @param course: the course to set as the current selected course
     */
    public static void setCurrentSelectedCourse(Course course) {
        currentSelectedCourse = course;
        if (course == null) {
            selectedCourseLabel.setText("No course selected");
            setCurrentSelectedLesson(null);
        } else {
            selectedCourseLabel.setText(course.getName());
        }
    }

    /**
     * Launches the application
     * @param args: the arguments to launch the application with
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Removes the file extension from the given file name
     * @param fileName: the file name to remove the file extension from
     * @return: the file name without the file extension
     */
    public static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }

    /**
     * Converts the given string to title case
     * @param input: the string to convert to title case
     * @return: the string in title case
     */
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

    /**
     * Loads the cards
     */
    public static void loadCards() {
        CardDatabase cardDatabase = new CardDatabase();
        try {
            for (String f : Objects.requireNonNull(new File(pathToCardDataFolder).list())) {
                File selectedDirectory = new File(pathToCardDataFolder, f);
                File[] csvFiles = selectedDirectory.listFiles((dir, name) -> name.endsWith(".csv"));
                if (csvFiles != null && csvFiles.length > 0) {
                    System.out.println("CSV file found: " + csvFiles[0].getName());
                }
                assert csvFiles != null;
                cardDatabase.addCardPack(String.valueOf(new File(selectedDirectory, csvFiles[0].getName())), f);
            }
        } catch (Exception e) {
            System.out.println("data not found");
        }
        System.out.println("added the cards in the card database");
        filterDatabase = new FilterDatabase();
        filterDatabase.addFilterOptions(cardDatabase.getCards());

        filteredData = FilterDatabase.allData;
    }

    /**
     * Returns the delete icon
     * @return: the delete icon
     */
    public static ImageView getDeleteIcon() {
        ImageView deleteIcon = new ImageView(trashCan);
        deleteIcon.setPreserveRatio(true);
        deleteIcon.setFitWidth(30);
        deleteIcon.setOnMouseEntered(event -> deleteIcon.setImage(redTrashCan));
        deleteIcon.setOnMouseExited(event -> deleteIcon.setImage(trashCan));
        return deleteIcon;
    }

    /**
     * Starts the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException: if the FXML file is not found
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        pathToTargetFolder = Objects.requireNonNull(App.class.getResource("")).toExternalForm().substring(6);
        if (!OS[0].equals("Windows")) {
            pathToTargetFolder = "/" + pathToTargetFolder;
        }
        pathToResourcesFolder = pathToTargetFolder + "../../../../src/main/resources/edu/augustana/";
        pathToCardDataFolder = pathToResourcesFolder + "staticFiles/CardData/";

        loadCards();
        loadCourseHistory();
        Scene scene = new Scene(loadFXML("primary"), 1400, 760);

        File cssFile = new File(pathToTargetFolder + "staticFiles/cssFiles/style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setMaximized(true);
        primaryStage.show();

        VBox labels = ((VBox) ((VBox) (scene.getRoot().getChildrenUnmodifiable().get(1))).getChildren().get(0));
        selectedCourseLabel = (Label) ((HBox) labels.getChildren().get(0)).getChildren().get(0);
        selectedLessonLabel = (Label) ((HBox) labels.getChildren().get(1)).getChildren().get(0);
    }
}

