package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Course {
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

    public void addLesson(Lesson newLesson){
        lessonsInCourse.add(newLesson);
    }

    public void saveToFile(File logFile) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String serializedMovieLogText = gson.toJson(this);
        PrintWriter writer = new PrintWriter(new FileWriter(logFile));
        writer.println(serializedMovieLogText);
        writer.close();
    }
}
