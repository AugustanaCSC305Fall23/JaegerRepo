package edu.augustana;
import javafx.application.Application;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.Objects;

public class Printing{
    private static Label jobStatus = new Label();
    private ImageView imageView = new ImageView();

    public static void start(Stage primaryStage) {
        VBox root = new VBox(5);

        VBox imagesVBox = new VBox();
//        ImageView img1 = new ImageView(new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/add.png")).toExternalForm()));
//        imagesVBox.getChildren().add(img1);
        for (int cardId : App.getCurrentSelectedLesson().getCardIndexes()){
            ImageView img = new ImageView(new Image(App.getCardDatabase().get(cardId).getFilePath()));
            img.setFitWidth(100);
            img.setFitHeight(200);
            imagesVBox.getChildren().add(img);
        };
        ScrollPane imagesScroll = new ScrollPane(new ImageView(new Image(String.valueOf(App.class.getResource("staticFiles/images/1.png")))));
        Button printButton = new Button("Print"); //ex
        printButton.setOnAction(e -> printAction(imagesScroll));//ex

        HBox jobStatusBox = new HBox(5, new Label("Print Job Staus: "),jobStatus);
        HBox buttonBox = new HBox(5,printButton);

        root.getChildren().addAll(imagesScroll,jobStatusBox, buttonBox );
        //VBox root = new VBox(printButton);
//        root.getChildren().add(imagesVBox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Print Option Example");
        primaryStage.show();
    }

    private static void printAction(Node node) {
        jobStatus.textProperty().unbind();
        jobStatus.setText(("creating a printer job..."));
        PrinterJob job = PrinterJob.createPrinterJob();

        if(job != null){
            jobStatus.textProperty().bind(job.jobStatusProperty().asString());
            Boolean printed = job.printPage(node);
            if (printed){
                job.endJob();
            }else{
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing Failed");
            }

            /**PrinterJob job = PrinterJob.createPrinterJob();
             if (job != null && job.showPrintDialog(null)) {
             boolean success = job.printPage(//Specify the node or content you want to print );
             if (success) {
             job.endJob();
             }
             } **/
        }else{
            jobStatus.setText("Could not create a printer job.");
        }
//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        if(job != null){
//            Printer printer = job.getPrinter();
//            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
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


@Override
public void start(Stage primaryStage) {
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
