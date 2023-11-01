package edu.augustana;

import java.util.ArrayList;

public class Lesson {
    private final ArrayList<Integer> cardIndexes ;
    private final String lessonName;
    private static int TotalLessonIndex = 0;
    private final int lessonIndex;
    public Lesson(String lessonName){
        cardIndexes = new ArrayList<>();
        this.lessonName = lessonName;
        Lesson.TotalLessonIndex++;
        lessonIndex = Lesson.TotalLessonIndex;
    }
    public ArrayList<Integer> getCardIndexes() {
        return cardIndexes;
    }
    public void addCard(Card newCard){
        if (!cardIndexes.contains(newCard.getCardId())) {
            cardIndexes.add(newCard.getCardId());
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
