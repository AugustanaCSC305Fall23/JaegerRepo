package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Course{
    private String courseName;
    private ArrayList<Lesson> lessonsInCourse;

    public Course(String courseName){
        this.courseName = courseName;
        lessonsInCourse = new ArrayList<>();
    }

    public ArrayList<Lesson> getLessons() {
        return lessonsInCourse;
    }

    public String getName() {
        return courseName;
    }

    public void addData(Lesson newLesson){
        lessonsInCourse.add(newLesson);
    }

//    public void saveToFile(File logFile) throws IOException {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
//                .registerTypeAdapter(HBox.class, new HBoxSerializer())
//                .create();
//
//        String serializedMovieLogText = gson.toJson(this);
//        PrintWriter writer = new PrintWriter(new FileWriter(logFile));
//        writer.println(serializedMovieLogText);
//        writer.close();
//    }
public void saveToFile(File logFile) throws IOException {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
            .registerTypeAdapter(HBox.class, new HBoxSerializer())
            .create();

    String serializedCourseLogText = gson.toJson(this);

    // Check if the serialized text is not null and logFile is not null before creating the PrintWriter
    if (serializedCourseLogText!= null && logFile != null) {
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

    public void setCourseName(String name){
        this.courseName = name;
    }
    public void removeLesson(int index){
        lessonsInCourse.remove(index);
    }
}
