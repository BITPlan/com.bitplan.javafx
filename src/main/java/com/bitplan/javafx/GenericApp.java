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

import com.bitplan.error.SoftwareVersion;
import com.bitplan.gui.App;
import com.bitplan.gui.Form;
import com.bitplan.gui.Group;
import com.bitplan.i18n.Translator;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Generic Application
 * @author wf
 *
 */
public class GenericApp extends WaitableApp {
  public static boolean debug = false;
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
  public static boolean testMode = false;
  protected com.bitplan.gui.App app;
  protected SoftwareVersion softwareVersion;
  protected Map<String, GenericControl> controls;
  protected XYTabPane xyTabPane;
  private Map<String, GenericPanel> panels = new HashMap<String, GenericPanel>();
  public int ICON_SIZE = 64;
  
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
  
  /**
   * setup the given Application adds tabPanes to the tabPaneByView map
   * 
   * @param app
   */
  public void setup(App app) {
    controls = new HashMap<String, GenericControl>();
    for (Group group : app.getGroups()) {
      TabPane tabPane = xyTabPane.addTabPane(group.getId(),Translator.translate(group.getName()),group.getIcon());
      for (Form form : group.getForms()) {
        GenericPanel panel = new GenericPanel(stage, form);
        panels.put(form.getId(), panel);
        controls.putAll(panel.controls);
        xyTabPane.addTab(tabPane, form.getId(),Translator.translate(form.getTitle()), form.getIcon(),panel);
      }
    }
  }
  
  /**
   * construct me
   * @param app
   * @param softwareVersion
   */
  public GenericApp(App app, SoftwareVersion softwareVersion) {
    toolkitInit();
    xyTabPane=new XYTabPane(ICON_SIZE);
    // new JFXPanel();
    this.setApp(app);
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
   * select a random tab
   */
  public void selectRandomTab() {
    this.xyTabPane.selectRandomTab();
  }

  /**
   * get the tab of the given view with the given id
   * 
   * @param view
   * @param tabId
   * @return - the tab
   */
  public Tab getTab(String tabId) {
    Tab tab = xyTabPane.getTab(tabId);
    return tab;
  }
  
  @Override
  public void start(Stage stage) {
    super.start(stage);
    stage.setTitle(
        softwareVersion.getName() + " " + softwareVersion.getVersion());
  }

}
