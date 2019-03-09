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

import com.bitplan.javafx.ImageViewPane;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

/**
 * see
 * https://stackoverflow.com/questions/30295071/how-to-create-stackpane-on-the-drawn-rectangle-area
 * 
 * @author Roland
 *
 */
public class RubberBandSelectionDemo extends TestApplication {

  CheckBox drawButtonCheckBox;

  @Override
  public void start(Stage primaryStage) {
    ImageView imageView = super.getImageView();
    ImageViewPane imageViewPane=new ImageViewPane(imageView);
    Pane pane = new Pane();
    pane.getChildren().add(imageViewPane);
    
    drawButtonCheckBox = new CheckBox("Draw Button");
    pane.getChildren().add(drawButtonCheckBox);
    createSceneAndShowStage(primaryStage, pane);

    new RubberBandSelection(pane);

  }

  public class RubberBandSelection {

    final DragContext dragContext = new DragContext();
    Rectangle rect;

    Pane group;

    public RubberBandSelection(Pane group) {

      this.group = group;

      rect = new Rectangle(0, 0, 0, 0);
      rect.setStroke(Color.BLUE);
      rect.setStrokeWidth(1);
      rect.setStrokeLineCap(StrokeLineCap.ROUND);
      rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

      group.addEventHandler(MouseEvent.MOUSE_PRESSED,
          onMousePressedEventHandler);
      group.addEventHandler(MouseEvent.MOUSE_DRAGGED,
          onMouseDraggedEventHandler);
      group.addEventHandler(MouseEvent.MOUSE_RELEASED,
          onMouseReleasedEventHandler);

    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        dragContext.mouseAnchorX = event.getSceneX();
        dragContext.mouseAnchorY = event.getSceneY();

        rect.setX(dragContext.mouseAnchorX);
        rect.setY(dragContext.mouseAnchorY);
        rect.setWidth(0);
        rect.setHeight(0);
        add(rect);

      }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {

        // get coordinates
        double x = rect.getX();
        double y = rect.getY();
        double w = rect.getWidth();
        double h = rect.getHeight();

        if (drawButtonCheckBox.isSelected()) {

          // create button
          Button node = new Button();
          node.setDefaultButton(false);
          node.setPrefSize(w, h);
          node.setText("Button");
          node.setLayoutX(x);
          node.setLayoutY(y);
          add(node);

        } else {
          // create rectangle
          Rectangle node = new Rectangle(0, 0, w, h);
          node.setStroke(Color.BLACK);
          node.setFill(Color.BLACK.deriveColor(0, 0, 0, 0.3));
          node.setLayoutX(x);
          node.setLayoutY(y);
          add(node);
        }

        // remove rubberband
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(0);
        rect.setHeight(0);

        group.getChildren().remove(rect);

      }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {

        double offsetX = event.getSceneX() - dragContext.mouseAnchorX;
        double offsetY = event.getSceneY() - dragContext.mouseAnchorY;

        if (offsetX > 0)
          rect.setWidth(offsetX);
        else {
          rect.setX(event.getSceneX());
          rect.setWidth(dragContext.mouseAnchorX - rect.getX());
        }

        if (offsetY > 0) {
          rect.setHeight(offsetY);
        } else {
          rect.setY(event.getSceneY());
          rect.setHeight(dragContext.mouseAnchorY - rect.getY());
        }
      }
    };

    private final class DragContext {

      public double mouseAnchorX;
      public double mouseAnchorY;

    }

    protected void add(Node node) {
      group.getChildren().add(node);

    }
  }

  public static void main(String[] args) {
    launch(args);
  }

}
