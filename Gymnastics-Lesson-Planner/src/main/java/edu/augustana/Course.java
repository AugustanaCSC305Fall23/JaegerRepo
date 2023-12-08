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

    public void saveToFile(File logFile) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();

        String serializedMovieLogText = gson.toJson(this);
        PrintWriter writer = new PrintWriter(new FileWriter(logFile));
        writer.println(serializedMovieLogText);
        writer.close();
    }

    public static Course loadFromFile(File logFile) throws IOException {
        FileReader reader = new FileReader(logFile);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();
        return gson.fromJson(reader, Course.class);
    }
}
