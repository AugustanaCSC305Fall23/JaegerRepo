package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class CardView{
    private final ImageView cardImage;
    private final String cardId;
    private final ArrayList<String> equipments;
    private final String cardTitle;
    private final HBox addButtonWrapper;
    private final HBox buttonAndFavButton;
    private final HBox favoriteButtonWrapper;
    private boolean isSelected = false;
    private boolean isFavorite;
    private String searchString;
    public CardView(Card card){
        equipments = card.getEquipment();
        cardTitle = card.getTitle();
        cardImage = new ImageView(new Image(card.getFilePath()));
        cardImage.setFitWidth(250);
        cardImage.setPreserveRatio(true);
        cardId = String.valueOf(card.getCardId());

        searchString = setUpSearchString(card).toLowerCase();
        isFavorite = false;

        ImageView addButton = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));
        addButton.setFitWidth(30);
        addButton.setPreserveRatio(true);
        addButtonWrapper = new HBox(addButton);
        addButtonWrapper.setPadding(new Insets(5, 5, 5, 150));

        ImageView favoriteButton = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/heart.png")).toExternalForm()));
        favoriteButton.setFitWidth(30);
        favoriteButton.setPreserveRatio(true);
        favoriteButtonWrapper = new HBox(favoriteButton);
        favoriteButtonWrapper.setPadding(new Insets(5, 5, 5, 5));
        favoriteButtonWrapper.setOnMouseClicked(event -> changeFavoriteWhenClicked());
        buttonAndFavButton = combineButtonAndFavButton();

        hoverFeatures();
        setUpToolTips();
    }

    private String setUpSearchString(Card card){
        StringBuilder searchString = new StringBuilder(card.getCode() + card.getEvent() + card.getCategory() + card.getTitle() + card.getGender() + card.getModelSex());
        for (String l: card.getLevels()){
            searchString.append(l);
        }
        for (String e: card.getEquipment()){
            searchString.append(e);
        }
        for (String k: card.getKeywords()){
            searchString.append(k);
        }

        return searchString + " ";
    }
    private void setUpToolTips(){
        Tooltip.install(buttonAndFavButton, new Tooltip("Click to add the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(1000));
        }});
        Tooltip.install(cardImage, new Tooltip("Click to magnify the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(1000));
        }});
    }
    public void hoverFeatures(){
        addButtonWrapper.setOnMouseEntered(event -> {
            if (!isSelected){
                ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/fillAdd.png")).toExternalForm()));
            }
        });
        addButtonWrapper.setOnMouseExited(event -> {
            if (!isSelected){
                ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));
            }
        });

        favoriteButtonWrapper.setOnMouseEntered(event -> {
            if (!isFavorite){
                ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/fillHeart.png")).toExternalForm()));
            }
        });
        favoriteButtonWrapper.setOnMouseExited(event -> {
            if (!isFavorite){
                ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/heart.png")).toExternalForm()));
            }
        });

    }
    public boolean addButtonClicked(){
        if (isSelected){
            unSelect();
        }else {
            select();
        }
        return isSelected;
    }
    public void select(){
        isSelected = true;
        ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/selected.png")).toExternalForm()));
    }

    public void unSelect(){
        isSelected = false;
        ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));

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
        buttonAndCode.getChildren().addAll(favoriteButtonWrapper, addButtonWrapper);
        buttonAndCode.setAlignment(Pos.CENTER);
        return buttonAndCode;
    }

    public HBox getAddButton(){
        return addButtonWrapper;
    }

    public ImageView getCardImage(){return cardImage;}

    private void changeFavoriteWhenClicked(){
        if(isFavorite){
            ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/heart.png")).toExternalForm()));
        }else {
            ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/filledHeart.png")).toExternalForm()));
        }
        isFavorite = !isFavorite;
    }
    public String getCardId() {
        return cardId;
    }

    public ArrayList<String> getEquipments() {
        return equipments;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getSearchString() {
        return searchString;
    }
    public boolean getIsSelected(){
        return isSelected;
    }
}
