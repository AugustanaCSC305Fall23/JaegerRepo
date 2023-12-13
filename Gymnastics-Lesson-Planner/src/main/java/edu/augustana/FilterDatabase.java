package edu.augustana;

import java.util.*;

public class FilterDatabase {
    public static HashMap<String, List<String>> allData;
    private final HashMap<String, TreeMap<String, Collection<CardView>>> filterOptions;

    /**
     * Constructor for FilterDatabase object
     */
    public FilterDatabase() {
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
    }

    /**
     * Adds filter options to the filter database
     * @param cards: HashMap of CardView objects
     */
    public void addFilterOptions(HashMap<Integer, CardView> cards) {
        for (int cardId : cards.keySet()) {
            CardView currCard = cards.get(cardId);
            String event = cards.get(cardId).getEvent();
            String gender = cards.get(cardId).getGender();
            String modelSex = cards.get(cardId).getModelSex();

            if (event.equalsIgnoreCase("all")) {
                for (String subCategory : filterOptions.get("Event").keySet()) {
                    addToFilterOptions("Event", subCategory, currCard);
                }
            } else {
                addToFilterOptions("Event", event, currCard);
            }

            if (gender.equalsIgnoreCase("n")) {
                addToFilterOptions("Gender", "M", currCard);
                addToFilterOptions("Gender", "F", currCard);
            } else {
                addToFilterOptions("Gender", gender, currCard);
            }
            if (modelSex.equalsIgnoreCase("n")) {
                addToFilterOptions("ModelSex", "M", currCard);
                addToFilterOptions("ModelSex", "F", currCard);
            } else {
                addToFilterOptions("ModelSex", modelSex, currCard);
            }

            for (String l : cards.get(cardId).getLevels()) {
                if (l.equalsIgnoreCase("all")) {
                    for (String subCategory : filterOptions.get("Level").keySet()) {
                        addToFilterOptions("Level", subCategory, currCard);
                    }
                } else {
                    addToFilterOptions("Level", l, currCard);
                }

            }
            ArrayList<String> equipments = cards.get(cardId).getEquipments();
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
    }

    /**
     * Adds a card to the filter options
     * @param category: String of the category to add to
     * @param subCategory: String of the subcategory to add to
     * @param card: CardView object to add to the filter options
     */
    private void addToFilterOptions(String category, String subCategory, CardView card) {
        if (!filterOptions.get(category).containsKey(subCategory)) {
            filterOptions.get(category).put(subCategory, new HashSet<>());
        }
        if (!allData.get(category).contains(subCategory)) {
            allData.get(category).add(subCategory);
        }
        filterOptions.get(category).get(subCategory).add(card);
    }

    /**
     * Adds a formatted equipment to the filter options
     * @param equipment: String of the equipment to add
     * @param card: CardView object to add to the filter options
     */
    private void addFormattedEquipment(String equipment, CardView card) {
        equipment = equipment.trim().replaceAll("\"", "");
        addToFilterOptions("Equipments", equipment, card);
    }

    public HashMap<String, TreeMap<String, Collection<CardView>>> getFilterOptions() {
        return filterOptions;
    }
}
