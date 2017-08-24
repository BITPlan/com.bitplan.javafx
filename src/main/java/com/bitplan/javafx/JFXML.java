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

import java.net.URL;
import java.util.ResourceBundle;

import com.bitplan.error.ErrorHandler;
import com.bitplan.error.ExceptionHandler;
import com.bitplan.gui.App;
import com.bitplan.i18n.Translator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * FXML loader utility that can integrate between the platform independent
 * Application (App) concept and FXML
 * 
 * @author wf
 *
 */
public class JFXML {

  private String resourcePath;
  private Stage stage;
  private App app;

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  /**
   * create an FXML loader for the given resourcePath
   * 
   * @param resourcePath
   */
  public JFXML(String resourcePath, Stage stage, App app) {
    this.stage = stage;
    this.app = app;
    this.resourcePath = resourcePath;
  }

  /**
   * Load a Result
   * 
   * @param <T>
   */
  public class LoadResult<T> {
    Parent parent;
    private T controller;

    public T getController() {
      return controller;
    }

    public void setController(T controller) {
      this.controller = controller;
    }
  }

  /**
   * load me from the given fxml fileName
   * 
   * @param fxmlFileName
   * @return the load Result
   */
  public <T> LoadResult<T> load(String fxmlFileName) {
    try {
      LoadResult<T> loadResult = new LoadResult<T>();
      ResourceBundle resourceBundle = Translator.getBundle();
      URL fxml = JFXWizardPane.class
          .getResource(resourcePath + fxmlFileName + ".fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(fxml, resourceBundle);
      loadResult.parent = fxmlLoader.load();
      loadResult.setController(fxmlLoader.getController());
      return loadResult;
    } catch (Throwable th) {
      ErrorHandler.handle(th);
    }
    return null;
  }

  /**
   * load the presenter for the given entityName
   * 
   * @param entityName
   * @param clazz
   *          - the runtime class
   * @param exceptionHandler
   * @return the presenter
   */
  public <T extends BasePresenter<V>, V> T loadPresenter(String entityName,
      Class<V> clazz, ExceptionHandler exceptionHandler) {
    LoadResult<T> loadResult = load(entityName);
    T presenter = loadResult.controller;
    presenter.setParent(loadResult.parent);
    presenter.init(stage, app, exceptionHandler);
    presenter.setClazz(clazz);
    return presenter;
  }
}
