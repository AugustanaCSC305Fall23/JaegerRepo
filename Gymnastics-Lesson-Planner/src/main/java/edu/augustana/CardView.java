package edu.augustana;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class CardView {
    private final ImageView cardImage;
    private final int cardId;
    private final String cardCode;
    private final ImageView addButton;
    private final HBox buttonAndFavButton;
    private final ImageView favoriteButton;
    private final ImageView heart;
    private boolean isFavorite = false;
    public CardView(Card card){
        cardImage = new ImageView(new Image(card.getFilePath()));
        cardImage.setFitWidth(250);
        cardImage.setPreserveRatio(true);
        cardId = card.getCardId();
        cardCode = card.getCode();
        addButton = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));
        addButton.setFitWidth(30);
        addButton.setPreserveRatio(true);
        heart = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/favorite.png")).toExternalForm()));
        favoriteButton = heart;
        favoriteButton.setFitWidth(30);
        favoriteButton.setPreserveRatio(true);
        favoriteButton.setOnMouseClicked(event -> changeFavoriteWhenClicked());
        buttonAndFavButton = combineButtonAndFavButton();

        // Set the tooltip
        Tooltip.install(buttonAndFavButton, new Tooltip("Click to add the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(100));
        }});
        Tooltip.install(cardImage, new Tooltip("Click to magnify the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(100));
        }});
    }
    public VBox getCardView(){
        VBox finalCardView = new VBox(cardImage, buttonAndFavButton);
        finalCardView.setAlignment(Pos.CENTER);
        finalCardView.setSpacing(5);
        finalCardView.setId("imageAndButton");

        return finalCardView;
    }
    private HBox combineButtonAndFavButton(){
        HBox buttonAndCode = new HBox();
        buttonAndCode.getChildren().addAll(favoriteButton, addButton);
        buttonAndCode.setAlignment(Pos.CENTER);
        buttonAndCode.setSpacing(150);
        return buttonAndCode;
    }

    public HBox getButtonAndCode(){
        return buttonAndFavButton;
    }

    public ImageView getCardImage(){return cardImage;}

    private void changeFavoriteWhenClicked(){
        if(isFavorite){
            favoriteButton.setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/favorite.png")).toExternalForm()));
        }else {
            favoriteButton.setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/redheart.png")).toExternalForm()));
        }
        isFavorite = !isFavorite;
    }
    public int getCardId() {
        return cardId;
    }
}
