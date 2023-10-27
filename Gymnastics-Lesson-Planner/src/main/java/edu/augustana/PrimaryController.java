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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class PrimaryController {

    @FXML
    private BorderPane window;
    @FXML
    private VBox centerArea;
    @FXML
    private VBox selectedArea;
    @FXML
    private VBox filters;
    @FXML
    private ComboBox<String> pickCourseComboBox;
    @FXML
    private ScrollPane filterScrollPane;
    @FXML
    private VBox allOptions;
    @FXML
    private void initialize() throws IOException {
        initializeDropdowns();
        loadImagesToGridView();
        initializefilters();
        loadSelectedCardTitle();
    }

    private void initializeDropdowns(){
        pickCourseComboBox.getItems().addAll("+ Create New Course");
        pickCourseComboBox.setOnAction(event -> {
            try {
                courseDropdownOptionSelected();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void courseDropdownOptionSelected() throws IOException {
        if (pickCourseComboBox.getValue().equals("+ Create New Course")){
            showAddLessonPlanPopUpWindow();
        }
    }

    private void initializefilters(){
        HashMap<String, TreeSet<String>> filterOptions = App.getFilterOptions();
        allOptions.setAlignment(Pos.CENTER);
        for (String category : filterOptions.keySet()){
            VBox newCategory = new VBox();
            newCategory.setAlignment(Pos.CENTER);
            newCategory.getChildren().add(createCategoryButton(category));
            VBox subCategoryWrapper = new VBox();
            subCategoryWrapper.setAlignment(Pos.CENTER);
            subCategoryWrapper.setId(category + "FilterOptions");
            subCategoryWrapper.setVisible(false);
            subCategoryWrapper.managedProperty().bind(subCategoryWrapper.visibleProperty());
            for (String subCategory : filterOptions.get(category)){
                subCategoryWrapper.getChildren().add(createSubCategoryButton(subCategory, category));
            }
            newCategory.getChildren().add(subCategoryWrapper);
            allOptions.getChildren().add(newCategory);

        }
    }
//    private void loadSelectedCardTitle() throws IOException{
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setFitToWidth(true);
//
//        GridPane cardGrid = new GridPane();
//        ColumnConstraints colConstraints = new ColumnConstraints();
//        colConstraints.setFillWidth(true);
//        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
//        cardGrid.getColumnConstraints().add(colConstraints);
//        scrollPane.setContent(cardGrid);
//        selectedArea.getChildren().add(scrollPane);
//
//        for (String cardId : App.currentSelectedLesson.getCardIndexes()){
//
//        }
//        addSelectedCardTitleToGrid(cardGrid);
//
//    }

    private void loadSelectedCardTitle() throws IOException {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox cardBox = new VBox();
        cardBox.setSpacing(4);
        cardBox.setFillWidth(true);

        scrollPane.setContent(cardBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        selectedArea.getChildren().add(scrollPane);

//        for (String cardId : App.currentSelectedLesson.getCardIndexes()) {
//        }

        addSelectedCardTitleToBox(cardBox);
    }

    private VBox createCategoryButton(String categoryName){
        Button button = new Button(categoryName);
        button.setId("categoryButton");
        VBox buttonWrapper = new VBox(button);
        buttonWrapper.setId(categoryName);
        buttonWrapper.setAlignment(Pos.CENTER);
        button.setOnMouseClicked(event -> {changeVisibilityOfFilterOptions(button);});
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
        return buttonWrapper;
    }

    private void changeVisibilityOfFilterOptions(Button button){
        Scene scene = button.getScene();
        VBox filterOptions = (VBox) scene.lookup("#" + button.getText()+"FilterOptions");
        filterOptions.setVisible(!filterOptions.visibleProperty().getValue());
    }

//    private void addSelectedCardTitleToGrid(GridPane cardGrid) {
//        int currCol = 0;
//        int currRow = 0;
//
//        HashMap<String, Card> cards = App.getCardHashMap();
//
//        for (String cardId : cards.keySet()) {
//            if (currCol == 1) {
//                currCol = 0;
//                currRow++;
//            }
//
//            Label titleLabel = new Label(cards.get(cardId).getTitle());
//            titleLabel.setStyle("-fx-border-color: grey; -fx-padding: 5px;");
//            titleLabel.setAlignment(Pos.CENTER);
//            titleLabel.setPadding(new Insets(10, 0, 0, 0));
//
//            cardGrid.add(titleLabel, currCol, currRow);
//
//            currCol++;
//        }
//    }

    private void addSelectedCardTitleToBox(VBox cardBox) {
        if (App.currentSelectedLesson !=null) {
            ArrayList<String> cards;
//            HashMap<String, Card> cards = App.getCardDatabase();

            if (App.currentSelectedLesson.getCardIndexes() == null) {
                cards = new ArrayList<String>();
            } else {
                cards = App.currentSelectedLesson.getCardIndexes();
            }


            for (String cardId : cards) {
                Card card = App.getCardDatabase().get(cardId);
                if (card != null) {
                    String cardTitle = card.getTitle();
                    Label titleLabel = new Label(cardTitle);
                    titleLabel.setStyle("-fx-border-color: grey; -fx-padding: 5px;");
                    titleLabel.setAlignment(Pos.CENTER);
                    titleLabel.setPadding(new Insets(10, 0, 0, 0));

                    cardBox.getChildren().add(titleLabel);
                }
            }
        }



//        for (String cardId : cardz.keySet()) {
//            Label titleLabel = new Label(cardz.get(cardId).getTitle());
//            titleLabel.setStyle("-fx-border-color: grey; -fx-padding: 5px;");
//            titleLabel.setAlignment(Pos.CENTER);
//            titleLabel.setPadding(new Insets(10, 0, 0, 0));
//
//            cardBox.getChildren().add(titleLabel);
//        }
    }
    private VBox createSubCategoryButton(String subCategoryName, String categoryName){
        Button button = new Button(subCategoryName);
        button.setId("subCategoryButton");
        VBox buttonWrapper = new VBox(button);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setId(categoryName + "FilterOption");
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
        return buttonWrapper;
    }

    private void loadImagesToGridView() throws IOException {
        ScrollPane scrollPane = new ScrollPane(); // creating a scroll pane
        scrollPane.setFitToWidth(true);

        GridPane cardGrid = new GridPane(); // creating a gridpane

        // centers the grid pane
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);

        // adding the gridpane in the scroll pane and the scroll pane into the center area
        scrollPane.setContent(cardGrid);
        centerArea.getChildren().add(scrollPane);

        addCardsToHBoxToGrid(cardGrid);
    }

    private void addCardsToHBoxToGrid(GridPane cardGrid) throws FileNotFoundException {
        // indexes to keep track of the location in grid
        int currCol = 0;
        int currRow = 0;

        // getting the card hashmap that was created during the initial execution
        HashMap<String, Card> cards = App.getCardDatabase();

        HBox row = new HBox(); // using a hbox cause organizng the grid columns was a hassle so there is only one column and it has a hbox that holds 3 cards

        // looping through the cards hashmap and adding it to the grid
        for (String cardId : cards.keySet()){
            if (currCol == 3){
                //creating a new hbox or row after 3 cards are added
                row.setAlignment(Pos.CENTER);
                addHBoxToGrid(cardGrid, row, currRow);
                row = new HBox();
                currCol = 0;
                currRow++;
            }
            // adding a card to the hbox or row
            row.getChildren().add(cardAndButtonForGrid(cards, cardId));
            currCol++;
        }
    }

    /**
     * @param cards
     * @param cardId
     * @returns a card wrapped in a vbox for margin reasons
     * @throws FileNotFoundException
     */
    private VBox createCardForGrid(HashMap<String, Card> cards, String cardId) throws FileNotFoundException {
        ImageView cardImage = new ImageView();
        cardImage.setImage(new Image(cards.get(cardId).getFilePath()));
        cardImage.setPreserveRatio(true);
        cardImage.setFitWidth(250);

        VBox cardImageWrapper = new VBox(cardImage);
        VBox.setMargin(cardImageWrapper, new Insets(0, 0, 10, 0));

        return cardImageWrapper;
    }

    /**
     * @returns a plus button wrapped in a vbox for margin reasons
     * @throws FileNotFoundException
     */
    private VBox createPlusButtonForGrid() throws FileNotFoundException {
        ImageView plusButton = new ImageView();
        plusButton.setImage(new Image(App.imagesFilePath + "/images/add.png"));

        plusButton.setPreserveRatio(true);
        plusButton.setFitWidth(30);
        plusButton.setFitHeight(30);
        VBox plusButtonWrapper = new VBox(plusButton);
        VBox.setMargin(plusButtonWrapper, new Insets(0, 30, 0, 0));
        plusButtonWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        plusButtonWrapper.setOnMouseClicked(event -> {
            try {
                plusClicked(plusButtonWrapper);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return plusButtonWrapper;
    }

    /**
     * @param cards
     * @param cardId
     * @returns a vbox wrapped plus button and card inside another vbox
     * @throws FileNotFoundException
     */
    private VBox cardAndButtonForGrid(HashMap<String, Card> cards, String cardId) throws FileNotFoundException {
        VBox imageAndButton = new VBox();
        imageAndButton.setPadding(new Insets(20, 20, 5, 20));

        imageAndButton.setId("imageAndButton");
        imageAndButton.getChildren().add(createCardForGrid(cards, cardId));
        HBox titleAndButton = new HBox();
        titleAndButton.setAlignment(Pos.CENTER);
        Label id = new Label(cards.get(cardId).getCode());
        VBox idWrapper = new VBox(id);
        idWrapper.setAlignment(Pos.CENTER);
        idWrapper.setPadding(new Insets(0, 200, 0, 0));
        titleAndButton.getChildren().add(idWrapper);
        titleAndButton.getChildren().add(createPlusButtonForGrid());
        imageAndButton.getChildren().add(titleAndButton);
        imageAndButton.setAlignment(Pos.TOP_RIGHT);

        return imageAndButton;
    }

    private void addHBoxToGrid(GridPane grid,HBox row, int rowNum){
        grid.add(row, 0, rowNum);
    }

    @FXML
    private void plusClicked(VBox plusButtonWrapper) throws IOException {
        HBox plusButtonParent = (HBox) plusButtonWrapper.getParent();
        VBox plusButtonParentParent = (VBox) plusButtonParent.getParent();
        HBox titleAndButtonHBox = (HBox) plusButtonParentParent.getChildren().get(1);
        String code = ((Label)((VBox)titleAndButtonHBox.getChildren().get(0)).getChildren().get(0)).getText();

        if (App.isLessonSelected()){
            App.addCardToLesson(code);
            System.out.println(code +" added in lesson "+ App.currentSelectedLesson.getLessonName());
        }else{
            showAddLessonPlanPopUpWindow();
//            App.addCardToLesson(code);
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
}
