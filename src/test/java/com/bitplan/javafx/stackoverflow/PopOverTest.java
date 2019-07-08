/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except
 *    - the ones in the stackoverflow package
 *    - SwingFXUtils.java from Oracle which is provided e.g. for the raspberry env
 *
 * Copyright 2017-2018 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.javafx.stackoverflow;

import org.controlsfx.control.PopOver;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * https://stackoverflow.com/questions/21655790/javafx-popover-from-controlfx
 * JavaFX PopOver From ControlFX
 * https://stackoverflow.com/a/47187898/1497139
 * @author Sedrick
 */
public class PopOverTest extends TestApplication {

    @Override
    public void start(Stage primaryStage) {

        ImageView imageView=super.getImageView(primaryStage);
        //Build PopOver look and feel
        Label lblName = new Label("John Doe");
        Label lblStreet = new Label("123 Hello Street");
        Label lblCityStateZip = new Label("MadeUpCity, XX 55555");   
        VBox vBox = new VBox(lblName, lblStreet, lblCityStateZip,imageView);
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
