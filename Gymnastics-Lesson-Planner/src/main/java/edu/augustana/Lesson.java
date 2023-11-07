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
    public boolean addCard(Card newCard){
        if (!cardIndexes.contains(newCard.getCardId())) {
            cardIndexes.add(newCard.getCardId());
            return true;
        }else{
            System.out.println("card already selected");
            return false;
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
