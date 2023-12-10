package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

public class HBoxForListView extends HBox{
    private CardView cardView;
    private ImageView deleteIcon;
    public HBoxForListView(CardView cardView){
        super(5);
        this.cardView = cardView;
        super.setStyle("-fx-border-color: black");
        super.setPadding(new Insets(10,3,10,3));
        super.setAlignment(Pos.CENTER);
        VBox imageVbox = new VBox();
        imageVbox.setAlignment(Pos.CENTER_RIGHT);
        Label label = new Label(cardView.getCardTitle());
        deleteIcon = App.getDeleteIcon();
        super.getChildren().addAll(label);




        super.setOnMouseEntered(event -> {
            super.getChildren().add(imageVbox);
            imageVbox.getChildren().add(deleteIcon);
        });
        super.setOnMouseExited(event -> super.getChildren().remove(1));
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getDeleteIcon() {
        return deleteIcon;
    }
}
