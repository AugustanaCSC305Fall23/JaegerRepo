package edu.augustana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static edu.augustana.App.toTitleCase;

public class FilterDatabase {
    private final HashMap<String, TreeSet<String>> filterOptions;

    public FilterDatabase(CardDatabase cards){
        filterOptions = new HashMap<>();
        filterOptions.put("Event", new TreeSet<>());
        filterOptions.put("Gender", new TreeSet<>());
        filterOptions.put("ModelSex", new TreeSet<>());
        filterOptions.put("Level", new TreeSet<>());
        filterOptions.put("Equipments", new TreeSet<>());

        addFilterOptions(cards.getCards());
    }

    public void addFilterOptions(HashMap<Integer, Card> cards){
        for (int cardId: cards.keySet()) {
            filterOptions.get("Event").add(cards.get(cardId).getEvent());
            filterOptions.get("Gender").add(cards.get(cardId).getGender());
            filterOptions.get("ModelSex").add(cards.get(cardId).getModelSex());
            filterOptions.get("Level").add(cards.get(cardId).getLevel());
            ArrayList<String> equipments = cards.get(cardId).getEquipment();
            for (String equipment : equipments) {
                if (equipment.contains("/")) {
                    for (String e : equipment.split("/")) {
                        addFormattedEquipment(e);
                    }
                } else {
                    addFormattedEquipment(equipment);
                }
            }
        }
    }

    private void addFormattedEquipment(String equipment){
        equipment = equipment.trim().replaceAll("\"", "");
        filterOptions.get("Equipments").add(toTitleCase(equipment));
    }

    public HashMap<String, TreeSet<String>> getFilterOptions() {
        return filterOptions;
    }
}
