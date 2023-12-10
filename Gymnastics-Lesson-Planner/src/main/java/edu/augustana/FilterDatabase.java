package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.*;

public class FilterDatabase {
    private HashMap<String, TreeMap<String, Collection<CardView>>> filterOptions =  new HashMap<>();
    public static HashMap<String, List<String>> allData;

    public FilterDatabase(){
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

    public void addFilterOptions(HashMap<Integer, Card> cards){
        for (int cardId: cards.keySet()) {
            Card currCard = cards.get(cardId);
            String event = cards.get(cardId).getEvent();
            String gender = cards.get(cardId).getGender();
            String modelSex = cards.get(cardId).getModelSex();


            addToFilterOptions("Event", event, currCard);
            addToFilterOptions("Gender", gender, currCard);
            addToFilterOptions("ModelSex", modelSex, currCard);

            for (String l: cards.get(cardId).getLevels()){
                addToFilterOptions("Level", l, currCard);
            }
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

    public static void saveFilterDatabase(File logFile){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ImageView.class, new ImageViewSerializer())
                .registerTypeAdapter(HBox.class, new HBoxSerializer())
                .create();

        String serializedCourseLogText = gson.toJson(App.getFilterDatabase().getFilterOptions());

        // Check if the serialized text is not null and logFile is not null before creating the PrintWriter
        if (serializedCourseLogText!= null && logFile != null) {
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
}
