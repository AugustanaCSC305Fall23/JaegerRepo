package edu.augustana;

import java.util.HashMap;

public class Lesson {
    private HashMap<String, Card> cardHashMap;
    private String lessonName;
    private static int TotalLessonIndex = 0;
    private int lessonIndex;
    public Lesson(String lessonName){
        cardHashMap = new HashMap<>();
        this.lessonName = lessonName;
        Lesson.TotalLessonIndex++;
        lessonIndex = Lesson.TotalLessonIndex;
    }
    public HashMap<String, Card> getCardHashMap() {
        return cardHashMap;
    }
    private void addCard(Card newCard){
        if (cardHashMap.get(newCard.getCode()) != null) {
            cardHashMap.put(newCard.getCode(), newCard);
        }else{
            System.out.println("card already selected");
        }
    }

    public int getLessonIndex() {
        return lessonIndex;
    }

    public String getLessonName() {
        return lessonName;
    }



}
