package edu.augustana;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private ImageView imageView = new ImageView();

    public static void start() {
        VBox root = new VBox(5);
        root.setAlignment(Pos.CENTER);

        VBox vBoxForCards = new VBox();
        vBoxForCards.setStyle("-fx-background-color: white;");

        vBoxForCards.setSpacing(10);

        ScrollPane imagesScroll = new ScrollPane(vBoxForCards);
        imagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imagesScroll.setPrefHeight(500);
        ArrayList<ImageView> loadedCards = new ArrayList<>();

        imagesScroll.setContent(vBoxForCards);
        int currCol = 0;
        int currRow = 0;

        HBox row = new HBox();
        for (CardView cardView : App.getCurrentSelectedLesson().getSelectedCardViews()) {

            ImageView imageView = cardView.getMainImage();
            imageView.setFitWidth(220);
            imageView.setPreserveRatio(true);
            VBox imageViewWrapper = new VBox(3);
            HBox currentEquipments = new HBox(5);
            currentEquipments.getChildren().add(new Label("Equipments: "));
            for (String e : cardView.getEquipments()) {
                Label equipment = new Label(e);
                equipment.setPadding(new Insets(0, 3, 3, 3));
                equipment.setStyle("-fx-border-color: black");
                currentEquipments.getChildren().add(equipment);
            }
            imageViewWrapper.getChildren().addAll(imageView, currentEquipments);
            if (!loadedCards.contains(imageView)) {
                if (currCol == 2) {
                    //creating a new HBox or row after 3 cards are added
                    vBoxForCards.getChildren().add(row);
                    row = new HBox();
                    currCol = 0;
                    currRow++;
                }
                row.setAlignment(Pos.CENTER);
                row.setSpacing(30);

                row.getChildren().add(imageViewWrapper);
                currCol++;
                loadedCards.add(imageView);
            }
        }
        if (!row.getChildren().isEmpty()) {
            vBoxForCards.getChildren().add(row);
        }

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


        HBox jobStatusBox = new HBox(5, new Label("Print Job Staus: "), jobStatus);
        HBox buttonBox = new HBox(5, printButton);

        root.getChildren().addAll(imagesScroll, jobStatusBox, buttonBox);
        Scene scene = new Scene(root, 480, 600);
        printingWindow = new Stage();
        printingWindow.initModality(Modality.APPLICATION_MODAL);
        printingWindow.initOwner(App.primaryStage);

        printingWindow.setScene(scene);
        printingWindow.setTitle("Print Option Example");
        printingWindow.show();
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

            } else {
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing Failed");
            }
        } else {
            jobStatus.setText("Could not create a printer job.");
        }
    }
}
