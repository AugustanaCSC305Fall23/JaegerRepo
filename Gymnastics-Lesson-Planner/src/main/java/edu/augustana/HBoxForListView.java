package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
        Label label = new Label(cardView.getCardTitle());
        deleteIcon = App.getDeleteIcon();
        super.getChildren().addAll(label);
        super.setOnMouseEntered(event -> super.getChildren().add(deleteIcon));
        super.setOnMouseExited(event -> super.getChildren().remove(1));
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getDeleteIcon() {
        return deleteIcon;
    }
}
