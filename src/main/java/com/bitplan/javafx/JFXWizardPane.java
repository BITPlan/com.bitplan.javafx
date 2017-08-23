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

import java.util.logging.Logger;

import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import com.bitplan.error.ExceptionHandler;
import com.bitplan.gui.Linker;
import com.bitplan.i18n.Translator;
import com.bitplan.javafx.JFXML.LoadResult;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 * a page in a wizard
 * 
 * @author wf
 *
 */
public class JFXWizardPane extends WizardPane implements ExceptionHandler {
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  public static Linker linker;
 
  /**
   * 
   */
  ImageSelector<String> selector;
  private String i18nTitle;
  private int step;
  private int steps;
  protected Object controller;
  private GlyphFont fontAwesome;
  private JFXWizard wizard;
  private Button helpButton;
  private ConstrainedGridPane gridPane;
  private Node contentNode;
  private static ButtonType helpButtonType;

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public int getSteps() {
    return steps;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }

  public String getI18nTitle() {
    return i18nTitle;
  }

  public void setI18nTitle(String i18nTitle) {
    this.i18nTitle = i18nTitle;
    refreshI18n();
  }
  
  public static void setLinker(Linker pLinker) {
    linker=pLinker;
  }

  /**
   * construct me with the given title
   * 
   * @param i18nTitle
   */
  public JFXWizardPane(JFXWizard wizard,int step, int steps, String i18nTitle) {
    this.wizard=wizard;
    fontAwesome = GlyphFontRegistry.font("FontAwesome");
    if (helpButtonType == null)
      helpButtonType = new ButtonType(Translator.translate("help"),
          ButtonBar.ButtonData.HELP);
    this.setStep(step);
    this.setSteps(steps);
    gridPane=new ConstrainedGridPane();
    gridPane.fixColumnSizes(0, 100);
    gridPane.fixRowSizes(0,100);
    setContent(gridPane);
    this.setI18nTitle(i18nTitle);
  }

  /**
   * set the Help
   * 
   * @param help
   */
  public void setHelp(String help) {
    //this.getButtonTypes().add(helpButtonType);
    //helpButton = this.findButton(helpButtonType);
    helpButton=new Button(wizard.getI18n("help"));
    //wizard.getPrivateDialog().getDialogPane().getButtonTypes().add(helpButtonType);
    //getPrivateButtonBar().getButtons().add(0, helpButton);
    gridPane.add(helpButton, 0, 1);
    Glyph helpIcon = fontAwesome.create(FontAwesome.Glyph.QUESTION_CIRCLE);
    helpButton.setGraphic(helpIcon);
    
    helpButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(final ActionEvent actionEvent) {
        linker.browse(help);
        actionEvent.consume();
      }
    });
  }

  /**
   * refresh my Internationalization content
   */
  public void refreshI18n() {
    setHeaderText(wizard.getI18n("welcomeStep", getStep(), getSteps()) + "\n\n"
        + wizard.getI18n(i18nTitle));
    this.fixButtons();
  }

  /**
   * construct me with the given title and selector
   * 
   * @param i18nTitle
   * @param selector
   */
  public JFXWizardPane(JFXWizard wizard,int step, int steps, String i18nTitle,
      final ImageSelector<String> selector) {
    this(wizard,step, steps, i18nTitle);
    this.selector = selector;
    setContentNode(selector);
  }

  /**
   * fix the encoding problems with controlfx buttons
   * https://bitbucket.org/controlsfx/controlsfx/issues/769/encoding-problem-all-german-umlauts-are
   */
  protected void fixButtons() {
    ButtonType buttonTypes[] = { ButtonType.NEXT, ButtonType.PREVIOUS,
        ButtonType.CANCEL, ButtonType.FINISH };
    Glyph glyphs[] = { fontAwesome.create(FontAwesome.Glyph.CHEVRON_RIGHT),
        fontAwesome.create(FontAwesome.Glyph.CHEVRON_LEFT),
        fontAwesome.create(FontAwesome.Glyph.TIMES),
        fontAwesome.create(FontAwesome.Glyph.CHECK) };
    int index = 0;
    for (ButtonType buttonType : buttonTypes) {
      Button button = findButton(buttonType);
      if (button != null) {
        button.setText(buttonType.getText());
        button.setGraphic(glyphs[index]);
      }
      index++;
    }
    if (helpButton!=null)
      helpButton.setText(wizard.getI18n("help"));
  }

  /**
   * get the Button for the given buttonType
   * 
   * @return the button
   */
  public Button findButton(ButtonType buttonType) {
    /* Dialog dialog=wizard.getPrivateDialog();
    return (Button) dialog.getDialogPane().lookupButton(buttonType);
    */
   
    for (Node node : getChildren()) {
      if (node instanceof ButtonBar) {
        ButtonBar buttonBar = (ButtonBar) node;
        ObservableList<Node> buttons = buttonBar.getButtons();
        for (Node buttonNode : buttons) {
          Button button = (Button) buttonNode;
          @SuppressWarnings("unchecked")
          ObjectProperty<ButtonData> prop = (ObjectProperty<ButtonData>) button
              .getProperties().get("javafx.scene.control.ButtonBar.ButtonData");
          ButtonData buttonData = prop.getValue();
          if (buttonData.equals(buttonType.getButtonData())) {
            return button;
          }
        }
      }
    }
    // LOGGER.log(Level.WARNING,"find Button failed in step "+step+" of "+steps);
    return null;
  }

  @Override
  public void onEnteringPage(Wizard wizard) {
    fixButtons();
  }

  @Override
  public void onExitingPage(Wizard wizard) {
    if (selector != null)
      wizard.getSettings().put(selector.getTitle(), selector.getSelection());
  }

  public void setController(Object controller) {
    this.controller = controller;
  }  
  
  /**
   * load the controller for the given fxmlFileName
   * @param fxmlFileName
   * @return - the fxmlFileName
   */
  public <T> T load(String fxmlFileName) {
    LoadResult<T> loadResult=wizard.getFxml().load(fxmlFileName);
    this.setContentNode(loadResult.parent);
    controller=loadResult.getController();
    return loadResult.getController();
  }
  
  /**
   * accessor for contentNode
   * @return -the content Node
   */
  public Node getContentNode() {
    return contentNode;
  }
  
  /**
   * set the Content (since setContent of DialogPane is final this function has a different name)
   * this is a work around to have room for a help button so a grid is interwoven between the dialogpane
   * and the content we'd like to show
   * @param parent
   */
  public void setContentNode(Node parent) {
    if (contentNode!=null)
      gridPane.getChildren().remove(contentNode);
    gridPane.add(parent, 0, 0);
    contentNode=parent;
    // resize the dialog
    // https://stackoverflow.com/a/31208445/1497139
    this.wizard.getPrivateDialog().getDialogPane().getScene().getWindow().sizeToScene();
  }
  
  /**
   * handle an Exception or a Throwable
   * 
   * @param th
   */
  public void handleException(Throwable th) {
    load("exception");
    setI18nTitle("problem_occured");
    ExceptionController exceptionController = (ExceptionController) controller;
    exceptionController.handleException(th);
  }
  
  /**
   * get the button Bar
   * @return the button bar
   */
  public ButtonBar getPrivateButtonBar() {
    Node buttonBar=wizard.getPrivate(DialogPane.class,"buttonBar",this);
    return (ButtonBar) buttonBar;
  }

}