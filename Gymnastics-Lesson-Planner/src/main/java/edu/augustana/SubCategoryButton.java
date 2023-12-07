package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SubCategoryButton {
    public boolean buttonClicked;
    public String subCategoryButtonName;
    public VBox buttonWrapper;
    public String buttonCategory;
    public Button mainButton;

    public SubCategoryButton(String subCategoryName, String categoryName){
        mainButton =  new Button(subCategoryName);;
        this.subCategoryButtonName = subCategoryName;
        buttonCategory = categoryName;
        buttonClicked = false;
        mainButton.setId("subCategoryButton");
        buttonWrapper = new VBox(mainButton);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setId(categoryName + "FilterOption");
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
    }

    public boolean isButtonClicked() {
        return buttonClicked;
    }

    public void click(){
        if (buttonClicked){
            buttonClicked = false;
            mainButton.setId("subCategoryButton");
            App.filteredData.remove(subCategoryButtonName);
            App.currentSelectedButtons.remove(this);
            if (App.currentSelectedButtons.isEmpty()){
                App.filteredData = FilterDatabase.allData;
            }
        }else{
            if (App.currentSelectedButtons.isEmpty()){
                App.resetFilteredData();
            }
            buttonClicked = true;
            mainButton.setId("subCategoryButtonClicked");
//            App.filteredData.put(subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get(buttonCategory).get(subCategoryButtonName));
            App.filteredData.get(buttonCategory).add(subCategoryButtonName);
            App.currentSelectedButtons.add(this);
        }
    }


    public String getButtonName() {
        return subCategoryButtonName;
    }

    public VBox getButtonWrapper() {
        return buttonWrapper;
    }

    public VBox getSubCategoryButtonWrapper(){
        return buttonWrapper;
    }


}
