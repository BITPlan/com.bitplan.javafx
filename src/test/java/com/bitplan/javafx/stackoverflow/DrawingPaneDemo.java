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

import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * see http://www.tutego.de/blog/javainsel/2013/12/javafx-put-a-draggable-resizable-polygon-on-an-image/
 * @author wf
 *
 */
class DrawingPane extends Pane {
  public DrawingPane(ImageView imageView, Polygon poly) {
    poly.setFill(Color.web("ANTIQUEWHITE", 0.8));
    poly.setStroke(Color.web("ANTIQUEWHITE"));
    poly.setStrokeWidth(2);

    getChildren().addAll(imageView, poly);

    for (int i = 0; i < poly.getPoints().size(); i += 2) {
      Circle circle = new Circle(poly.getPoints().get(i),
          poly.getPoints().get(i + 1), 5);
      circle.setFill(Color.web("PERU", 0.8));
      circle.setStroke(Color.PERU);
      circle.setStrokeWidth(2);

      AtomicInteger polyCoordinateIndex = new AtomicInteger(i);
      circle.centerXProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldValue, Number newValue) {
          poly.getPoints().set(polyCoordinateIndex.get(),
              newValue.doubleValue());
        }
      });
      circle.centerYProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldValue, Number newValue) {
          poly.getPoints().set(polyCoordinateIndex.get() + 1,
              (Double) newValue);
        }
      });
      setDragHandler(circle);
      getChildren().add(circle);
    }
  }

  private double dragDeltaX, dragDeltaY;

  private void setDragHandler(Circle circle) {
    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        dragDeltaX = circle.getCenterX() - mouseEvent.getSceneX();
        dragDeltaY = circle.getCenterY() - mouseEvent.getSceneY();
      }
    });

    circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        circle.setCenterX(mouseEvent.getSceneX() + dragDeltaX);
        circle.setCenterY(mouseEvent.getSceneY() + dragDeltaY);
        circle.setCursor(Cursor.MOVE);
      }
    });

    circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        circle.setCursor(Cursor.HAND);
      }
    });

    circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        circle.setCursor(Cursor.HAND);
      }
    });
  }
}

public class DrawingPaneDemo extends TestApplication {
  @Override
  public void start(Stage primaryStage) {
    ImageView imageView = super.getImageView();
    Polygon poly = new Polygon( 10, 10, 100, 10, 200, 100, 50, 200 );
    DrawingPane pane = new DrawingPane(imageView,poly);
    imageView.fitWidthProperty().bind(primaryStage.widthProperty());
    imageView.fitHeightProperty().bind(primaryStage.heightProperty());
    createSceneAndShowStage(primaryStage, pane);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
