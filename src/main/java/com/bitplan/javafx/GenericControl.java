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

import java.io.File;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bitplan.gui.Field;
import com.bitplan.gui.Form;
import com.bitplan.gui.ValueHolder;
import com.bitplan.i18n.I18n;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * generic wrapper for JavaFX controls
 * 
 * @author wf
 *
 */
@SuppressWarnings("restriction")
public class GenericControl implements ValueHolder {
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  public static final String INTEGER_MATCH="[0-9]*";
  public static final String DOUBLE_MATCH="[0-9]*(\\.[0-9]*)?";
  Control control;
  Label label;
  private TextField textField;

  Button button;
  Field field;
  ChoiceBox<String> choiceBox;
  private Tooltip tooltip;
  private CheckBox checkBox;
  protected Stage stage;
  private FileChooser fileChooser;
  private DirectoryChooser directoryChooser;
  public static final boolean debug = false;

  public Control getControl() {
    return control;
  }

  public void setControl(Control control) {
    this.control = control;
  }

  /**
   * get the column Count estimate for the given field
   * 
   * @param field
   * @return - the column count
   */
  public int getColumnCount(Field field) {
    int columnCount = 0;
    if (field.getFieldSize() != null) {
      // heuristic correction of columns specified in generic description
      // versus Java FX interpretation - the columns are too wide as of
      // 2017-06-28
      columnCount = field.getFieldSize() * 4 / 6;
    }
    return columnCount;
  }
  
  /**
   * create a new generic Control from the given control
   * @param stage
   * @param control
   */
  public GenericControl(Stage stage, Control control) {
    this.stage=stage;
    this.control=control;
  }

