package edu.augustana;

import java.util.ArrayList;

public class Lesson {
    private ArrayList<Card> cards;

    public Lesson(){
        cards = new ArrayList<>();
    }

    public void addCard(Card newCard){
        cards.add(newCard);
    }
    
}
