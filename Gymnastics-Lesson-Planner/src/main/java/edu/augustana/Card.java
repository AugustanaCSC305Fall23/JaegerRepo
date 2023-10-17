package edu.augustana;

public class Card {
    private String code;
    private String event;
    private String category;
    private String title;
    private String filePath;
    private String gender;
    private String modelSex;
    private String level;
    private String[] equipment;
    private String[] keywords;

    public Card(String[] csvData){
        this.code = csvData[0];
        this.event = csvData[1];
        this.category = csvData[2];
        this.title = csvData[3];
        this.filePath = App.imagesFilePath + "/" + csvData[4] + "/" + csvData[5];
        this.gender = csvData[6];
        this.modelSex = csvData[7];
        this.level = csvData[8];
        this.equipment = csvData[9].split(", ");
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
        return filePath;
    }

    public String getLevel() {
        return level;
    }

    public String[] getEquipment() {
        return equipment;
    }

    public String[] getKeywords() {
        return keywords;
    }
}
