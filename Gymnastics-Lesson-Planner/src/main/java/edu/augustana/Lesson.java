package edu.augustana;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson{
    private final ArrayList<CardView> selectedCardViews;
    private final String lessonName;
    private static int TotalLessonIndex = 0;
    private final int lessonIndex;
    private ArrayList<String> equipments;
    public Lesson(String lessonName){
        equipments = new ArrayList<>();
        selectedCardViews = new ArrayList<>();
        this.lessonName = lessonName;
        Lesson.TotalLessonIndex++;
        lessonIndex = Lesson.TotalLessonIndex;
    }
    public ArrayList<CardView> getSelectedCardViews() {
        return selectedCardViews;
    }

    public boolean addData(CardView cardView){
        if (!selectedCardViews.contains(cardView)) {
            selectedCardViews.add(cardView);
            return true;
        }else{
            System.out.println("card already selected");
            return false;
        }
    }

    public boolean removeData(CardView cardView){
        if (selectedCardViews.contains(cardView)){
            selectedCardViews.remove(cardView);
            return true;
        }
        return false;
    }

    public int getLessonIndex() {
        return lessonIndex;
    }

    public static int getTotalLessonIndex() {
        return TotalLessonIndex;
    }

    public String getName() {
        return lessonName;
    }

    public boolean addEquipment(String newEquipment){
        if (equipments.contains(newEquipment)){
            return false;
        }
        equipments.add(newEquipment);
        return true;
    }

    public boolean removeEquipment(String equipment){
        if (equipments.contains(equipment)){
            equipments.remove(equipment);
            return true;
        }
        return false;
    }
    public ArrayList<String> getEquipments() {
        return equipments;
    }
}
