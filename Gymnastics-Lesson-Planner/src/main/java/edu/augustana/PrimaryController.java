package edu.augustana;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class PrimaryController {
    public static final String borderColor = "grey";
    @FXML
    private VBox centerArea;
    @FXML
    private VBox selectCourseLessonVbox;
    
    @FXML
    private VBox allFilterOptions;
    @FXML
    private ListView<String> cardBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<String> equipmentsBox;
    @FXML
    private Label currLessonLabel;
    @FXML
    private Label currCourseLabel;
    public static SelectOptionPopUp selectLessonPopUp;
    public static SelectOptionPopUp selectCoursePopUp;

    @FXML
    private void initialize() {
        loadCardsToGridView();
        initializeFilters();
    }

    private void initializeFilters() {
        HashMap<String, TreeMap<String, HashSet<CardView>>> filterOptions = App.getFilterOptions();
        allFilterOptions.setAlignment(Pos.CENTER);
        for (String category : filterOptions.keySet()) {
            VBox newCategory = new VBox();
            newCategory.setAlignment(Pos.CENTER);
            newCategory.getChildren().add(createCategoryButton(category));
            VBox subCategoryWrapper = new VBox();
            subCategoryWrapper.setAlignment(Pos.CENTER);
            subCategoryWrapper.setId(category + "FilterOptions");
            subCategoryWrapper.setVisible(false);
            subCategoryWrapper.managedProperty().bind(subCategoryWrapper.visibleProperty());
            for (String subCategory : filterOptions.get(category).keySet()) {
                SubCategoryButton subButton = new SubCategoryButton(subCategory, category);
                subCategoryWrapper.getChildren().add(subButton.getSubCategoryButtonWrapper());
                Button button = (Button) subButton.getSubCategoryButtonWrapper().getChildren().get(0);
                button.setOnMouseClicked(event -> {
                    clickSubCategoryButton(subButton, button);
                });
            }
            newCategory.getChildren().add(subCategoryWrapper);
            allFilterOptions.getChildren().add(newCategory);

        }
    }

    private void clickSubCategoryButton(SubCategoryButton subButton, Button button) {
        subButton.click();
        addCardsToHBoxToGrid(new GridPane());
    }

    public VBox createCategoryButton(String categoryName) {
        Button button = new Button(categoryName);
        button.setId("categoryButton");
        VBox buttonWrapper = new VBox(button);
        buttonWrapper.setId(categoryName);
        buttonWrapper.setAlignment(Pos.CENTER);
        button.setOnMouseClicked(event -> {
            changeVisibilityOfFilterOptions(button);
            if (button.getId().equals("categoryButton")) {
                button.setId("categoryButtonClicked");
            } else {
                button.setId("categoryButton");
            }
        });
        VBox.setMargin(buttonWrapper, new Insets(0, 0, 10, 0));
        return buttonWrapper;
    }

    private void changeVisibilityOfFilterOptions(Button button) {
        Scene scene = button.getScene();
        VBox filterOptions = (VBox) scene.lookup("#" + button.getText() + "FilterOptions");
        filterOptions.setVisible(!filterOptions.visibleProperty().getValue());
    }

    private Label cardTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-border-color:" + borderColor + "; -fx-padding: 5px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPadding(new Insets(10, 0, 0, 0));

        return titleLabel;
    }

    private void loadCardsToGridView() {
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
            if (App.allCardsLoadedGridPane == null) {
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
                    newCardView.getCardImage().setOnMouseClicked(e -> {magnifyImage(newCardView.getCardImage());});

                    row.getChildren().add(newCardView.getCardView());
                    currCol++;

                }
                if (!row.getChildren().isEmpty()) {
                    cardGrid.add(row, 0, currRow);


                }
                if (App.allCardsLoadedGridPane == null) {
                    App.allCardsLoadedGridPane = cardGrid;
                }
            }else {
                scrollPane.setContent(App.allCardsLoadedGridPane);
            }

        } else {
            for (String subCategoryName : App.filteredData.keySet()) {
                for (CardView card : App.filteredData.get(subCategoryName)) {
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
                    card.getButtonAndCode().setOnMouseClicked(event -> {
                        try {
                            plusClicked(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    card.getCardImage().setOnMouseClicked(e->{magnifyImage(card.getCardImage());});


                    row.getChildren().add(card.getCardView());
                    currCol++;

                }
            }
            if (!row.getChildren().isEmpty()) {
                cardGrid.add(row, 0, currRow);

            }
        }
    }

    private void plusClicked(CardView cardView) throws IOException {
        if (App.isCourseSelected()) {
            addCardToCardBox(cardView.getCardId(), false);
            addEquipmentToEquipmentBox(cardView.getCardId(), false);
        }else{
            showSelectCoursePlanPopUpWindow();
        }
    }

    public void addEquipmentToEquipmentBox(int code, boolean forceAdd){
        Card equipmentToAdd = App.getCardDatabase().get(code);
        for (String e: equipmentToAdd.getEquipment()){
            if (App.getCurrentSelectedLesson().addEquipment(e) || forceAdd) {
                equipmentsBox.getItems().add(e);
//                equipmentsBox.getChildren().add(cardTitleBox(e));
            }
        }
    }

    public void addCardToCardBox(int code, boolean forceAdd){
        Card card = App.getCardDatabase().get(code);
        if (App.getCurrentSelectedLesson().addData(card) || forceAdd) {
                cardBox.getItems().add(card.getTitle());
            }
    }

    public void changeSelectedLessonEquipment(Lesson lesson){
        App.setCurrentSelectedLesson(lesson);
        while (!equipmentsBox.getItems().isEmpty()) {
            equipmentsBox.getItems().remove(0);
        }
        for (int index : lesson.getCardIndexes()){
            addEquipmentToEquipmentBox(index, true);
        }
    }

    @FXML
    private Button saveButton;

    @FXML
    public void saveCourseAction() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Course (*.course)", "*.course");
        fileChooser.getExtensionFilters().add(filter);
        Window mainWindow = equipmentsBox.getScene().getWindow();
        File chosenFile = fileChooser.showSaveDialog(mainWindow);
//        chosenFile.createNewFile();
        System.out.println(chosenFile.getAbsolutePath());
        App.saveCurrentCourseLogToFile(chosenFile);
    }


    @FXML
    private void showSelectCoursePlanPopUpWindow(){
        selectCoursePopUp = new SelectOptionPopUp("Course");
        selectLessonPopUp = selectCoursePopUp.initializeCourseInWindow(App.getCourses());
        selectCoursePopUp.getPopUpWindow().show();
    }

    @FXML
    private void showSelectLessonPlanPopUpWindow(){
        cardBox.getItems().removeAll();
        equipmentsBox.getItems().removeAll();
        if (App.isCourseSelected()) {
            selectLessonPopUp.getoptionsVBox().getChildren().removeAll();
            selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
            selectLessonPopUp.getPopUpWindow().show();
        }else {
            showSelectCoursePlanPopUpWindow();
        }
    }

    @FXML
    private void magnifyImage(ImageView image){
        ImageView newImage = new ImageView(image.getImage());
        Stage popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(App.primaryStage);
        VBox contentVBox = new VBox();
        contentVBox.setAlignment(Pos.CENTER);
        newImage.setFitWidth(600);
        newImage.setPreserveRatio(true);
//        newImage.preserveRatioProperty();
        contentVBox.getChildren().add(newImage);
        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);
        popUpWindow.show();

    }
}
