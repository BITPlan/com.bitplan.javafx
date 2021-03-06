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
package com.bitplan.javafx;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 * RubberBandSelection
 * 
 * @author Roland see
 *         https://stackoverflow.com/questions/30295071/how-to-create-stackpane-on-the-drawn-rectangle-area
 * 
 * @author wf
 *
 */
public class RubberBandSelection {

  /**
   * remember the selection with it's relative bounds
   * 
   * @author wf
   *
   */
  public class Selection {
    Node node;
    Bounds relativeBounds;

    /**
     * create a selection from the given
     * 
     * @param node
     * @param rect
     */
    public Selection(Node node, Rectangle rect) {
      Bounds pB = parent.getBoundsInParent();
      this.node = node;
      double rx = rect.getX() / pB.getWidth();
      double ry = rect.getY() / pB.getHeight();
      double rw = rect.getWidth() / pB.getWidth();
      double rh = rect.getHeight() / pB.getHeight();
      relativeBounds = new BoundingBox(rx, ry, rw, rh);
    }

    /**
     * get a string with my relative positions in percent
     * 
     * @return String with x,y,w,h formatted with
     */
    public String asPercent() {
      String pStr = String.format("x: %4.1f%% y: %4.1f%% w: %4.1f%% h: %4.1f%%",
          relativeBounds.getMinX() * 100, relativeBounds.getMinY() * 100,
          relativeBounds.getHeight() * 100, relativeBounds.getWidth() * 100);
      return pStr;
    }
  }

  /**
   * 
   */
  final DragContext dragContext = new DragContext();
  Rectangle rect;
  Map<Node, Selection> selected = new HashMap<Node, Selection>();

  Parent parent;

  /**
   * construct me for the given
   * 
   * @param parent
   */
  public RubberBandSelection(Parent parent) {

    this.parent = parent;

    rect = new Rectangle(0, 0, 0, 0);
    rect.setStroke(Color.BLUE);
    rect.setStrokeWidth(1);
    rect.setStrokeLineCap(StrokeLineCap.ROUND);
    rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

    parent.addEventHandler(MouseEvent.MOUSE_PRESSED,
        onMousePressedEventHandler);
    parent.addEventHandler(MouseEvent.MOUSE_DRAGGED,
        onMouseDraggedEventHandler);
    parent.addEventHandler(MouseEvent.MOUSE_RELEASED,
        onMouseReleasedEventHandler);
  }
  
  /**
   * get the click position for a given mouse event
   * @param event
   * @return the click position
   */
  public Point2D clickPos(MouseEvent event) {
    Point2D click=new Point2D(event.getX(),event.getY());
    // new Point2D(event.getSceneX(),event.getSceneY());
    return click;
  }

  EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

    @Override
    public void handle(MouseEvent event) {
      Point2D click = clickPos(event);
      dragContext.mouseAnchorX = click.getX();
      dragContext.mouseAnchorY = click.getY();

      rect.setX(dragContext.mouseAnchorX);
      rect.setY(dragContext.mouseAnchorY);
      rect.setWidth(0);
      rect.setHeight(0);
      add(rect, rect, false);
    }
  };

  EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

    @Override
    public void handle(MouseEvent event) {

      select(rect);

      // remove rubber band
      rect.setX(0);
      rect.setY(0);
      rect.setWidth(0);
      rect.setHeight(0);

      remove(rect, false);

    }
  };

  EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

    @Override
    public void handle(MouseEvent event) {
      Point2D click = clickPos(event);
      double offsetX = click.getX() - dragContext.mouseAnchorX;
      double offsetY = click.getY() - dragContext.mouseAnchorY;

      if (offsetX > 0)
        rect.setWidth(offsetX);
      else {
        rect.setX(click.getX());
        rect.setWidth(dragContext.mouseAnchorX - rect.getX());
      }

      if (offsetY > 0) {
        rect.setHeight(offsetY);
      } else {
        rect.setY(click.getY());
        rect.setHeight(dragContext.mouseAnchorY - rect.getY());
      }
    }
  };

  private final class DragContext {

    public double mouseAnchorX;
    public double mouseAnchorY;

  }

  public ObservableList<Node> getChildren() {
    if (parent instanceof Pane) {
      return ((Pane) parent).getChildren();
    } else if (parent instanceof Group) {
      return ((Group) parent).getChildren();
    } else {
      return parent.getChildrenUnmodifiable();
    }
  }

  /**
   * add the given node
   * 
   * @param node
   * @param rect
   * @param remember
   */
  protected void add(Node node, Rectangle rect, boolean remember) {
    addNode(node);
    if (remember)
      this.selected.put(node, new Selection(node, rect));
  }

  /**
   * remove the given node
   * 
   * @param node
   */
  protected void remove(Node node, boolean remember) {
    removeNode(node);
    if (remember)
      this.selected.remove(node);
  }

  public void addNode(Node node) {
    getChildren().add(node);
  }

  public void removeNode(Node node) {
    getChildren().remove(node);
  }

  /**
   * select the given rectangle
   * 
   * @param rect
   */
  public void select(Rectangle rect) {
    // get coordinates
    double x = rect.getX();
    double y = rect.getY();
    double w = rect.getWidth();
    double h = rect.getHeight();

    // create button
    Button button = new Button();
    button.setDefaultButton(false);
    button.setPrefSize(w, h);
    button.setText("");
    button.setStyle(
        "-fx-border-color: blue;-fx-background-color: rgba(0,0,0, 0.3);");
    button.setLayoutX(x);
    button.setLayoutY(y);
    button.setOnAction(e -> {
      remove((Control) e.getSource(), true);
    });
    add(button, rect, true);
  }

}