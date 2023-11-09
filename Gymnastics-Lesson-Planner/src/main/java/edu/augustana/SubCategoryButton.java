package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SubCategoryButton {
    public boolean isButtonClicked;
    public String subCategoryButtonName;
    public VBox buttonWrapper;
    public String buttonCategory;
    public Button mainButton;

    public boolean isButtonClicked() {
        return isButtonClicked;
    }

    public void on(){
        isButtonClicked = true;
        mainButton.setId("subCategoryButtonClicked");
        if (App.equipmentFilterValue != null){
            App.equipmentFilterValue.off();
        }
        if (App.eventFilterValue != null){
            App.eventFilterValue.off();
        }
        if (App.modelSexFilterValue != null){
            App.modelSexFilterValue.off();
        }
        if (App.levelFilterValue != null){
            App.levelFilterValue.off();
        }
        if (App.genderFilterValue != null){
            App.genderFilterValue.off();
        }

        switch (buttonCategory) {
            case "Equipments":
                App.equipmentFilterValue = this;
                break;
            case "Event":
                App.eventFilterValue = this;
                break;
            case "ModelSex":
                App.modelSexFilterValue = this;
                break;
            case "Level":
                App.levelFilterValue = this;
                break;
            case "Gender":
                App.genderFilterValue = this;
                break;
        }
    }

    public void off(){
        isButtonClicked = false;
        mainButton.setId("subCategoryButton");
        App.filteredData.remove(subCategoryButtonName);
    }

    public String getButtonName() {
        return subCategoryButtonName;
    }

    public VBox getButtonWrapper() {
        return buttonWrapper;
    }

    public SubCategoryButton(String subCategoryName, String categoryName){
        javafx.scene.control.Button button = new javafx.scene.control.Button(subCategoryName);
        mainButton = button;
        this.subCategoryButtonName = subCategoryName;
        buttonCategory = categoryName;
        button.setId("subCategoryButton");
        off();
        buttonWrapper = new VBox(button);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setId(categoryName + "FilterOption");
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
    }

    public VBox getSubCategoryButtonWrapper(){
        return buttonWrapper;
    }


}
