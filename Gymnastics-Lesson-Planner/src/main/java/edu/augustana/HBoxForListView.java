package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HBoxForListView extends HBox {
    private CardView cardView;
    private ImageView deleteIcon;

    public HBoxForListView(CardView cardView) {
        super(5);
        this.cardView = cardView;
        super.setStyle("-fx-border-color: black");
        super.setPadding(new Insets(0, 0, 0, 0));
        super.setAlignment(Pos.CENTER);


        this.setOnMouseClicked(e-> {
            showImage(cardView);
        });
        VBox imageVbox = new VBox();
        imageVbox.setAlignment(Pos.CENTER_RIGHT);
        imageVbox.setPadding(new Insets(0, 0, 0, 0));

        Label label = new Label(cardView.getCardTitle());
        deleteIcon = App.getDeleteIcon();

        label.setMinWidth(150); // Adjust the width as needed
        imageVbox.setMaxWidth(20);
        label.setPadding(new Insets(0, 0, 0, 5));

        super.setMinHeight(31);
        super.setMaxHeight(31);

        super.getChildren().addAll(label, imageVbox);
        imageVbox.getChildren().add(deleteIcon);
        imageVbox.setVisible(false);

        super.setOnMouseEntered(event -> imageVbox.setVisible(true));
        super.setOnMouseExited(event -> imageVbox.setVisible(false));
    }

    private void showImage(CardView card) {
        ImageView newImage = card.getMainImage();
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
        Scene scene = new Scene(contentVBox, 600, 420);
        popUpWindow.setScene(scene);
        popUpWindow.show();
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getDeleteIcon() {
        return deleteIcon;
    }
}
