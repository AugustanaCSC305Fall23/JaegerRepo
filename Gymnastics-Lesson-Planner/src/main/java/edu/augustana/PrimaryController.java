package edu.augustana;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class PrimaryController {
    public static final String borderColor = "grey";
    @FXML
    private VBox centerArea;
    @FXML
    private ComboBox<String> pickCourseComboBox;
    @FXML
    private VBox allFilterOptions;
    @FXML
    private VBox cardBox;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void initialize(){
        loadCardsToGridView();
        initializeFilters();
        initializeDropdowns();

    }

    private void initializeDropdowns(){
        pickCourseComboBox.getItems().addAll("+ Create New Course");
        HashMap<String, Lesson> lessons = App.getLessons();
        for (String id : lessons.keySet()){
            pickCourseComboBox.getItems().add(lessons.get(id).getLessonName());
        }
        pickCourseComboBox.setOnAction(event -> {
            try {
                courseDropdownOptionSelected();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void courseDropdownOptionSelected() throws IOException {
        String selectedOption = pickCourseComboBox.getValue();
        if (selectedOption.equals("+ Create New Course")){
            showAddLessonPlanPopUpWindow();
        } else {
            changeSelectedLesson(App.getLessons().get(selectedOption));
        }
    }

    private void initializeFilters(){
        HashMap<String, TreeMap <String, HashSet<Card>>> filterOptions = App.getFilterOptions();
        allFilterOptions.setAlignment(Pos.CENTER);
        for (String category : filterOptions.keySet()){
            VBox newCategory = new VBox();
            newCategory.setAlignment(Pos.CENTER);
            newCategory.getChildren().add(createCategoryButton(category));
            VBox subCategoryWrapper = new VBox();
            subCategoryWrapper.setAlignment(Pos.CENTER);
            subCategoryWrapper.setId(category + "FilterOptions");
            subCategoryWrapper.setVisible(false);
            subCategoryWrapper.managedProperty().bind(subCategoryWrapper.visibleProperty());
            for (String subCategory : filterOptions.get(category).keySet()){

                SubCategoryButton subButton = new SubCategoryButton(subCategory,category);

                subCategoryWrapper.getChildren().add(subButton.getSubCategoryButtonWrapper());
                Button button = (Button) subButton.getSubCategoryButtonWrapper().getChildren().get(0);
                button.setOnMouseClicked(event -> {
                    ClickSubCategoryButton(subButton, button);});
            }
            newCategory.getChildren().add(subCategoryWrapper);
            allFilterOptions.getChildren().add(newCategory);

        }
    }
    private void ClickSubCategoryButton(SubCategoryButton subButton, Button button) {
        if (!subButton.isButtonClicked) {
            subButton.on();
            addFilteredData();
        }else{
            subButton.off();
            scrollPane.setContent(null);
            scrollPane.setContent(App.allCardsLoadedGridPane);
        }
    }

    private void addFilteredData(){
        if (App.equipmentFilterValue != null){
            App.filteredData.put(App.equipmentFilterValue.subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get("Equipments").get(App.equipmentFilterValue.subCategoryButtonName));
        }
        if (App.eventFilterValue != null){
            App.filteredData.put(App.eventFilterValue.subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get("Event").get(App.eventFilterValue.subCategoryButtonName));
        }
        if (App.genderFilterValue != null){
            App.filteredData.put(App.genderFilterValue.subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get("Gender").get(App.genderFilterValue.subCategoryButtonName));
        }
        if (App.levelFilterValue != null){
            App.filteredData.put(App.levelFilterValue.subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get("Level").get(App.levelFilterValue.subCategoryButtonName));
        }
        if (App.modelSexFilterValue != null){
            App.filteredData.put(App.modelSexFilterValue.subCategoryButtonName, App.getFilterDatabase().getFilterOptions().get("ModelSex").get(App.modelSexFilterValue.subCategoryButtonName));
        }
        addCardsToHBoxToGrid(new GridPane());
    }

    public VBox createCategoryButton(String categoryName){
        Button button = new Button(categoryName);
        button.setId("categoryButton");
        VBox buttonWrapper = new VBox(button);
        buttonWrapper.setId(categoryName);
        buttonWrapper.setAlignment(Pos.CENTER);
        button.setOnMouseClicked(event -> {
            changeVisibilityOfFilterOptions(button);
            if (button.getId().equals("categoryButton")){
                button.setId("categoryButtonClicked");
            }else {button.setId("categoryButton");}
        });
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
        return buttonWrapper;
    }

    private void changeVisibilityOfFilterOptions(Button button){
        Scene scene = button.getScene();
        VBox filterOptions = (VBox) scene.lookup("#" + button.getText()+"FilterOptions");
        filterOptions.setVisible(!filterOptions.visibleProperty().getValue());
    }

    private Label cardTitleBox(Card card){
        String cardTitle = card.getTitle();
        Label titleLabel = new Label(cardTitle);
        titleLabel.setStyle("-fx-border-color:" + borderColor + "; -fx-padding: 5px;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPadding(new Insets(10, 0, 0, 0));

        return titleLabel;
    }

    private void loadCardsToGridView(){
        scrollPane.setFitToWidth(true);

        GridPane cardGrid = new GridPane(); // creating a gridPane
        cardGrid.setVgap(10);

        // centers the grid pane
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);

        // adding the gridPane in the scroll pane and the scroll pane into the center area

        addCardsToHBoxToGrid(cardGrid);
    }

    private void addCardsToHBoxToGrid(GridPane cardGrid) {
        scrollPane.setContent(null);
        scrollPane.setContent(cardGrid);
        // indexes to keep track of the location in grid
        int currCol = 0;
        int currRow = 0;
        // getting the card hashmap that was created during the initial execution
        HashMap<Integer, Card> cards = App.getCardDatabase();

        HBox row = new HBox(); // using a HBox cause organizing the grid columns was a hassle so there is only one column, and it has a HBox that holds 3 cards

        if (App.filteredData.isEmpty()) {
            // looping through the cards hashmap and adding it to the grid
            for (int cardId : cards.keySet()) {


                if (currCol == 3) {
                    //creating a new HBox or row after 3 cards are added
                    cardGrid.add(row, 0, currRow);
                    row = new HBox();

                    currCol = 0;
                    currRow++;
                }
                // adding a card to the HBox or row
                row.setAlignment(Pos.CENTER);
                row.setSpacing(50);
                CardView newCardView = new CardView(cards.get(cardId));
                newCardView.getButtonAndCode().setOnMouseClicked(event -> {
                    try {
                        plusClicked(newCardView);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                row.getChildren().add(newCardView.getCardView());
                currCol++;


            }
            if (!row.getChildren().isEmpty()) {
                cardGrid.add(row, 0, currRow);
            }
            if (App.allCardsLoadedGridPane == null){
                App.allCardsLoadedGridPane = cardGrid;
            }

        } else {
            for (String subCategoryName: App.filteredData.keySet()) {
                for (Card card: App.filteredData.get(subCategoryName)) {
                    System.out.println(card);
                    if (currCol == 3) {
                        //creating a new HBox or row after 3 cards are added
                        cardGrid.add(row, 0, currRow);
                        row = new HBox();

                        currCol = 0;
                        currRow++;
                    }
                    // adding a card to the HBox or row
                    row.setAlignment(Pos.CENTER);
                    row.setSpacing(50);
                    CardView newCardView = new CardView(card);
                    newCardView.getButtonAndCode().setOnMouseClicked(event -> {
                        try {
                            plusClicked(newCardView);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    row.getChildren().add(newCardView.getCardView());
                    currCol++;

                }
            }
            if (!row.getChildren().isEmpty()) {
                cardGrid.add(row, 0, currRow);

            }
        }
    }
//
//    public boolean isCardFiltered(int cardId){
//        if ()
//    }

    private void plusClicked(CardView cardView) throws IOException {
        if (App.isLessonSelected()){
            addCardToLesson(cardView.getCardId(), false);
        }else{
            showAddLessonPlanPopUpWindow();
        }
    }

    public void addCardToLesson(int code, boolean forceAdd){
        Card cardToAdd = App.getCardDatabase().get(code);
        boolean added = App.getCurrentSelectedLesson().addCard(cardToAdd);
        if (added || forceAdd) {
            cardBox.getChildren().add(cardTitleBox(cardToAdd));
        }
    }

    private void showAddLessonPlanPopUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addLessonPlanPopUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Add to lesson");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void changeSelectedLesson(Lesson lesson){
        App.setCurrentSelectedLesson(lesson);
        while (!cardBox.getChildren().isEmpty()) {
            cardBox.getChildren().remove(0);
        }
        for (int index : lesson.getCardIndexes()){
            addCardToLesson(index, true);
        }
    }
    public VBox getCardBox(){
        return cardBox;
    }

}
