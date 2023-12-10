package edu.augustana;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.nio.file.*;

import java.io.*;
import java.util.*;

public class PrimaryController {
    public static final String borderColor = "grey";
    @FXML
    private VBox centerArea;
    @FXML
    private VBox selectCourseLessonVbox;
    @FXML
    private VBox allFilterOptions;
    @FXML
    private ListView<HBox> cardBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<HBox> equipmentsBox;
    @FXML
    private Label currLessonLabel;
    @FXML
    private Label currCourseLabel;
    @FXML
    private TextField searchValue;

    public static SelectOptionPopUp selectLessonPopUp;
    public static SelectOptionPopUp selectCoursePopUp;

    @FXML
    private void initialize() {
        loadCardsToGridView();
        initializeFilters();
        currLessonLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            resetEquipmentBox(App.getCurrentSelectedLesson().getEquipments());
            resetCardBox(App.getCurrentSelectedLesson().getSelectedCardViews());
            addCardsToHBoxToGrid(" ", App.getCurrentSelectedLesson());
        });
    }

    private void initializeFilters() {
        HashMap<String, TreeMap<String, Collection<CardView>>> filterOptions = App.getFilterOptions();
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
                    clickSubCategoryButton(subButton);
                });
                button.setOnMouseEntered(e->{
                    if (button.getId().equals("subCategoryButton")) {
                        button.setStyle("-fx-background-color: #79c3db;");
                    }
                });
                button.setOnMouseExited(e-> {
                    if(button.getId().equals("subCategoryButton")){
                        button.setStyle("-fx-background-color: #ADD8E6;");
                    }
                });
            }
            newCategory.getChildren().add(subCategoryWrapper);
            allFilterOptions.getChildren().add(newCategory);
        }
    }

    private void clickSubCategoryButton(SubCategoryButton subButton) {
        subButton.click();
        addCardsToHBoxToGrid(" ");
    }

    public VBox createCategoryButton(String categoryName) {
        Button button = new Button(categoryName);
        Tooltip.install(button, new Tooltip("Click to view all the filters."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(1000));
        }});
        button.setId("categoryButton");
        VBox buttonWrapper = new VBox(button);
        buttonWrapper.setId(categoryName);
        buttonWrapper.setAlignment(Pos.CENTER);


        button.setOnMouseEntered(e->{
            if (button.getId().equals("categoryButton")) {
                button.setStyle("-fx-background-color: #79c3db;");
            }
                });
        button.setOnMouseExited(e-> {
            if(button.getId().equals("categoryButton")){
                button.setStyle("-fx-background-color: #ADD8E6;");
            }
        });


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

    private void loadCardsToGridView() {
        addCardsToHBoxToGrid(" ");
    }
    private void addCardsToHBoxToGrid(String searchValue){
        addCardsToHBoxToGrid(searchValue, new Lesson("place holder"));
    }
    private void addCardsToHBoxToGrid(String searchValue, Lesson currLesson) {
        GridPane cardGrid = new GridPane();
        cardGrid.setVgap(10);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);


        ArrayList<String> loadedCards = new ArrayList<>();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(null);
        scrollPane.setContent(cardGrid);
        int currCol = 0;
        int currRow = 0;

        HBox row = new HBox();
        for (String category: App.filteredData.keySet()){
            for (String subCategory: App.filteredData.get(category)){
                for (CardView cardView: App.getFilterDatabase().getFilterOptions().get(category).get(subCategory)){
                    if (!loadedCards.contains(cardView.getCardId()) && cardView.getSearchString().contains(searchValue.toLowerCase())) {
                        if (currLesson.getCardIds().contains(cardView.getCardId())){
                            cardView.select();
                        }else{
                            cardView.unSelect();
                        }
                        if (currCol == 3) {
                            //creating a new HBox or row after 3 cards are added
                            cardGrid.add(row, 0, currRow);
                            row = new HBox();
                            currCol = 0;
                            currRow++;
                        }
                        row.setAlignment(Pos.CENTER);
                        row.setSpacing(50);
                        cardView.getAddButton().setOnMouseClicked(event -> {
                            try {
                                plusClicked(cardView);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        cardView.getCardImage().setOnMouseClicked(e -> {
                            magnifyImage(cardView.getCardImage());
                        });
                        row.getChildren().add(cardView.getCardView());
                        currCol++;
                        loadedCards.add(cardView.getCardId());
                    }
                }
            }
        }
        if (!row.getChildren().isEmpty()) {
            cardGrid.add(row, 0, currRow);
        }
    }

    private void plusClicked(CardView cardView) throws IOException {
        if (App.isCourseSelected()) {
            if (cardView.addButtonClicked()) {
                addCardToCardBox(cardView);
                resetEquipmentBox(App.getCurrentSelectedLesson().getEquipments());
            }else{
                removeCardFromCardBox(cardView);
                resetEquipmentBox(App.getCurrentSelectedLesson().getEquipments());
            }
        }else{
            showSelectCoursePlanPopUpWindow();
        }
    }

    public void resetEquipmentBox(ArrayList<String> equipments){
        while (!equipmentsBox.getItems().isEmpty()){
            equipmentsBox.getItems().remove(0);
        }
        for (String e: equipments){
            equipmentsBox.getItems().add(hBoxForListView(e));
        }
    }

    public void resetCardBox(ArrayList<CardView> cards){
        while (!cardBox.getItems().isEmpty()){
            cardBox.getItems().remove(0);
        }
        for (CardView cardView: cards){
            cardBox.getItems().add(new HBoxForListView(cardView));
        }
    }

    public void addCardToCardBox(CardView cardView){
        if (App.getCurrentSelectedLesson().addData(cardView)){
            resetCardBox(App.getCurrentSelectedLesson().getSelectedCardViews());
        }
    }

    public void removeCardFromCardBox(CardView cardView){
        if (App.getCurrentSelectedLesson().removeData(cardView)) {
            resetCardBox(App.getCurrentSelectedLesson().getSelectedCardViews());
        }
    }

    private static HBox hBoxForListView(String e){
        Label label = new Label(e);
        HBox wrapper = new HBox(3);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(3, 3, 3, 3));
        wrapper.setStyle("-fx-border-color: black");
        wrapper.getChildren().add(label);
        return wrapper;
    }
    @FXML
    public String saveAsCourseAction() throws IOException {
        if (App.isCourseSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save the course");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Course (*.course)", "*.course");
            fileChooser.getExtensionFilters().add(filter);
            fileChooser.setInitialFileName(App.getCurrentSelectedCourse().getName());
            Window mainWindow = equipmentsBox.getScene().getWindow();
            File chosenFile = fileChooser.showSaveDialog(mainWindow);

            String fileName = App.removeFileExtension(chosenFile.getName());
            if (!fileName.equals(App.getCurrentSelectedCourse().getName())){
                App.getCurrentSelectedCourse().setCourseName(fileName);
                currCourseLabel.setText(fileName);
            }

            App.historyPaths.put(fileName, chosenFile.getAbsolutePath());
            App.saveCourseHistory();
            App.saveCurrentCourseLogToFile(chosenFile);
            return fileName;
        }else{
            showSelectCoursePlanPopUpWindow();
            return "";
        }
    }

    @FXML
    private void saveCourseAction() throws IOException {
        if (App.currentLoadedCourseFile == null){
            saveAsCourseAction();
        }else {
            Files.deleteIfExists(App.currentLoadedCourseFile.toPath());
            App.saveCurrentCourseLogToFile(App.currentLoadedCourseFile);
        }
    }

    @FXML
    private void printLessonAction(){
        App.launchPrinting();
    }

    @FXML
    private void showSelectCoursePlanPopUpWindow(){
        selectCoursePopUp = new SelectOptionPopUp("Course");
        selectLessonPopUp = selectCoursePopUp.initializeCourseInWindow(App.getCourses());
        selectCoursePopUp.getPopUpWindow().show();
    }

    @FXML
    private void showSelectLessonPlanPopUpWindow(){
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
    @FXML
    private Button createlessonButton;
    @FXML
    private Button createcourseButton;

    @FXML
    private void createCourseButtonMouseHover(){
        createcourseButton.setStyle("-fx-background-color:  #039c83;" +
                "    -fx-text-fill: white;" +
                " -fx-background-radius: 20;");
    }
    @FXML
    private void createCourseButtonMouseExit(){
        createcourseButton.setStyle("-fx-background-color:  #69d1c0;" +
                "    -fx-text-fill: white;"+
                " -fx-background-radius: 20;");
    }

    @FXML
    private void createlessonButtonMouseHover(){
        createlessonButton.setStyle("-fx-background-color:  #0591a3;" +
                "    -fx-text-fill: white;" +
                " -fx-background-radius: 20;");
    }

    @FXML
    private void createlessonButtonMouseExit(){
        createlessonButton.setStyle("-fx-background-color:  #69c5d1;" +
                "    -fx-text-fill: white;"+
                " -fx-background-radius: 20;");
    }

    @FXML
    private Button print;
    @FXML
    private Button saveAsLessonButton;
    @FXML
    private Button saveLessonButton;

    @FXML
    private void hoverFeatures(){
        print.setOnMouseEntered(e-> print.setStyle("-fx-background-color:   #69d1c0;"));
        print.setOnMouseExited(e->print.setStyle("-fx-background-color:   #ADD8E6;"));
        saveLessonButton.setOnMouseEntered(e-> saveLessonButton.setStyle("-fx-background-color:   #69d1c0;"));
        saveLessonButton.setOnMouseExited(e->saveLessonButton.setStyle("-fx-background-color:   #ADD8E6;"));
        saveAsLessonButton.setOnMouseEntered(e-> saveAsLessonButton.setStyle("-fx-background-color:   #69d1c0;"));
        saveAsLessonButton.setOnMouseExited(e->saveAsLessonButton.setStyle("-fx-background-color:   #ADD8E6;"));
    }

    @FXML
    private void searchClicked(){
        addCardsToHBoxToGrid(searchValue.getText());
    }
}