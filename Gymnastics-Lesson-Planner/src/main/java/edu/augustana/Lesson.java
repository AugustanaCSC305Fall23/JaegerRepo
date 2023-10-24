package edu.augustana;

import java.util.ArrayList;
import java.util.HashMap;

public class Lesson {
    private HashMap<String, Card> cardHashMap;
    private String lessonName;

    public Lesson(String lessonName){
        cardHashMap = new HashMap<>();
        this.lessonName = lessonName;
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
    private String getLessonName() {
        return lessonName;
    }



}
