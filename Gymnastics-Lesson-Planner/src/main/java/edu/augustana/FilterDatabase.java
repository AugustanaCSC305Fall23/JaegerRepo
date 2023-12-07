package edu.augustana;

import java.util.*;

public class FilterDatabase {
    private HashMap<String, TreeMap<String, Collection<CardView>>> filterOptions =  new HashMap<>();
    public static HashMap<String, List<String>> allData;

    public FilterDatabase(CardDatabase cards){
        filterOptions = new HashMap<>();
        filterOptions.put("Event", new TreeMap<>());
        filterOptions.put("Gender", new TreeMap<>());
        filterOptions.put("ModelSex", new TreeMap<>());
        filterOptions.put("Level", new TreeMap<>());
        filterOptions.put("Equipments", new TreeMap<>());

        allData = new HashMap<>();
        allData.put("Event", new ArrayList<>());
        allData.put("Gender", new ArrayList<>());
        allData.put("ModelSex", new ArrayList<>());
        allData.put("Level", new ArrayList<>());
        allData.put("Equipments", new ArrayList<>());

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

    private void addToFilterOptions(String category, String subCategory, Card card){
        if (!filterOptions.get(category).containsKey(subCategory)){
            filterOptions.get(category).put(subCategory, new HashSet<>());
        }
        if (!allData.get(category).contains(subCategory)) {
            allData.get(category).add(subCategory);
        }
        filterOptions.get(category).get(subCategory).add(new CardView(card));
    }

    private void addFormattedEquipment(String equipment, Card card){
        equipment = equipment.trim().replaceAll("\"", "");
        addToFilterOptions("Equipments", equipment, card);
    }

    public HashMap<String, TreeMap<String, Collection<CardView>>> getFilterOptions() {
        return filterOptions;
    }

}
