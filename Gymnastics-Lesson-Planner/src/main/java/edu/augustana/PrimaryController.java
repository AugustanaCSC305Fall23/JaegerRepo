package edu.augustana;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    private void initialize() throws FileNotFoundException {
        pickLessonComboBox.getItems().addAll("+ Create New Lesson");
        loadImagesToGridView();
    }

    private void loadImagesToGridView() throws FileNotFoundException {
        ScrollPane scrollPane = new ScrollPane();
        GridPane cardGrid = new GridPane();
        cardGrid.setId("cardGridView");

        cardGrid.setVgap(20);

        scrollPane.setContent(cardGrid);
        centerArea.getChildren().add(scrollPane);
        cardGrid.setAlignment(Pos.CENTER);

        addCardsToHboxToGrid(cardGrid);
    }

    private ImageView getImageView(String path) throws FileNotFoundException {
        ImageView image = new ImageView();
        image.setImage(new Image(new FileInputStream(path)));
        image.setPreserveRatio(true);
        image.setFitWidth(250);

        return image;
    }

    private void addCardsToHboxToGrid(GridPane cardGrid) throws FileNotFoundException {
        int currCol = 0;
        int currRow = 0;
        HashMap<String, Card> cards = App.getCardHashMap();
        HBox row = new HBox();
        for (String cardId : cards.keySet()){
            if (currCol == 3){
                row.setAlignment(Pos.CENTER);
                row.setSpacing(100);
                addHBoxToGrid(cardGrid, row, currRow);
                row = new HBox();
                currCol = 0;
                currRow++;
            }
            VBox imageAndButton = new VBox(getImageView(cards.get(cardId).getFilePath()));
            imageAndButton.setAlignment(Pos.TOP_RIGHT);
            row.getChildren().add(imageAndButton);
            currCol++;
        }
    }

    private void addHBoxToGrid(GridPane grid,HBox row, int rowNum){
        grid.add(row, 0, rowNum);
    }

    private void addImageToGrid(GridPane gridPane, String imageUrl, int col, int row) throws FileNotFoundException {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(new FileInputStream(imageUrl)));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(250);

        ImageView plusImage = new ImageView();
        plusImage.setImage(new Image(new FileInputStream(App.imagesFilePath + "/add.png")));
        plusImage.setFitHeight(20);
        plusImage.setFitWidth(20);


//        gridPane.add(card, col, row);
    }

    @FXML
    private void plusClicked() throws IOException {
        if (App.isLessonSelected()){
            System.out.println("lesson clicked");
        }else{
            showAddLessonPlanPopUpWindow();
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
