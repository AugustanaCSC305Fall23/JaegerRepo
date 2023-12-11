package edu.augustana;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
                        cardView.getThumbnailImage().setOnMouseClicked(e -> {
                            magnifyImage(cardView.getMainImage());
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
            equipmentsBox.getItems().add(hBoxForEquipmentBox(e));
        }
    }

    public void resetCardBox(ArrayList<CardView> cards){
        while (!cardBox.getItems().isEmpty()){
            cardBox.getItems().remove(0);
        }
        for (CardView cardView: cards){
            HBoxForListView hBox = new HBoxForListView(cardView);
            hBox.getDeleteIcon().setOnMouseClicked(event -> {
                App.getCurrentSelectedLesson().removeData(cardView);
                resetCardBox(App.getCurrentSelectedLesson().getSelectedCardViews());
                resetEquipmentBox(App.getCurrentSelectedLesson().getEquipments());
                addCardsToHBoxToGrid(" ", App.getCurrentSelectedLesson());
            });
            cardBox.getItems().add(hBox);
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

    private static HBox hBoxForEquipmentBox(String e){
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

            App.addToHistory(fileName, chosenFile);
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
        if (App.historyPaths.get(App.getCurrentSelectedCourse().getName()) == null){
            saveAsCourseAction();
        }else{
            Files.deleteIfExists(Path.of(App.historyPaths.get(App.getCurrentSelectedCourse().getName())));
            App.saveCurrentCourseLogToFile(new File(App.historyPaths.get(App.getCurrentSelectedCourse().getName())));
        }
    }

    @FXML
    private void printLessonAction(){
        App.launchPrinting();
    }

    @FXML
    private void showSelectCoursePlanPopUpWindow(){
        selectCoursePopUp = new SelectOptionPopUp("Course");
        selectLessonPopUp = selectCoursePopUp.initializeCourseInWindow();
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
        popUpWindow.setResizable(false);
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
    private Label getHeaderLabelForAbout(String name){
        Label header = new Label(name);
        header.setFont(new Font(15));
        header.setStyle("-fx-background-color: #d4f7fa; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-padding: 5px 10px;");
        return header;
    }

    @FXML
    private void showAboutSection(){
        Stage aboutWindow = new Stage();

        aboutWindow.initModality(Modality.APPLICATION_MODAL);
        aboutWindow.initOwner(App.primaryStage);
        aboutWindow.setMinWidth(600);
        aboutWindow.setMinHeight(400);
        aboutWindow.setMaxHeight(400);
        aboutWindow.setMaxWidth(600);
        VBox contentVBox = new VBox(15);
        contentVBox.setAlignment(Pos.CENTER);
        contentVBox.getChildren().add(getHeaderLabelForAbout("Supervisor"));
        Image supervisorImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/supervisor.png")).toExternalForm());
        contentVBox.getChildren().add(getNameAndImage("Forrest Stonedahl", supervisorImage));

        contentVBox.getChildren().add(getHeaderLabelForAbout("Developers"));
        Image developerFImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/developerF.png")).toExternalForm());
        Image developerMImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/developerM.png")).toExternalForm());
        contentVBox.getChildren().add(getNameAndImage("Drishtant Bhandari", developerMImage));
        contentVBox.getChildren().add(getNameAndImage("Aakriti Bhandari", developerFImage));
        contentVBox.getChildren().add(getNameAndImage("Sara Zbir", developerFImage));
        contentVBox.getChildren().add(getNameAndImage("Bibhu Lamichhane", developerMImage));

        contentVBox.getChildren().add(getHeaderLabelForAbout("Product Designer"));
        Image productDesigner = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/graphic-designer.png")).toExternalForm());
        contentVBox.getChildren().add(getNameAndImage(" Samantha Keehn", productDesigner));

        contentVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f5f5, #c4e7fb); -fx-padding: 20px; -fx-border-radius: 5px;");


        ScrollPane scrollPane = new ScrollPane(contentVBox);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-radius: 5px;");
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 600, 400);
        scene.setFill(Color.TRANSPARENT);

        aboutWindow.setScene(scene);
        aboutWindow.show();
    }

    private HBox getNameAndImage(String n, Image image){
        HBox nameAndImage = new HBox(30);
        nameAndImage.setAlignment(Pos.CENTER);
        Label name = new Label(n);
        name.setStyle("-fx-text-fill: #333333;");
        ImageView img = new ImageView(image);
        img.setFitWidth(80);

        img.setPreserveRatio(true);
        nameAndImage.getChildren().addAll(name, img);
        return nameAndImage;
    }

    private static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Directory");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
    @FXML
    private void addNewCardPack(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Directory");

        // Show the directory chooser dialog
        File selectedDirectory = directoryChooser.showDialog(App.primaryStage);
        if (checkDirectory(selectedDirectory)){
            try {
                String destinationFolderCount = String.valueOf(Objects.requireNonNull(new File(App.pathToCardDataFolder).list()).length);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Loading the data please wait....");
                alert.setHeaderText(null);
                alert.show();
                copyDirectory(selectedDirectory, new File(App.pathToCardDataFolder, destinationFolderCount));
                alert.close();
                alert.setContentText("Directory moved successfully.\nPlease restart the app to see changes.");
                alert.showAndWait();
            } catch (Exception e) {
                System.err.println("Error moving directory: " + e.getMessage());
            }
        }
    }

    private void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    copyDirectory(new File(source, file), new File(destination, file));
                }
            }
        } else {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean checkDirectory(File selectedDirectory) {
        if (!selectedDirectory.exists() || !selectedDirectory.isDirectory()) {
            showAlert("Invalid directory path");
            return false;
        }

        // Check for a CSV file
        File[] csvFiles = selectedDirectory.listFiles((dir, name) -> name.endsWith(".csv"));
        if (csvFiles != null && csvFiles.length > 0) {
            System.out.println("CSV file found: " + csvFiles[0].getName());
        } else {
            showAlert("CSV file not found");
            return false;
        }

        // Check for PNG images
        File[] pngFiles = selectedDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (pngFiles != null && pngFiles.length > 0) {
            System.out.println("PNG images found: " + pngFiles.length);
        } else {
            showAlert("No PNG images found");
            return false;
        }

        // Check for a 'thumbs' directory with JPG files
        File thumbsDirectory = new File(selectedDirectory, "thumbs");
        if (thumbsDirectory.isDirectory()) {
            File[] jpgFilesInThumbs = thumbsDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
            if (jpgFilesInThumbs != null && jpgFilesInThumbs.length > 0) {
                System.out.println("JPG files in 'thumbs' directory: " + jpgFilesInThumbs.length);
            } else {
                showAlert("No JPG files found in 'thumbs' directory");
                return false;
            }
        } else {
            showAlert("'thumbs' directory not found");
            return false;
        }

        // Check for any other files in the directory
        File[] otherFiles = selectedDirectory.listFiles(file -> !file.isDirectory() &&
                !file.getName().endsWith(".csv") &&
                !file.getName().toLowerCase().endsWith(".png") &&
                !file.equals(thumbsDirectory));

        if (otherFiles != null && otherFiles.length > 0) {
            showAlert("Invalid files found in the directory");
            return false;
        }

        return true;
    }
}