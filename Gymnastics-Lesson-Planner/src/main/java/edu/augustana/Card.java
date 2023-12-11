package edu.augustana;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Card {
    private final String code;
    private final String event;
    private final String category;
    private final String title;
    private final URL filePath;
    private final String gender;
    private final String modelSex;
    private final String[] levels;
    private final String[] equipments;
    private final String[] keywords;
    private final int cardId;
    private static int cardIndex = 0;
    private String thumbnailFilePath;
    private String dir;
    private String fileName;

    public Card(String[] csvData, String newDirName){
        this.code = csvData[0];
        this.event = csvData[1];
        this.category = csvData[2];
        this.title = csvData[3];
        String folderFilePath = "staticFiles/CardData/" + newDirName + "/";
        this.filePath = App.class.getResource(folderFilePath + csvData[5]);
        this.thumbnailFilePath = Objects.requireNonNull(App.class.getResource(folderFilePath + "thumbs/" + App.removeFileExtension(csvData[5]) + ".jpg").toExternalForm());
        System.out.println(newDirName + "/" + csvData[5]);
        this.gender = csvData[6];
        this.modelSex = csvData[7];
        this.levels = csvData[8].split(" ");
        this.equipments = csvData[9].split(", ");
        this.keywords = csvData[10].split(", ");
        cardId = cardIndex;
        cardIndex++;
    }

    public String getThumbnailFilePath() {
        return thumbnailFilePath;
    }
    public int getCardId() {
        return cardId;
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

    public ArrayList<String> getLevels() {
        return new ArrayList<>(Arrays.asList(levels));
    }

    public ArrayList<String> getEquipment() {
        ArrayList<String> equipmentsAsList = new ArrayList<>();
        for (String equipment : equipments) {
            if (equipment.contains("/")) {
                for (String e : equipment.split("/")) {
                    e = e.trim().replaceAll("[\"]", "");
                    equipmentsAsList.add(App.toTitleCase(e));
                }
            } else {
                equipment = equipment.trim().replaceAll("[\"]", "");
                equipmentsAsList.add(App.toTitleCase(equipment));
            }
        }
        return equipmentsAsList;
    }

    public String[] getKeywords() {
        return keywords;
    }
}

