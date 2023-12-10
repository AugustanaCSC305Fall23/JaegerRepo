package edu.augustana;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson{
    private final ArrayList<CardView> selectedCardViews;
    private final ArrayList<String> cardIds = new ArrayList<>();
    private final String lessonName;
    public Lesson(String lessonName){
        selectedCardViews = new ArrayList<>();
        this.lessonName = lessonName;
    }
    public ArrayList<CardView> getSelectedCardViews() {
        return selectedCardViews;
    }

    public boolean addData(CardView cardView){
        if (!cardIds.contains(cardView.getCardId())) {
            cardIds.add(cardView.getCardId());
            selectedCardViews.add(cardView);
            return true;
        }else{
            System.out.println("card already selected");
            return false;
        }
    }

    public boolean removeData(CardView cardView){
        if (cardIds.contains(cardView.getCardId())){
            selectedCardViews.remove(cardView);
            cardIds.remove(cardView.getCardId());
            return true;
        }
        return false;
    }

    public String getName() {
        return lessonName;
    }

    public ArrayList<String> getEquipments() {
        ArrayList<String> equipments = new ArrayList<>();
        for (CardView cardView: selectedCardViews) {
            for (String e : cardView.getEquipments()) {
                if (!equipments.contains(e)) {
                    equipments.add(e);
                }
            }
        }
        return equipments;
    }

    public ArrayList<String> getCardIds(){
        return cardIds;
    }
}
