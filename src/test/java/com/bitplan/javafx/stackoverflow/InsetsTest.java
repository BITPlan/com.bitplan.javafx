package com.bitplan.javafx.stackoverflow;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX 8: Stage insets (window decoration thickness)?
 * https://stackoverflow.com/a/26722832/1497139
 * @author wf
 *
 */
public class InsetsTest extends TestApplication {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Label topLabel = new Label();
    Label leftLabel = new Label();
    Label rightLabel = new Label();
    Label bottomLabel = new Label();
    Label menuBarLabel=new Label();

   
    final Menu menu1 = new Menu("File");
    final Menu menu2 = new Menu("Options");
    final Menu menu3 = new Menu("Help");

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(menu1, menu2, menu3);
    VBox root = new VBox(10, menuBar,topLabel, leftLabel, bottomLabel, rightLabel,menuBarLabel);
    // root.setAlignment(Pos.CENTER); root.getChildren().add(menuBar);

    super.createSceneAndShowStage(primaryStage, root);
    ObjectBinding<Insets> insets = Bindings.createObjectBinding(
        () -> new Insets(scene.getY(),
            primaryStage.getWidth() - scene.getWidth() - scene.getX(),
            primaryStage.getHeight() - scene.getHeight() - scene.getY(),
            scene.getX()),
        scene.xProperty(), scene.yProperty(), scene.widthProperty(),
        scene.heightProperty(), primaryStage.widthProperty(),
        primaryStage.heightProperty());

    topLabel.textProperty().bind(Bindings
        .createStringBinding(() -> "Top: " + insets.get().getTop(), insets));
    leftLabel.textProperty().bind(Bindings
        .createStringBinding(() -> "Left: " + insets.get().getLeft(), insets));
    rightLabel.textProperty().bind(Bindings.createStringBinding(
        () -> "Right: " + insets.get().getRight(), insets));
    bottomLabel.textProperty().bind(Bindings.createStringBinding(
        () -> "Bottom: " + insets.get().getBottom(), insets));
    menuBarLabel.textProperty().bind(Bindings.createStringBinding(()->"menuBar height "+menuBar.getHeight(),menuBar.prefHeightProperty()));
  }

  public static void main(String[] args) {
    launch(args);
  }

}