  /**
   * create a generic control for the given field
   * 
   * @param stage
   * @param field
   */
  public GenericControl(Stage stage, Field field) {
    this.stage = stage;
    this.field = field;
    Form form=field.getForm();
    String i18n=field.getLabel();
    if (form!=null) {
      String ti18n=I18n.get(field.getId());
      if (ti18n!=null  && !"".equals(ti18n) && !field.getId().equals(ti18n))
        i18n=ti18n;
    }
    String fieldType = field.getType();
    if (fieldType == null || "Integer".equals(fieldType) || "Long".equals(fieldType)
        || "Double".equals(fieldType) || "Password".equals(fieldType)
        || "File".equals(fieldType) || "Directory".equals(fieldType)) {
      if ("Password".equals(fieldType)) {
        textField = new PasswordField();
      } else {
        textField = new TextField();
      }
      textField.setPromptText(i18n);
      int columnCount = this.getColumnCount(field);
      if (columnCount > 0)
        textField.setPrefColumnCount(columnCount);
      control = textField;
    } else if ("Choice".equals(fieldType)) {
      choiceBox = new ChoiceBox<String>();
      choiceBox.getItems().addAll(field.getChoices().split(","));
      control = choiceBox;
    } else if ("Boolean".equals(fieldType)) {
      checkBox = new CheckBox();
      control = checkBox;
    } else {
      throw new IllegalArgumentException(
          String.format("Unsupported field type %s", fieldType));
    }
    // force numeric content for integer fields
    if ("Integer".equals(fieldType) || "Long".equals(fieldType) || "Double".equals(fieldType)) {
      // force numeric content
      // https://stackoverflow.com/a/36436243/1497139
      String matchExpr = "";
      switch (fieldType) {
      case "Integer":
      case "Long":
        matchExpr = INTEGER_MATCH;
        break;
      case "Double":
        matchExpr = DOUBLE_MATCH;
        break;
      }
      final String match = matchExpr;
      UnaryOperator<Change> filter = change -> {
        String text = change.getText();

        if (text.matches(match)) {
          return change;
        }

        return null;
      };
      TextFormatter<String> textFormatter = new TextFormatter<>(filter);
      textField.setTextFormatter(textFormatter);
    }
    //
    if ("File".equals(fieldType) || ("Directory").equals(fieldType)) {
      if ("File".equals(fieldType))
        setFileChooser(new FileChooser());
      else
        setDirectoryChooser(new DirectoryChooser());
      button = new Button("...");
      button.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent e) {
          File file;
          if ("File".equals(fieldType))
            file = getFileChooser().showOpenDialog(stage);
          else
            file = getDirectoryChooser().showDialog(stage);
          if (file != null) {
            textField.setText(file.getPath());
          }
        }
      });
    }
    if (field.getHint() != null) {
      tooltip = new Tooltip();
      tooltip.setText(field.getHint());
      control.setTooltip(tooltip);
    }
    label = new Label(i18n + ":");
  }

  /**
   * set whether the control is editable
   * 
   * @param b
   */
  public void setEditable(boolean b) {
    if (control instanceof TextField) {
      textField.setEditable(b);
    } else {
      control.setDisable(!b);
    }
  }

  /**
   * set the value of this control
   * 
   * @param value
   */
  public void setValue(Object value) {
    if (control instanceof TextField) {
      if (value != null) {
        String valueText = value.toString();
        // workaround gson returning doubles in the map
        if ("Integer".equals(field.getType()) && (value instanceof Double)) {
          Double d = (Double) value;
          valueText = "" + d.intValue();
        }
        if (debug)
          LOGGER.log(Level.INFO, field.getId() + "=" + valueText + "("
              + value.getClass().getSimpleName() + ")");
        // valueText+="("+updateCount+")";
        textField.setText(valueText);
      }
    } else if (control instanceof CheckBox) {
      if (value != null)
        checkBox.setSelected((boolean) value);
      else
        checkBox.setIndeterminate(true);
    } else if (control instanceof ChoiceBox) {
      if (value == null) {
        choiceBox.getSelectionModel().clearSelection();
      } else {
        choiceBox.getSelectionModel().select((String) value);
      }
    }
  }

  /**
   * set the toolTip
   * 
   * @param toolTip
   */
  public void setToolTip(String toolTip) {
    if (control.getTooltip() == null)
      control.setTooltip(new Tooltip());
    control.getTooltip().setText(toolTip);
  }

  /**
   * get the value of a control
   * 
   * @return the value
   */
  public Object getValue() {
    if (control instanceof TextField) {
      if ("Integer".equals(field.getType())) {
        if (textField.getText().isEmpty())
          return null;
        Integer result = Integer.parseInt(textField.getText());
        return result;
      } else if ("Long".equals(field.getType())) {
        if (textField.getText().isEmpty())
          return null;
        Long result = Long.parseLong(textField.getText());
        return result;
      } else if ("Double".equals(field.getType())) {
        if (textField.getText().isEmpty())
          return null;
        Double result = Double.parseDouble(textField.getText());
        return result;
      } else {
        return textField.getText();
      }
    } else if (control instanceof CheckBox) {
      if (checkBox.isIndeterminate())
        return null;
      else
        return checkBox.isSelected();
    } else if (control instanceof ChoiceBox) {
      return choiceBox.getSelectionModel().getSelectedItem();
    }
    return null;
  }

  /**
   * create a generic control
   * 
   * @param stage
   * @param field
   * @return the new generic control
   */
  public static GenericControl create(Stage stage, Field field) {
    GenericControl gcontrol = new GenericControl(stage, field);
    return gcontrol;
  }

  /**
   * accessor for FileChooser
   * 
   * @return the fileChooser
   */
  public FileChooser getFileChooser() {
    return fileChooser;
  }

  public void setFileChooser(FileChooser fileChooser) {
    this.fileChooser = fileChooser;
  }

  public DirectoryChooser getDirectoryChooser() {
    return directoryChooser;
  }

  public void setDirectoryChooser(DirectoryChooser directoryChooser) {
    this.directoryChooser = directoryChooser;
  }

}
