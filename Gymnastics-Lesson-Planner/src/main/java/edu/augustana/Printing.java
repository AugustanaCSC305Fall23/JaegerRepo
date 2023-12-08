package edu.augustana;

import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Printing {
    private static Label jobStatus = new Label();
    private ImageView imageView = new ImageView();

    public static void start() {
        VBox root = new VBox(5);

        VBox imagesVBox = new VBox();
        for (CardView cardView : App.getCurrentSelectedLesson().getSelectedCardViews()) {
            ImageView img = cardView.getCardImage();
            img.setFitWidth(100);
            img.setFitHeight(200);
            imagesVBox.getChildren().add(img);
        }
        ;
        GridPane cardGrid = new GridPane();

        cardGrid.setVgap(10);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        cardGrid.getColumnConstraints().add(colConstraints);

        ScrollPane imagesScroll = new ScrollPane(cardGrid);
        imagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imagesScroll.setPrefHeight(500);
        ArrayList<ImageView> loadedCards = new ArrayList<>();
        imagesScroll.setContent(null);
        imagesScroll.setContent(cardGrid);
        int currCol = 0;
        int currRow = 0;

        HBox row = new HBox();
        for (CardView cardView: App.getCurrentSelectedLesson().getSelectedCardViews()) {

            ImageView imageView = cardView.getCardImage();
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            if (!loadedCards.contains(imageView)) {
                if (currCol == 3) {
                    //creating a new HBox or row after 3 cards are added
                    cardGrid.add(row, 0, currRow);
                    row = new HBox();
                    currCol = 0;
                    currRow++;
                }
                row.setAlignment(Pos.CENTER);
                row.setSpacing(30);

                row.getChildren().add(imageView);
                currCol++;
                loadedCards.add(imageView);
            }
        }


        if (!row.getChildren().isEmpty()) {
            cardGrid.add(row, 0, currRow);
        }


        Button printButton = new Button("Print");
        printButton.setOnAction(e -> printAction(imagesScroll));

        HBox jobStatusBox = new HBox(5, new Label("Print Job Staus: "), jobStatus);
        HBox buttonBox = new HBox(5, printButton);

        root.getChildren().addAll(imagesScroll, jobStatusBox, buttonBox);
        Scene scene = new Scene(root, 850, 700);
        Stage printingWindow = new Stage();
        printingWindow.initModality(Modality.APPLICATION_MODAL);
        printingWindow.initOwner(App.primaryStage);

        printingWindow.setScene(scene);
        printingWindow.setTitle("Print Option Example");
        printingWindow.show();
    }

    private static void printAction(Node node) {
        jobStatus.textProperty().unbind();
        jobStatus.setText(("creating a printer job..."));
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        job.getJobSettings().setPageLayout(pageLayout);

        if (job != null && job.showPrintDialog(null)) {
            jobStatus.textProperty().bind(job.jobStatusProperty().asString());
            boolean printed = job.printPage(node);
            if (printed) {
                job.endJob();
            } else {
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing Failed");
            }
        } else {
            jobStatus.setText("Could not create a printer job.");
        }
//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        if(job != null){
//            Printer printer = job.getPrinter();
//
//            double width = printData.getWidth();
//            double height = printData.getHeight();
//
//            PrintResolution resolution = job.getJobSettings().getPrintResolution();
//
//            width /= resolution.getFeedResolution();
//
//            height /= resolution.getCrossFeedResolution();
//
//            double scaleX = pageLayout.getPrintableWidth()/width/600;
//            double scaleY = pageLayout.getPrintableHeight()/height/600;
//
//            Scale scale = new Scale(scaleX, scaleY);
//
//            printData.getTransforms().add(scale);
//
//            boolean success = job.printPage(pageLayout, printData);
//            if(success){
//                job.endJob();
//            }
//        }
    }

/**package edu.augustana;
 import javafx.application.Application;
 import javafx.print.PrinterJob;
 import javafx.scene.Node;
 import javafx.scene.Scene;
 import javafx.scene.control.Label;
 import javafx.scene.layout.StackPane;
 import javafx.stage.Stage;

 public class Printing extends Application {


@Override public void start(Stage primaryStage) {
Label label = new Label("Content to Print");

StackPane root = new StackPane();
root.getChildren().add(label);

Scene scene = new Scene(root, 300, 200);

primaryStage.setScene(scene);
primaryStage.setTitle("Printable Node Example");
primaryStage.show();

// Print the content
printNode(label);
}

private void printNode(Node node) {
PrinterJob job = PrinterJob.createPrinterJob();
if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
boolean success = job.printPage(node);
if (success) {
job.endJob();
}
}
}

public static void main(String[] args) {
launch(args);
}**/
}
