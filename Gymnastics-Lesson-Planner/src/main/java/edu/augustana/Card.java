package edu.augustana;

import java.net.URL;
import java.util.ArrayList;

public class Card {
    private String code;
    private String event;
    private String category;
    private String title;
    private URL filePath;
    private String gender;
    private String modelSex;
    private String level;
    private String[] equipments;
    private String[] keywords;

    public Card(String[] csvData){
        this.code = csvData[0];
        this.event = csvData[1];
        this.category = csvData[2];
        this.title = csvData[3];
//        this.filePath = App.imagesFilePath + "/" + csvData[4] + "/" + csvData[5];
        this.filePath = App.class.getResource("staticFiles/" + csvData[4] + "/" + csvData[5]);

        this.gender = csvData[6];
        this.modelSex = csvData[7];
        this.level = csvData[8];
        this.equipments = csvData[9].split(", ");
        this.keywords = csvData[10].split(", ");
    }

    public String getTitle() {
        return title;
    }

    public String getGender() {
        return gender;
    }

    public String getModelSex() {
        return modelSex;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getEvent() {
        return event;
    }

    public String getFilePath() {
        return filePath.toExternalForm();
    }

    public String getLevel() {
        return level;
    }

    public ArrayList<String> getEquipment() {
        ArrayList<String> equipmentsAsList = new ArrayList<>();
        for (String equipment : equipments) {
            if (equipment.contains("/")) {
                for (String e : equipment.split("/")) {
                    e = e.trim().replaceAll("[\"]", "");
                    equipmentsAsList.add(e);
                }
            } else {
                equipment = equipment.trim().replaceAll("[\"]", "");
                equipmentsAsList.add(equipment);
            }
        }

        return equipmentsAsList;
    }

    public String[] getKeywords() {
        return keywords;
    }
}

