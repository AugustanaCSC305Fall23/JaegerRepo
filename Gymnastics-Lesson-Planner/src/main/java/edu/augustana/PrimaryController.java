package edu.augustana;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    private ListView<String> cardBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<String> equipmentsBox;
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
//        setBackground();
        loadCardsToGridView();
        initializeFilters();
    }


//    private void setBackground(){
//        Image backgroundImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/background.jpg")).toExternalForm());
//        BackgroundImage backgroundImg = new BackgroundImage(
//                backgroundImage,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
//
//        // Create a Background with the BackgroundImage
//        Background background = new Background(backgroundImg);
//        filters.setBackground(background);
//        searchHBox.setBackground(background);
//        allFilterOptions.setBackground(background);
//    }

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

    private void addCardsToHBoxToGrid(String searchValue) {
        GridPane cardGrid = new GridPane();
        cardGrid.setVgap(10);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);


        ArrayList<Integer> loadedCards = new ArrayList<>();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(null);
        scrollPane.setContent(cardGrid);
        int currCol = 0;
        int currRow = 0;

        HBox row = new HBox();
        for (String category: App.filteredData.keySet()){
            for (String subCategory: App.filteredData.get(category)){
                System.out.println(subCategory);
                for (CardView cardView: App.getFilterDatabase().getFilterOptions().get(category).get(subCategory)){
                    if (!loadedCards.contains(cardView.getCardId()) && cardView.getSearchString().contains(searchValue.toLowerCase())) {
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
                addEquipmentToEquipmentBox(cardView.getEquipments());
            }else{
                removeCardToCardBox(cardView);
                removeEquipmentToEquipmentBox(cardView.getEquipments());
            }
        }else{
            showSelectCoursePlanPopUpWindow();
        }
    }

    public void addEquipmentToEquipmentBox(ArrayList<String> equipments){
        for (String e: equipments){
            if (App.getCurrentSelectedLesson().addEquipment(e)) {
                equipmentsBox.getItems().add(e);
            }
        }
    }

    public void addCardToCardBox(CardView cardView){
        if (App.getCurrentSelectedLesson().addData(cardView)) {
                cardBox.getItems().add(cardView.getCardTitle());
            }
    }

    public void removeEquipmentToEquipmentBox(ArrayList<String> equipments){
        for (String e: equipments){
            if (App.getCurrentSelectedLesson().removeEquipment(e)) {
                equipmentsBox.getItems().remove(e);
            }
        }
    }

    public void removeCardToCardBox(CardView cardView){
        if (App.getCurrentSelectedLesson().removeData(cardView)) {
            cardBox.getItems().remove(cardView.getCardTitle());
        }
    }

    @FXML
    public void saveAsCourseAction() throws IOException {
        if (App.isCourseSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save the course");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Course (*.course)", "*.course");
            fileChooser.getExtensionFilters().add(filter);
            Window mainWindow = equipmentsBox.getScene().getWindow();
            File chosenFile = fileChooser.showSaveDialog(mainWindow);

            App.saveCurrentCourseLogToFile(chosenFile);
        }
    }

    @FXML
    private void saveCourseAction() throws IOException {
        File chosenFile = App.currentLoadedCourseFile;

        // Create a new file in the same directory as the original file
        try {
            File newFile = new File(chosenFile.getParent(), "new_" + chosenFile.getName());

        // Save the current course log to the new file
        App.saveCurrentCourseLogToFile(newFile);
        if (chosenFile.exists() && newFile.exists()) {
            Path oldPath = chosenFile.toPath();
            Path newFilePath = newFile.toPath();

            try {
                // Rename the new file to the same name as the old file
                Files.move(newFilePath, newFilePath.resolveSibling(oldPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File renamed and replaced successfully.");
            } catch (IOException e) {
                System.err.println("Error renaming file: " + e.getMessage());
            }
        } else {
            System.err.println("Files do not exist.");
        }

        App.currentLoadedCourseFile = newFile;
        System.out.println(newFile.getAbsolutePath());
        }catch (Exception e){
            saveAsCourseAction();
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
