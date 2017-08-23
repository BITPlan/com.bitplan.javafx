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

import com.bitplan.error.ExceptionHandler;
import com.bitplan.gui.App;
import com.bitplan.gui.Presenter;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Base Presenter class
 * @author wf
 *
 */
public abstract class BasePresenter<T> implements Presenter<T>, Initializable {
 
  private Stage stage;
  private App app;
  private Parent parent;
  Class<T> clazz; // runtime Class Information
  private ExceptionHandler exceptionHandler;

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

  public Parent getParent() {
    return parent;
  }

  public void setParent(Parent parent) {
    this.parent = parent;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  public void setClazz(Class<T> clazz) {
    this.clazz = clazz;
  }

  public ExceptionHandler getExceptionHandler() {
    return exceptionHandler;
  }

  public void setExceptionHandler(ExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * initialize me
   * @param stage
   * @param app
   * @param exceptionHandler
   */
  public void init(Stage stage, App app, ExceptionHandler exceptionHandler) {
    preInit();
    this.stage=stage;
    this.app=app;
    this.exceptionHandler=exceptionHandler;
    postInit();
  }
  
  public void preInit() {}; // override if you like
  public void postInit(){}; // override if you like
  abstract public void show(T model);
}
