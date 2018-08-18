package com.bitplan.javafx.stackoverflow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

/**
 * https://stackoverflow.com/questions/21655790/javafx-popover-from-controlfx
 * JavaFX PopOver From ControlFX
 * https://stackoverflow.com/a/47187898/1497139
 * @author Sedrick
 */
public class PopOverTest extends Application {

    @Override
    public void start(Stage primaryStage) {


        //Build PopOver look and feel
        Label lblName = new Label("John Doe");
        Label lblStreet = new Label("123 Hello Street");
        Label lblCityStateZip = new Label("MadeUpCity, XX 55555");   
        VBox vBox = new VBox(lblName, lblStreet, lblCityStateZip);
        //Create PopOver and add look and feel
        PopOver popOver = new PopOver(vBox);


        Label label = new Label("Mouse mouse over me");
        label.setOnMouseEntered(mouseEvent -> {
            //Show PopOver when mouse enters label
            popOver.show(label);
        });

        label.setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            popOver.hide();
        });


        StackPane root = new StackPane();
        root.getChildren().add(label);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    

}
