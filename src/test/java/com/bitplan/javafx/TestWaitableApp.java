/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except the ones in the stackoverflow package
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

import java.util.logging.Logger;

import org.junit.Ignore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * test the main class handling
 * 
 * @author wf
 *
 */
public class TestWaitableApp {
  public static class MainApp extends WaitableApp {
    protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
    public static MainApp instance;
    public MainApp() {
      // LOGGER.log(Level.INFO,"constructor called");
      instance=this;
    }
    
    @Override
    public void start(Stage primaryStage)  {
      super.start(primaryStage);
      primaryStage.setTitle("Main Application");
      Label lbl = new Label();
      lbl.setText("I am started as an Application");
      StackPane root = new StackPane();
      root.getChildren().add(lbl);
      primaryStage.setScene(new Scene(root, 300, 250));
      primaryStage.show();
      stage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });
    }

  }

  @Ignore
  // makes travis choke ...
  public void testMainLaunch() throws Exception {
    String args[] = {};
    new Thread(() -> Application.launch(MainApp.class, args)).start();
    System.out.println("application started");
    while (MainApp.instance==null)
      Thread.sleep(10);
    MainApp mainApp = MainApp.instance;
    mainApp.waitOpen();
    System.out.println("application open");
    Thread.sleep(1000);
    System.out.println("application to be closed");
    Platform.runLater(()->mainApp.stage.close());
    System.out.println("waiting for close");
    mainApp.waitClose();
    System.out.println("closed");
  }
}
