package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView{
    private final ImageView thumbnailImage;
    private final String cardId;
    private final ArrayList<String> equipments;
    private final String cardTitle;
    private final HBox addButtonWrapper;
    private final HBox buttonAndFavButton;
    private final HBox favoriteButtonWrapper;
    private boolean isSelected = false;
    private boolean isFavorite;
    private String code;
    private String searchString;
    private String event;
    private String gender;
    private String modelSex;
    private ArrayList<String> levels;
    private String mainImageFilePath;
    public CardView(Card card){
        equipments = card.getEquipment();
        cardTitle = card.getTitle();
        thumbnailImage = new ImageView(new Image(card.getThumbnailFilePath()));
        thumbnailImage.setFitWidth(250);
        thumbnailImage.setPreserveRatio(true);
        cardId = String.valueOf(card.getCardId());
        event = card.getEvent();
        gender = card.getGender();
        modelSex = card.getModelSex();
        levels = card.getLevels();
        mainImageFilePath = card.getFilePath();
        code = card.getCode();

        searchString = setUpSearchString(card).toLowerCase();
        isFavorite = false;

        ImageView addButton = new ImageView(App.addButton);
        addButton.setFitWidth(30);
        addButton.setPreserveRatio(true);
        addButtonWrapper = new HBox(addButton);
        addButtonWrapper.setPadding(new Insets(5, 5, 5, 150));

        ImageView favoriteButton = new ImageView(App.heartButton);
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

    public String getCode() {
        return code;
    }

    private void setUpToolTips(){
        Tooltip.install(buttonAndFavButton, new Tooltip("Click to add the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(1000));
        }});
        Tooltip.install(thumbnailImage, new Tooltip("Click to magnify the card."){{
            setStyle("-fx-font-size: 14;");
            setShowDelay(javafx.util.Duration.millis(1000));
        }});
    }
    public void hoverFeatures(){
        addButtonWrapper.setOnMouseEntered(event -> {
            if (!isSelected){
                ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(App.fillAddButton);
            }
        });
        addButtonWrapper.setOnMouseExited(event -> {
            if (!isSelected){
                ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(App.addButton);
            }
        });

        favoriteButtonWrapper.setOnMouseEntered(event -> {
            if (!isFavorite){
                ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(App.fillHeartButton);
            }
        });
        favoriteButtonWrapper.setOnMouseExited(event -> {
            if (!isFavorite){
                ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(App.heartButton);
            }
        });

    }
    public ImageView getMainImage(){
        return new ImageView(new Image(mainImageFilePath));
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
        ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(App.selected);
    }

    public void unSelect(){
        isSelected = false;
        ((ImageView)addButtonWrapper.getChildren().get(0)).setImage(App.addButton);

    }
    public VBox getCardView(){
        VBox finalCardView = new VBox(thumbnailImage, buttonAndFavButton);
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
    public ArrayList<String> getLevels(){
        return levels;
    }

    public HBox getAddButton(){
        return addButtonWrapper;
    }

    public ImageView getThumbnailImage(){return thumbnailImage;}

    private void changeFavoriteWhenClicked(){
        if(isFavorite){
            ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(App.heartButton);
        }else {
            ((ImageView)favoriteButtonWrapper.getChildren().get(0)).setImage(App.fillHeartButton);
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


    public String getEvent() {
        return event;
    }

    public String getGender() {
        return gender;
    }

    public String getModelSex() {
        return modelSex;
    }
}
