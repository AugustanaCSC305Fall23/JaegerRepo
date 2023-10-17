package edu.augustana;

import java.util.ArrayList;

public class Lesson {
    private ArrayList<Card> cards;
    private String lessonName;

    public Lesson(String lessonName){
        cards = new ArrayList<>();
        this.lessonName = lessonName;
    }

    public void addCard(Card newCard){
        cards.add(newCard);
    }

    public String getLessonName() {
        return lessonName;
    }

}
