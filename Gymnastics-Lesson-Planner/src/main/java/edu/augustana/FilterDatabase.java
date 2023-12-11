package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.*;

public class FilterDatabase {
    public static HashMap<String, List<String>> allData;
    private HashMap<String, TreeMap<String, Collection<CardView>>> filterOptions = new HashMap<>();

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

    public static void saveFilterDatabase(File logFile) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();

        String serializedCourseLogText = gson.toJson(App.getFilterDatabase().getFilterOptions());

        // Check if the serialized text is not null and logFile is not null before creating the PrintWriter
        if (serializedCourseLogText != null && logFile != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
                writer.println(serializedCourseLogText);
            } catch (IOException e) {
                // Handle or log the IOException
                e.printStackTrace();
            }
        } else {
            System.err.println("Failed to serialize the object to JSON or logFile is null.");
        }
    }

    public static FilterDatabase loadFromFile(File logFile) throws IOException {
        FileReader reader = new FileReader(logFile);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();
        return gson.fromJson(reader, FilterDatabase.class);
    }

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

    private void addToFilterOptions(String category, String subCategory, CardView card) {
        if (!filterOptions.get(category).containsKey(subCategory)) {
            filterOptions.get(category).put(subCategory, new HashSet<>());
        }
        if (!allData.get(category).contains(subCategory)) {
            allData.get(category).add(subCategory);
        }
        filterOptions.get(category).get(subCategory).add(card);
    }

    private void addFormattedEquipment(String equipment, CardView card) {
        equipment = equipment.trim().replaceAll("\"", "");
        addToFilterOptions("Equipments", equipment, card);
    }

    public HashMap<String, TreeMap<String, Collection<CardView>>> getFilterOptions() {
        return filterOptions;
    }
}
