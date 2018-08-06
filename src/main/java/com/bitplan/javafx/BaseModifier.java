package com.bitplan.javafx;

import com.bitplan.error.ExceptionHandler;
import com.bitplan.gui.App;
import com.bitplan.gui.Presenter;
import com.bitplan.json.ValueMap;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Base class for Modifying via views
 * @author wf
 *
 * @param <T>
 */
public abstract class BaseModifier<T> implements Presenter<T> {

  private Stage stage;
  private App app;
  private Parent parent;
  Class<T> clazz; // runtime Class Information
  private T model;
  private ExceptionHandler exceptionHandler;
  private ValueMap valueMap;
  
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
  
  /**
   * @return the model
   */
  public T getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(T model) {
    this.model = model;
  }

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

  public void setView(ValueMap valueMap) {
    this.valueMap=valueMap;
  }
  
  @Override
  public ValueMap getView() {
    return valueMap;
  }

}
