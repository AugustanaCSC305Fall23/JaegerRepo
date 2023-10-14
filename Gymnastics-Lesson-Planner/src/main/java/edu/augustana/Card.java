package edu.augustana;

public class Card {
    private String code;
    private String event;
    private String category;
    private String title;
    private String filePath;
    private Character gender;
    private Character modelSex;
    private String level;
    private String[] equipment;
    private String[] keywords;

    public Card(String code, String event, String category, String title, String folder, String fileName, Character gender, Character modelSex, String level, String[] equipment, String[] keywords){
        this.code = code;
        this.event = event;
        this.category = category;
        this.title = title;
        this.filePath = folder + "/" + fileName;
        this.gender = gender;
        this.modelSex = modelSex;
        this.level = level;
        this.equipment = equipment;
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public Character getGender() {
        return gender;
    }

    public Character getModelSex() {
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
