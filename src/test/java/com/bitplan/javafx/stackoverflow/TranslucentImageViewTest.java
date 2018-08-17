package com.bitplan.javafx.stackoverflow;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TranslucentImageViewTest extends TestApplication {

  @Override
  public void start(Stage stage) throws Exception {
    ImageView imageView = getImageView(stage);
    imageView.setPreserveRatio(false);
    
    Pane drawOnGlass=new Pane();
    drawOnGlass.setStyle(
        "-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
    
    StackPane stackPane = new StackPane();
    StackPane.setAlignment(imageView, Pos.CENTER);
    stackPane.getChildren().addAll(imageView,drawOnGlass);
    
    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) ->{
      drawOnGlass.getChildren().clear();
      double w = drawOnGlass.getWidth();
      double h=drawOnGlass.getHeight();
      Line line = new Line(0,0,w,h);
      line.setStrokeWidth(2);
      line.setStroke(Color.rgb(0xff,0x80, 0x00,0.5));
      Line line2 = new Line(w,0,0,h);
      line2.setStrokeWidth(2);
      line2.setStroke(Color.rgb(0xff,0x80, 0x00,0.5));
      double radius=4;
      Circle circle=new Circle();
      circle.setRadius(radius);
      circle.setFill(Color.WHITE);
      circle.setTranslateX(w/2);
      circle.setTranslateY(h/2);
      Label label=new Label("Willich\n2mm 8:30 Uhr");
      label.setTranslateX(w/2+radius);
      label.setTranslateY(h/2+radius);
      label.setTextFill(Color.WHITE);
      drawOnGlass.getChildren().addAll(line,line2,circle,label);
      
        
    };
    drawOnGlass.widthProperty().addListener(sizeListener);
    drawOnGlass.heightProperty().addListener(sizeListener);
    /*final StackPane layout = new StackPane();
    layout.getChildren().addAll(stackPane);
    layout.setStyle("-fx-background-color: silver; -fx-padding: 10;");*/
    super.createSceneAndShowStage(stage, stackPane);
  }
  
  public static void main(String[] args) {
    launch(args);
  }

}
