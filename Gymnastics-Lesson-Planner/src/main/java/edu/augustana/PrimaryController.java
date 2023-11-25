package edu.augustana;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;


import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class PrimaryController {
    public static final String borderColor = "grey";
    @FXML
    private VBox centerArea;
    @FXML
    private HBox selectCourseLessonHbox;
    private static ComboBox<String> pickCourseComboBox = null;
    @FXML
    private VBox allFilterOptions;
    @FXML
    private VBox cardBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox equipmentsBox;
    @FXML
    private Label currLessonLabel;
    @FXML
    private Label currCourseLabel;
    private Button createNewLessonPlanButton;
    private VBox pickLessonVBox;
    private Button createNewLessonButton;
    private TextField lessonName;
    private Button createNewCoursePlanButton;
    private VBox pickCourseVBox;
    private Button createNewCourseButton;
    private TextField courseName;
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
        if (App.isLessonSelected()) {
            addCardToLesson(cardView.getCardId(), false);

            addEquipmentToEquipmentBar(cardView.getCardId(), false);
        }else{
            showAddCoursePlanPopUpWindow();

        }
    }

    public void addEquipmentToEquipmentBar(int code, boolean forceAdd){
        Card equipmentToAdd = App.getCardDatabase().get(code);
        for (String e: equipmentToAdd.getEquipment()){
            if (App.getCurrentSelectedLesson().addEquipment(e) || forceAdd) {
                equipmentsBox.getChildren().add(cardTitleBox(e));
            }
        }
    }

    public void changeSelectedLessonEquipment(Lesson lesson){
        App.setCurrentSelectedLesson(lesson);
        while (!equipmentsBox.getChildren().isEmpty()) {
            equipmentsBox.getChildren().remove(0);
        }
        for (int index : lesson.getCardIndexes()){
            addEquipmentToEquipmentBar(index, true);
        }
    }

    @FXML
    private Button saveButton;

    @FXML
    public void saveCourseAction() throws IOException {
        File chosenFile = new File(App.pathToResourcesFolder + "/staticFiles/savedCourses/" + App.getCurrentSelectedCourse().getCourseName()+ ".course");
        chosenFile.createNewFile();
        System.out.println(chosenFile.getAbsolutePath());
        App.saveCurrentCourseLogToFile(chosenFile);
    }

    @FXML
    private Button saveAsButton;

    @FXML
    public void saveAsLessonAction() throws IOException {
        showAddLessonPlanPopUpWindow();
        saveCourseAction();
    }

    @FXML
    private Button editButton;

    @FXML
    public void editLessonAction() throws IOException, ClassNotFoundException {
        String fileName = App.getCurrentSelectedLesson().getLessonName() + ".txt";
        FileInputStream fos = new FileInputStream(fileName);
        ObjectInputStream oos = new ObjectInputStream(fos);
        oos.readObject();
        oos.close();
    }

    @FXML
    private void showAddLessonPlanPopUpWindow(){
        Stage primaryStage = (Stage) cardBox.getScene().getWindow();
        final Stage selectLessonPopUpWindow = new Stage();
        selectLessonPopUpWindow.initModality(Modality.APPLICATION_MODAL);
        selectLessonPopUpWindow.initOwner(primaryStage);
        VBox popUpWindowContentVBox = new VBox(20);
        popUpWindowContentVBox.setAlignment(Pos.CENTER);
        popUpWindowContentVBox.setStyle("-fx-background-color: #E4CCFF");
        initializeSelectOptionPopUp(popUpWindowContentVBox, "lesson", App.getLessons());
        Scene scene = new Scene(popUpWindowContentVBox, 600, 400);
        selectLessonPopUpWindow.setScene(scene);
        selectLessonPopUpWindow.show();
    }

    private void intializeCreateNewPopUp(VBox vBoxForContent, String type){
        Label label = new Label("Create new "+ type);
        label.setFont(new Font("Segoe Script", 32));
        TextField nameTextField = new TextField();
        VBox nameWrapper = new VBox(nameTextField);
        nameWrapper.setAlignment(Pos.CENTER);
        VBox.setMargin(nameWrapper, new Insets(0, 150, 0, 150));
        Button createButton = new Button("Create");
        createButton.setPrefSize(154.4, 28);
        createButton.setOnMouseClicked(event -> {
            if (type.equals("lesson")) {
                createNewLesson();
                lessonName = nameTextField;
            }else {
                createNewCourse();
                courseName = nameTextField;
            }
        });
        vBoxForContent.getChildren().addAll(label, nameWrapper, createButton);
    }

    private void createNewLesson(){
        if (!lessonName.getText().isEmpty()) {
            Stage currWindow = (Stage) createNewLessonButton.getScene().getWindow();
            Lesson lesson = new Lesson (lessonName.getText());
            App.addLessonToLessons(lesson);
            lessonSelected(lesson);
            currWindow.close();
        }
    }

    public void changeSelectedLesson(Lesson lesson){
        App.setCurrentSelectedLesson(lesson);
        while (!cardBox.getChildren().isEmpty()) {
            cardBox.getChildren().remove(0);
        }
        for (int index : lesson.getCardIndexes()) {
            addCardToLesson(index, true);
        }
    }

    public void addCardToLesson(int code, boolean forceAdd) {
        Card cardToAdd = App.getCardDatabase().get(code);
        boolean added = App.getCurrentSelectedLesson().addCard(cardToAdd);
        if (added || forceAdd) {
            cardBox.getChildren().add(cardTitleBox(cardToAdd.getTitle()));
        }
    }

    @FXML
    private void lessonSelected(Lesson lesson) {
        Stage currWindow = (Stage) createNewLessonPlanButton.getScene().getWindow();
        App.setCurrentSelectedLesson(lesson);
        App.lessonSelected(true);
        currLessonLabel.setText(App.getCurrentSelectedLesson().getLessonName());
        currWindow.close();
        changeSelectedLesson(lesson);
        changeSelectedLessonEquipment(lesson);
    }



    @FXML
    private void newLessonPlanPopUpWindow(){
        Stage primaryStage = (Stage) cardBox.getScene().getWindow();
        final Stage createNewLessonPopUpWindow = new Stage();
        createNewLessonPopUpWindow.initModality(Modality.APPLICATION_MODAL);
        createNewLessonPopUpWindow.initOwner(primaryStage);
        VBox popUpWindowContentVBox = new VBox(20);
        popUpWindowContentVBox.setAlignment(Pos.CENTER);
        popUpWindowContentVBox.setStyle("-fx-background-color: #E4CCFF");
        intializeCreateNewPopUp(popUpWindowContentVBox, "lesson");
        Scene scene = new Scene(popUpWindowContentVBox, 600, 400);
        createNewLessonPopUpWindow.setScene(scene);
        createNewLessonPopUpWindow.show();
    }

    private void addLessonToSelectLessonPopUp(HashMap<String, Lesson> lessons, VBox lessonOption){
        for (String l : lessons.keySet()) {
            Button lessonButton = new Button(lessons.get(l).getLessonName());
            lessonButton.setMinWidth(169);
            lessonButton.setStyle("-fx-background-color:  #9472C1");
            lessonButton.setOnMouseClicked(event -> {
                lessonSelected(lessons.get(l));
            });
            lessonOption.getChildren().add(lessonButton);
        }
    }

    @FXML
    private void showAddCoursePlanPopUpWindow(){
        Stage primaryStage = (Stage) cardBox.getScene().getWindow();
        final Stage selectCoursePopUpWindow = new Stage();
        selectCoursePopUpWindow.initModality(Modality.APPLICATION_MODAL);
        selectCoursePopUpWindow.initOwner(primaryStage);
        VBox popUpWindowContentVBox = new VBox(20);
        popUpWindowContentVBox.setAlignment(Pos.CENTER);
        popUpWindowContentVBox.setStyle("-fx-background-color: #E4CCFF");
        initializeSelectOptionPopUp(popUpWindowContentVBox, "course", App.getCourses());
        Scene scene = new Scene(popUpWindowContentVBox, 600, 400);
        selectCoursePopUpWindow.setScene(scene);
        selectCoursePopUpWindow.show();
    }
    
    private void initializeSelectOptionPopUp(VBox vBoxForContent, String type, HashMap<String, ?> data){
        pickCourseVBox = new VBox();
        Label label = new Label("Select a Course");
        label.setFont(new Font("Segoe Script", 29));
        createNewCoursePlanButton = new Button("+ Create new Course");
        createNewCoursePlanButton.setFont(new Font("Segoe Script", 12));
        createNewCoursePlanButton.setPrefWidth(172.8);
        createNewCoursePlanButton.setPrefHeight(30.4);

        vBoxForContent.getChildren().add(label);
        vBoxForContent.getChildren().add(pickCourseVBox);
        vBoxForContent.getChildren().add(createNewCoursePlanButton);

        ScrollPane pickOptionScroll = new ScrollPane();
        pickOptionScroll.setFitToWidth(true);
        pickOptionScroll.setStyle("-fx-background-color:#E4CCFF");

        VBox options = new VBox(10);
        options.setAlignment(Pos.CENTER);
        options.setStyle("-fx-background-color: #E4CCFF");

        pickOptionScroll.setContent(options);

        if (type.equals("lesson")){
            createNewLessonPlanButton.setOnMouseClicked(event -> {
                newLessonPlanPopUpWindow();
                ((Stage) createNewLessonPlanButton.getScene().getWindow()).close();

            });
            addCourseToSelectCoursePopUp((HashMap<String, Course>) data, options);
        }else {
            createNewCoursePlanButton.setOnMouseClicked(event -> {
                newCoursePlanPopUpWindow();
                ((Stage) createNewCoursePlanButton.getScene().getWindow()).close();
            });
            Button loadCoursePlanButton = new Button("+ Load New Course");
            loadCoursePlanButton.setFont(new Font("Segoe Script", 12));
            loadCoursePlanButton.setPrefWidth(172.8);
            loadCoursePlanButton.setPrefHeight(30.4);
            vBoxForContent.getChildren().add(loadCoursePlanButton);
            addLessonToSelectLessonPopUp((HashMap<String, Lesson>) data, options);
        }
    }

    private void addCourseToSelectCoursePopUp(HashMap<String, Course> courses, VBox courseOption){
        for (Course course : courses.values()) {
            Button courseButton = new Button(course.getCourseName());
            courseButton.setMinWidth(169);
            courseButton.setStyle("-fx-background-color:  #9472C1");
            courseButton.setOnMouseClicked(event -> {
                courseSelected(course);
            });
            courseOption.getChildren().add(courseButton);
        }
    }

    @FXML
    private void courseSelected(Course course){
        Stage currWindow = (Stage) createNewCoursePlanButton.getScene().getWindow();
        App.setCurrentSelectedCourse(course);
        App.courseSelected(true);
        currCourseLabel.setText(App.getCurrentSelectedCourse().getCourseName());
        currWindow.close();
        showAddLessonPlanPopUpWindow();
    }

    @FXML
    private void newCoursePlanPopUpWindow(){
        Stage primaryStage = (Stage) cardBox.getScene().getWindow();
        final Stage createNewCoursePopUpWindow = new Stage();
        createNewCoursePopUpWindow.initModality(Modality.APPLICATION_MODAL);
        createNewCoursePopUpWindow.initOwner(primaryStage);
        VBox popUpWindowContentVBox = new VBox(20);
        popUpWindowContentVBox.setAlignment(Pos.CENTER);
        popUpWindowContentVBox.setStyle("-fx-background-color: #E4CCFF");
        intializeCreateNewPopUp(popUpWindowContentVBox, "course");
        Scene scene = new Scene(popUpWindowContentVBox, 600, 400);
        createNewCoursePopUpWindow.setScene(scene);
        createNewCoursePopUpWindow.show();
    }

    private void createNewCourse(){
        if (!courseName.getText().isEmpty()) {
            Stage currWindow = (Stage) createNewCourseButton.getScene().getWindow();
            Course course = new Course (courseName.getText());
            App.addCourseToCourses(course);
            courseSelected(course);
            currWindow.close();
        }
    }

}
