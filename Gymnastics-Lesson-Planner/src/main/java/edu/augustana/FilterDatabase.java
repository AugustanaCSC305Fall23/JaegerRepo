package edu.augustana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class FilterDatabase {
    private HashMap<String, TreeMap<String, HashSet<Card>>> filterOptions =  new HashMap<>();

    public FilterDatabase(CardDatabase cards){
        filterOptions = new HashMap<>();
        filterOptions.put("Event", new TreeMap<>());
        filterOptions.put("Gender", new TreeMap<>());
        filterOptions.put("ModelSex", new TreeMap<>());
        filterOptions.put("Level", new TreeMap<>());
        filterOptions.put("Equipments", new TreeMap<>());

        addFilterOptions(cards.getCards());
    }

    public void addFilterOptions(HashMap<Integer, Card> cards){
        for (int cardId: cards.keySet()) {
            Card currCard = cards.get(cardId);
            String event = cards.get(cardId).getEvent();
            String gender = cards.get(cardId).getGender();
            String modelSex = cards.get(cardId).getModelSex();
            String level = cards.get(cardId).getLevel();


            addToFilterOptions("Event", event, currCard);
            addToFilterOptions("Gender", gender, currCard);
            addToFilterOptions("ModelSex", modelSex, currCard);
            addToFilterOptions("Level", level, currCard);

            ArrayList<String> equipments = cards.get(cardId).getEquipment();
            for (String equipment : equipments) {
                if (equipment.contains("/")) {
                    for (String e : equipment.split("/")) {
                        addFormattedEquipment(e, currCard);
                    }
                } else {
                    addFormattedEquipment(equipment, currCard);
                }
            }
        }
        System.out.println(filterOptions.get("Event"));
    }

    private void addToFilterOptions(String category, String categoryValue, Card card){
        if (!filterOptions.get(category).containsKey(categoryValue)){
            filterOptions.get(category).put(categoryValue, new HashSet<>());
        }
        filterOptions.get(category).get(categoryValue).add(card);
    }

    private void addFormattedEquipment(String equipment, Card card){
        equipment = equipment.trim().replaceAll("\"", "");
        addToFilterOptions("Equipments", equipment, card);
    }

    public HashMap<String, TreeMap<String, HashSet<Card>>> getFilterOptions() {
        return filterOptions;
    }

}
