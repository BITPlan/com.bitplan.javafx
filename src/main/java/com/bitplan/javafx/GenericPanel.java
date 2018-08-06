/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * Copyright 2017 BITPlan GmbH https://github.com/BITPlan
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

import com.bitplan.gui.Form;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * a generic Panel
 * 
 * @author wf
 *
 */
@SuppressWarnings("restriction")
public class GenericPanel extends GridPane {
  protected Form form;
  public Map<String, GenericControl> controls;
  
  /**
   * get the Fields for the given form
   * 
   * @param grid
   * @param form
   * @param ypos
   * @return - the Map of fields
   */
  public static Map<String, GenericControl> getFields(Stage stage,
      GridPane grid, Form form, int ypos) {
    Map<String, GenericControl> controls = new HashMap<String, GenericControl>();
    for (com.bitplan.gui.Field field : form.getFields()) {
      GenericControl gcontrol = GenericControl.create(stage, field);
      int x = 0;
      int y = ypos;
      if (field.getGridX() != null)
        x = field.getGridX();
      if (field.getGridY() != null)
        y = field.getGridY();
      grid.add(gcontrol.label, x, y);
      if (gcontrol.control != null) {
        grid.add(gcontrol.control, x + 1, y);
      }
      if (gcontrol.button != null) {
        grid.add(gcontrol.button, x + 2, y);
      }
      if (field.getGridY() == null)
        ypos++;
      controls.put(field.getId(), gcontrol);
    }
    return controls;
  }

  /**
   * construct me from the given form description
   * 
   * @param form
   */
  public GenericPanel(Stage stage,Form form) {
    this.form = form;
    setHgap(10);
    setVgap(10);
    setPadding(new Insets(20, 150, 10, 10));
    int ypos = 0;
    if (form.getReadOnly())
      setEditable(false);
    controls = getFields(stage,this, form, ypos);
  }
  
  /**
   * set all my controls to the given editAble state
   * @param editAble
   */
  public void setEditable(boolean editAble)  {
    for (GenericControl control : controls.values()) {
      control.setEditable(editAble);
    }
  }
  
  /**
   * get the control with given id
   * @param controlId
   * @return - the control
   */
  public GenericControl getControl(String controlId) {
    return controls.get(controlId);
  }

  /**
   * set my values from the given map
   * @param valueMap
   */
  public void setValues(Map<String, Object> valueMap) {
    for (GenericControl control : controls.values()) {
      String fieldId=control.field.getId();
      control.setValue(valueMap.get(fieldId));
    }
  }
  
  /**
   * get the values
   * 
   * @return the valueMap
   */
  public Map<String, Object> getValueMap() {
    Map<String, Object> result = new HashMap<String, Object>();
    for (com.bitplan.gui.Field field : form.getFields()) {
      GenericControl gcontrol = controls.get(field.getId());
      result.put(field.getId(), gcontrol.getValue());
    }
    return result;
  }
}
