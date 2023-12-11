package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Printing {
    private static Label jobStatus = new Label();
    private static Stage printingWindow;
    private static boolean addEquipments = true;
    private static boolean noImage = false;
    private static VBox vBoxForCards;

    public static void start() {
        VBox root = new VBox(5);
        root.setAlignment(Pos.CENTER);

        vBoxForCards = new VBox();
        vBoxForCards.setStyle("-fx-background-color: white;");
        vBoxForCards.setSpacing(10);
        vBoxForCards.setAlignment(Pos.CENTER);
        vBoxForCards.setPrefWidth(480);
        vBoxForCards.setMinWidth(480);
        vBoxForCards.setMaxWidth(480);


        ScrollPane imagesScroll = new ScrollPane(vBoxForCards);
        imagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imagesScroll.setPrefHeight(500);

        Button printButton = setUpPrintButton(imagesScroll);

        HBox jobStatusBox = new HBox(5, new Label("Print Job Staus: "), jobStatus);
        jobStatusBox.setAlignment(Pos.CENTER);
        HBox buttonBox = new HBox(5, printButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox printingOptions = new HBox(10);
        printingOptions.setAlignment(Pos.CENTER);

        setupPrintingOptions(printingOptions);

        imagesScroll.setContent(vBoxForCards);
        addContentToPrintView();
        root.getChildren().addAll(imagesScroll, jobStatusBox, buttonBox, printingOptions);
        Scene scene = new Scene(root, 480, 600);
        printingWindow = new Stage();
        printingWindow.initModality(Modality.APPLICATION_MODAL);
        printingWindow.initOwner(App.primaryStage);

        printingWindow.setScene(scene);
        printingWindow.setTitle("Print Option Example");
        printingWindow.show();
    }

    private static void setupPrintingOptions(HBox printingOptions) {
        ToggleGroup group = new ToggleGroup();
        RadioButton cardAndEquipment = new RadioButton("Cards and Equipments");
        RadioButton cardNoEquipment = new RadioButton("Only Cards");
        RadioButton noImages = new RadioButton("No card images");
        cardAndEquipment.setToggleGroup(group);
        cardNoEquipment.setToggleGroup(group);
        noImages.setToggleGroup(group);
        printingOptions.getChildren().addAll(cardAndEquipment, cardNoEquipment, noImages);
        cardAndEquipment.setSelected(true);
        cardAndEquipment.setOnAction(event -> {
            addEquipments = true;
            noImage = false;
            addContentToPrintView();
        });
        cardNoEquipment.setOnAction(event -> {
            addEquipments = false;
            noImage = false;
            addContentToPrintView();
        });
        noImages.setOnAction(event -> {
            noImage = true;
            addEquipments = true;
            addContentToPrintView();
        });
    }

    private static Button setUpPrintButton(ScrollPane imagesScroll) {
        Button printButton = new Button("Print");
        printButton.setOnAction(e -> printAction((VBox) imagesScroll.getContent()));
        printButton.setStyle("-fx-background-color: #ADD8E6;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: black;");

        printButton.setOnMouseEntered(e -> printButton.setStyle("-fx-background-color: #69d1c0;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: black;"));
        printButton.setOnMouseExited(e -> printButton.setStyle("-fx-background-color: #ADD8E6;" + "-fx-background-radius: 20;" +
                "-fx-text-fill: black;"));

        return printButton;
    }

    private static void addContentToPrintView() {
        ArrayList<String> loadedCards = new ArrayList<>();
        while (!vBoxForCards.getChildren().isEmpty()) {
            vBoxForCards.getChildren().remove(0);
        }
        int currCol = 0;
        int currRow = 0;

        HBox row = new HBox();
        for (CardView cardView : App.getCurrentSelectedLesson().getSelectedCardViews()) {
            VBox cardData = new VBox(3);
            cardData.setAlignment(Pos.CENTER);
            if (!noImage) {
                ImageView imageView = cardView.getMainImage();
                imageView.setFitWidth(220);
                imageView.setPreserveRatio(true);
                cardData.getChildren().add(imageView);
            } else {
                cardData.setStyle("-fx-border-color: black");
                cardData.setPadding(new Insets(5, 5, 5, 5));
                HBox cardInfo = new HBox(5);
                cardInfo.setAlignment(Pos.CENTER);
                System.out.println(cardView.getCode());
                cardInfo.getChildren().addAll(new Label(cardView.getCode()), new Label(cardView.getCardTitle()));
                cardData.getChildren().add(cardInfo);
            }
            if (addEquipments) {
                HBox currentEquipments = new HBox(5);
                currentEquipments.getChildren().add(new Label("Equipments: "));
                for (String e : cardView.getEquipments()) {
                    Label equipment = new Label(e);
                    equipment.setPadding(new Insets(0, 3, 3, 3));
                    equipment.setStyle("-fx-border-color: black");
                    currentEquipments.getChildren().add(equipment);
                }
                cardData.getChildren().add(currentEquipments);
            }
            if (!loadedCards.contains(cardView.getCardId())) {
                if (currCol == 2) {
                    //creating a new HBox or row after 3 cards are added
                    vBoxForCards.getChildren().add(row);
                    row = new HBox();
                    currCol = 0;
                    currRow++;
                }
                row.setAlignment(Pos.CENTER);
                row.setSpacing(30);

                row.getChildren().add(cardData);
                currCol++;
                loadedCards.add(cardView.getCardId());
            }
        }
        if (!row.getChildren().isEmpty()) {
            vBoxForCards.getChildren().add(row);
        }
    }

    private static void printAction(VBox content) {
        jobStatus.textProperty().unbind();
        jobStatus.setText(("creating a printer job..."));
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        job.getJobSettings().setPageLayout(pageLayout);

        if (job.showPrintDialog(null)) {
            jobStatus.textProperty().bind(job.jobStatusProperty().asString());
            boolean printed = false;

            int itemsPerPage = 3;
            int remainingItems = content.getChildren().size();
            int pageCount = (int) Math.ceil((double) remainingItems / itemsPerPage);

            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                Pane printPane = new Pane();
                int itemsOnThisPage = Math.min(itemsPerPage, remainingItems);

                for (int i = 0; i < itemsOnThisPage; i++) {
                    Node item = content.getChildren().get(0);
                    if (item != null) {
                        printPane.getChildren().add(item);
                        content.getChildren().remove(item);
                    }
                }

                printed = job.printPage(printPane);
                remainingItems -= itemsOnThisPage;
            }

            if (printed) {
                job.endJob();
                printingWindow.close();
            } else {
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing Failed");
            }
        } else {
            jobStatus.setText("Could not create a printer job.");
        }
    }

    private void addNoImageContentToPrintView() {

    }
}
