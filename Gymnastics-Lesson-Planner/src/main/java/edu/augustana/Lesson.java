package edu.augustana;

import java.util.ArrayList;

public class Lesson {
    private ArrayList<String> cardIndexes ;
    private String lessonName;
    private static int TotalLessonIndex = 0;
    private int lessonIndex;
    public Lesson(String lessonName){
        cardIndexes = new ArrayList<>();
        this.lessonName = lessonName;
        Lesson.TotalLessonIndex++;
        lessonIndex = Lesson.TotalLessonIndex;
    }
    public ArrayList<String> getCardIndexes() {
        return cardIndexes;
    }
    public void addCard(Card newCard){
        if (!cardIndexes.contains(newCard.getCode())) {
            cardIndexes.add(newCard.getCode());
        }else{
            System.out.println("card already selected");
        }
    }

    public int getLessonIndex() {
        return lessonIndex;
    }

    public static int getTotalLessonIndex() {
        return TotalLessonIndex;
    }

    public String getLessonName() {
        return lessonName;
    }




}
