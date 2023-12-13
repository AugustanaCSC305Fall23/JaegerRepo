package edu.augustana;

import java.util.ArrayList;

public class Lesson {
    private final ArrayList<CardView> selectedCardViews;
    private final ArrayList<String> cardIds = new ArrayList<>();
    private final String lessonName;

    /**
     * Constructor for Lesson object
     * @param lessonName: String of the name of the lesson
     */
    public Lesson(String lessonName) {
        selectedCardViews = new ArrayList<>();
        this.lessonName = lessonName;
    }

    /**
     * Gets the selected cards in the lesson
     * @return: ArrayList of CardView objects
     */
    public ArrayList<CardView> getSelectedCardViews() {
        return selectedCardViews;
    }

    /**
     * Adds a card to the lesson
     * @param cardView: CardView object to add to the lesson
     * @return: Boolean of whether the card was added
     */
    public boolean addData(CardView cardView) {
        if (!cardIds.contains(cardView.getCardId())) {
            cardIds.add(cardView.getCardId());
            selectedCardViews.add(cardView);
            return true;
        } else {
            System.out.println("card already selected");
            return false;
        }
    }

    /**
     * Removes a card from the lesson
     * @param cardView: CardView object to remove from the lesson
     * @return: Boolean of whether the card was removed
     */
    public boolean removeData(CardView cardView) {
        if (cardIds.contains(cardView.getCardId())) {
            selectedCardViews.remove(cardIds.indexOf(cardView.getCardId()));
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
        for (CardView cardView : selectedCardViews) {
            for (String e : cardView.getEquipments()) {
                if (!equipments.contains(e)) {
                    equipments.add(e);
                }
            }
        }
        return equipments;
    }

    public ArrayList<String> getCardIds() {
        return cardIds;
    }
}
