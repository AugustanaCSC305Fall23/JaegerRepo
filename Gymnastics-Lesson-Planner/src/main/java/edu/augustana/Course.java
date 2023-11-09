package edu.augustana;

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

    public String getCourseName() {
        return courseName;
    }

    public void addLesson(Lesson newLesson){
        lessonsInCourse.add(newLesson);
    }
}
