package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class SubCategoryButton {
    public boolean isButtonClicked;
    public String buttonName;
    public VBox buttonWrapper;


    public boolean isButtonClicked() {
        return isButtonClicked;
    }

    public void setButtonClicked(boolean buttonClicked) {
        isButtonClicked = buttonClicked;
    }

    public String getButtonName() {
        return buttonName;
    }

    public VBox getButtonWrapper() {
        return buttonWrapper;
    }

    public SubCategoryButton(String subCategoryName, String categoryName){
        javafx.scene.control.Button button = new javafx.scene.control.Button(subCategoryName);
        button.setId("subCategoryButton");
        button.setOnMouseClicked(event -> {
            if (button.getId().equals("subCategoryButton")){
                button.setId("subCategoryButtonClicked");
            }else {button.setId("subCategoryButton");}
        });
        buttonWrapper = new VBox(button);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setId(categoryName + "FilterOption");
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
    }
    public VBox getSubCategoryButtonWrapper(){
        return buttonWrapper;
    }


}
