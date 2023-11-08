package edu.augustana;

import java.util.HashMap;

public class Course {
    private String courseName;
    private HashMap<Integer, Lesson> lessons;

    public Course(String courseName){
        this.courseName = courseName;
        lessons = new HashMap<>();
    }

    public HashMap<Integer, Lesson> getLessons() {
        return lessons;
    }

    public String getCourseName() {
        return courseName;
    }

    public void addLesson(Lesson newLesson){
        lessons.put(newLesson.getLessonIndex(), newLesson);
    }
}
