package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.ArrayList;

public class Course {
    private String courseName;
    private final ArrayList<Lesson> lessonsInCourse;

    /**
     * Constructor for Course object
     * @param courseName: String of the name of the course
     */
    public Course(String courseName) {
        this.courseName = courseName;
        lessonsInCourse = new ArrayList<>();
    }

    /**
     * Loads a course from a file
     * @param logFile: File object of the file to load
     * @return: Course object
     * @throws IOException: Throws exception if file is not found
     */
    public static Course loadFromFile(File logFile) throws IOException {
        FileReader reader = new FileReader(logFile);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();
        Course loadedCourse = gson.fromJson(reader, Course.class);
        reader.close();
        String fileName = App.removeFileExtension(logFile.getName());
        App.addToHistory(fileName, logFile);
        loadedCourse.setCourseName(fileName);
        return loadedCourse;
    }

    public ArrayList<Lesson> getLessons() {
        return lessonsInCourse;
    }

    public String getName() {
        return courseName;
    }

    /**
     * Adds a lesson to the course
     * @param newLesson: Lesson object to add to the course
     */
    public void addData(Lesson newLesson) {
        lessonsInCourse.add(newLesson);
    }

    /**
     * Saves the course to a file
     * @param logFile: File object of the file to save to
     */
    public void saveToFile(File logFile){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();

        String serializedCourseLogText = gson.toJson(this);

        // Check if the serialized text is not null and logFile is not null before creating the PrintWriter
        if (serializedCourseLogText != null && logFile != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
                writer.println(serializedCourseLogText);
            } catch (IOException e) {
                // Handle or log the IOException
                e.printStackTrace();
            }
        } else {
            System.err.println("Failed to serialize the object to JSON or logFile is null.");
        }
    }

    /**
     * Sets the name of the course
     * @param name: String of the name of the course
     */
    public void setCourseName(String name) {
        this.courseName = name;
    }

    /**
     * Removes a lesson from the course
     * @param index: int of the index of the lesson to remove
     */
    public void removeLesson(int index) {
        lessonsInCourse.remove(index);
    }
}
