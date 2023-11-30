package edu.augustana;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class CardView {
    private final ImageView cardImage;
    private final int cardId;
    private final String cardCode;
    private final ImageView addButton;
    private final HBox buttonAndCode;
    public CardView(Card card){
        cardImage = new ImageView(new Image(card.getFilePath()));
        cardImage.setFitWidth(250);
        cardImage.setPreserveRatio(true);
        cardId = card.getCardId();
        cardCode = card.getCode();
        addButton = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));
        addButton.setFitWidth(30);
        addButton.setPreserveRatio(true);
        buttonAndCode = combineButtonAndCode();
    }
    public VBox getCardView(){
        VBox finalCardView = new VBox(cardImage, buttonAndCode);
        finalCardView.setAlignment(Pos.CENTER);
        finalCardView.setSpacing(5);
        finalCardView.setId("imageAndButton");

        return finalCardView;
    }
    private HBox combineButtonAndCode(){
        HBox buttonAndCode = new HBox(new Label(cardCode), addButton);
        buttonAndCode.setAlignment(Pos.CENTER);
        buttonAndCode.setSpacing(200);

        return buttonAndCode;
    }
    public HBox getButtonAndCode(){
        return buttonAndCode;
    }

    public ImageView getCardImage(){return cardImage;}
    public int getCardId() {
        return cardId;
    }
}
