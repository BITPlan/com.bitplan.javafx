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
import java.util.logging.Logger;

import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;

import com.bitplan.error.ExceptionHandler;
import com.bitplan.error.SoftwareVersion;
import com.bitplan.gui.App;
import com.bitplan.gui.Form;
import com.bitplan.gui.Group;
import com.bitplan.gui.Linker;
import com.bitplan.i18n.I18n;
import com.bitplan.i18n.Translator;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Generic Application
 * 
 * @author wf
 *
 */
@SuppressWarnings("restriction")
public abstract class GenericApp extends WaitableApp
    implements ExceptionHandler, Linker, EventHandler<ActionEvent> {
 
  protected com.bitplan.gui.App app;
  protected SoftwareVersion softwareVersion;
  protected Map<String, GenericControl> controls;
  protected XYTabPane xyTabPane;
  private Map<String, GenericPanel> panels = new HashMap<String, GenericPanel>();
  public int ICON_SIZE = 64;
  private JFXML fxml;
  private String resourcePath;
  private Scene scene;
  private Button hideMenuButton;
  private MenuBar menuBar;
  private VBox root;

  public Scene getScene() {
    return scene;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public VBox getRoot() {
    return root;
  }

  public void setRoot(VBox root) {
    this.root = root;
  }

  /**
   * @return the menuBar
   */
  public MenuBar getMenuBar() {
    return menuBar;
  }

  /**
   * @param menuBar
   *          the menuBar to set
   */
  public void setMenuBar(MenuBar menuBar) {
    this.menuBar = menuBar;
  }

  public XYTabPane getXyTabPane() {
    return xyTabPane;
  }

  public void setXyTabPane(XYTabPane xyTabPane) {
    this.xyTabPane = xyTabPane;
  }

  public SoftwareVersion getSoftwareVersion() {
    return softwareVersion;
  }

  public void setSoftwareVersion(SoftwareVersion softwareVersion) {
    this.softwareVersion = softwareVersion;
  }

  public com.bitplan.gui.App getApp() {
    return app;
  }

  public void setApp(com.bitplan.gui.App app) {
    this.app = app;
  }

  public JFXML getFxml() {
    return fxml;
  }

  public void setFxml(JFXML fxml) {
    this.fxml = fxml;
  }

  public Map<String, GenericPanel> getPanels() {
    return panels;
  }

  public void setPanels(Map<String, GenericPanel> panels) {
    this.panels = panels;
  }

  /**
   * internationalization function
   * 
   * @param params
   * @param text
   * @return translated text
   */
  public String i18n(String text, Object... params) {
    String i18n = I18n.get(text, params);
    return i18n;
  }

  /**
   * show or hide the menuBar
   * 
   * @param scene
   * @param pMenuBar
   */
  public void showMenuBar(Scene scene, MenuBar pMenuBar, boolean show) {
    Parent sroot = scene.getRoot();
    ObservableList<Node> rootChilds = null;
    if (sroot instanceof VBox)
      rootChilds = ((VBox) sroot).getChildren();
    if (rootChilds == null)
      throw new RuntimeException(
          "showMenuBar can not handle scene root of type "
              + sroot.getClass().getName());
    if (!show && rootChilds.contains(pMenuBar)) {
      rootChilds.remove(pMenuBar);
    } else if (show) {
      rootChilds.add(0, pMenuBar);
    }
    pMenuBar.setVisible(show);
    hideMenuButton.setText(show ? "hide menu" : "show menu");
  }

  /**
   * create the Menu Bar
   * 
   * @param scene
   */
  public MenuBar createMenuBar(Scene scene, App app) {
    MenuBar lMenuBar = new MenuBar();
    for (com.bitplan.gui.Menu amenu : app.getMainMenu().getSubMenus()) {
      Menu menu = new Menu(i18n(amenu.getId()));
      lMenuBar.getMenus().add(menu);
      for (com.bitplan.gui.MenuItem amenuitem : amenu.getMenuItems()) {
        MenuItem menuItem = new MenuItem(i18n(amenuitem.getId()));
        menuItem.setOnAction(this);
        menuItem.setId(amenuitem.getId());
        menu.getItems().add(menuItem);
      }
    }

    hideMenuButton = new Button("hide menu");
    hideMenuButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        showMenuBar(scene, lMenuBar, !lMenuBar.isVisible());
      }
    });
    return lMenuBar;
  }

  /**
   * setup the given Application adds tabPanes to the tabPaneByView map
   * 
   * @param app
   */
  public void setup(App app) {
    controls = new HashMap<String, GenericControl>();
    for (Group group : app.getGroups()) {
      TabPane tabPane = xyTabPane.addTabPane(group.getI18nId(),
          Translator.translate(group.getI18nId()), group.getIcon());
      for (Form form : group.getForms()) {
        GenericPanel panel = new GenericPanel(stage, form);
        getPanels().put(form.getI18nId(), panel);
        controls.putAll(panel.controls);
        xyTabPane.addTab(tabPane, form.getI18nId(),
            Translator.translate(form.getI18nId()), form.getIcon(), panel);
      }
    }
    Button powerButton = xyTabPane.getTopLeftButton();
    Node icon = xyTabPane.getIcon(FontAwesome.Glyph.POWER_OFF.name(),
        xyTabPane.getIconSize());
    powerButton.setTooltip(new Tooltip(I18n.get(JavaFxI18n.POWER_OFF)));
    powerButton.setGraphic(icon);
    powerButton.setDisable(false);
    powerButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        close();
      }
    });
  }

  /**
   * setup the xyTabPane and it's behavior
   */
  public void setupXyTabPane() {
    // add the xyTabPane
    getRoot().getChildren().add(xyTabPane);
    // make sure it shrinks and grows with the scene
    xyTabPane.getvTabPane().prefHeightProperty()
        .bind(getStage().heightProperty().add(-xyTabPane.getTabSize()));
    xyTabPane.getvTabPane().prefWidthProperty()
        .bind(getStage().widthProperty().add(-xyTabPane.getTabSize()));
  }

  /**
   * construct me
   * 
   * @param app
   * @param softwareVersion
   */
  public GenericApp(App app, SoftwareVersion softwareVersion,
      String resourcePath) {
    toolkitInit();
    // new JFXPanel();
    double screenHeight = getScreenHeight();
    // 839
    ICON_SIZE = (int) Math.round(screenHeight / 13);
    // LOGGER.log(Level.INFO,"screenHeight= "+screenHeight+" icon
    // size="+ICON_SIZE);
    xyTabPane = new XYTabPane(ICON_SIZE);
    this.setApp(app);
    this.resourcePath = resourcePath;
    this.setSoftwareVersion(softwareVersion);
    ExceptionController.setExceptionHelper(app);
    ExceptionController.setSoftwareVersion(softwareVersion);
    ExceptionController.setLinker(this);
    JFXWizardPane.setLinker(this);
  }

  /**
   * show the given notification
   * 
   * @param title
   * @param text
   * @param milliSecs
   */
  public static void showNotification(String title, String text,
      int milliSecs) {
    Notifications notification = Notifications.create();
    notification.hideAfter(new Duration(milliSecs));
    notification.title(title);
    notification.text(text);
    Platform.runLater(() -> notification.showInformation());
  }

  /**
   * get the title of the active Panel
   * 
   * @return - the activeTab
   */
  public Tab getActiveTab() {
    return xyTabPane.getSelectedTab();
  }

  public void setActiveTabPane(String groupId) {
    xyTabPane.selectVTab(groupId);
  }

  /**
   * close this display
   */
  @Override
  public void close() {
    if (stage != null)
      Platform.runLater(() -> stage.close());
    // we do not wait and we do not set stage to null
  }

  /**
   * get the tab with the given tabId
   * 
   * @param tabId
   * @return - the tab
   */
  public Tab getTab(String tabId) {
    Tab tab = xyTabPane.getTab(tabId);
    return tab;
  }

  /**
   * select the given tab
   * 
   * @param tabId
   */
  public void selectTab(String tabId) {
    xyTabPane.selectTab(tabId);
  }

  /**
   * select a random tab
   */
  public void selectRandomTab() {
    this.xyTabPane.selectRandomTab();
  }

  @Override
  public void start(Stage stage) {
    super.start(stage);
    fxml = new JFXML(resourcePath, stage, app);
    stage.setTitle(
        softwareVersion.getName() + " " + softwareVersion.getVersion());
  }

  /**
   * handle the given exception
   * 
   * @param th
   */
  public void handleException(Throwable th) {
    Platform.runLater(
        () -> GenericDialog.showException(stage, Translator.translate("error"),
            Translator.translate("problem_occured"), th, this));
  }

  /**
   * show an About dialog
   */
  public void showAbout() {
    String headerText = softwareVersion.getName() + " "
        + softwareVersion.getVersion();
    GenericDialog.showAlert(stage, "About", headerText,
        softwareVersion.getUrl());
  }

  /**
   * browse to the link page
   */
  public Void showLink(String link) {
    try {
      this.browse(link);
    } catch (Exception e) {
      handleException(e);
    }
    return null;
  }

  @Override
  public abstract void handle(ActionEvent event);

}
