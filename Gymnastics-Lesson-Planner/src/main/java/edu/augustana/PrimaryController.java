package edu.augustana;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class PrimaryController {

    @FXML
    private BorderPane window;
    @FXML
    private VBox centerArea;
    @FXML
    private VBox filters;
    @FXML
    private ComboBox<String> pickLessonComboBox;
    @FXML
    private void initialize() throws IOException {
        pickLessonComboBox.getItems().addAll("+ Create New Lesson");
        loadImagesToGridView();
    }

    private void loadImagesToGridView() throws IOException {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        GridPane cardGrid = new GridPane();
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setFillWidth(true);
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);

        scrollPane.setContent(cardGrid);
        centerArea.getChildren().add(scrollPane);

        addCardsToHboxToGrid(cardGrid);
    }

    private Image getImage(String path) throws FileNotFoundException {
        return new Image(new FileInputStream(path));

    }

    private void addCardsToHboxToGrid(GridPane cardGrid) throws FileNotFoundException {
        int currCol = 0;
        int currRow = 0;
        HashMap<String, Card> cards = App.getCardHashMap();
        HBox row = new HBox();
        for (String cardId : cards.keySet()){
            ImageView plusButton = new ImageView();
            plusButton.setImage(getImage(App.imagesFilePath + "\\add.png"));

            plusButton.setPreserveRatio(true);
            plusButton.setFitWidth(30);
            plusButton.setFitHeight(30);
            if (currCol == 3){
                row.setAlignment(Pos.CENTER);
                addHBoxToGrid(cardGrid, row, currRow);
                row = new HBox();
                currCol = 0;
                currRow++;
            }

            VBox imageAndButton = new VBox();
            ImageView cardImage = new ImageView();
            cardImage.setImage(getImage(cards.get(cardId).getFilePath()));
            cardImage.setPreserveRatio(true);
            cardImage.setFitWidth(250);

            VBox cardImageWrapper = new VBox(cardImage);
            VBox plusButtonWrapper = new VBox(plusButton);

            VBox.setMargin(cardImageWrapper, new Insets(0, 0, 10, 0));
            imageAndButton.setPadding(new Insets(20, 20, 5, 20));
            imageAndButton.setStyle("-fx-border-color: grey");
            plusButtonWrapper.setAlignment(Pos.BOTTOM_RIGHT);
            plusButtonWrapper.setOnMouseClicked(event -> {
                try {
                    plusClicked();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            imageAndButton.getChildren().add(cardImageWrapper);
            imageAndButton.getChildren().add(plusButtonWrapper);
            imageAndButton.setAlignment(Pos.TOP_RIGHT);

            row.getChildren().add(imageAndButton);
            currCol++;
        }
    }

    private void addHBoxToGrid(GridPane grid,HBox row, int rowNum){
        grid.add(row, 0, rowNum);
    }

    @FXML
    private void plusClicked() throws IOException {
        showAddLessonPlanPopUpWindow();
//        if (App.isLessonSelected()){
//            System.out.println("lesson clicked");
//        }else{
//            showAddLessonPlanPopUpWindow();
//        }
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
