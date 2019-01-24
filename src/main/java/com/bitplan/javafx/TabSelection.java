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

import java.io.File;

import com.bitplan.error.ErrorHandler;
import com.bitplan.json.JsonAble;
import com.bitplan.json.JsonManager;
import com.bitplan.json.JsonManagerImpl;

/**
 * allow remembering the current TabSelection
 * @author wf
 *
 */
public class TabSelection implements JsonAble {
  protected String tabPaneId;
  protected String tabId;
  
  public String getTabPaneId() {
    return tabPaneId;
  }
  public void setTabPaneId(String tabPaneId) {
    this.tabPaneId = tabPaneId;
  }
  public String getTabId() {
    return tabId;
  }
  public void setTabId(String tabId) {
    this.tabId = tabId;
  }
  
  static TabSelection instance;

  /**
   * get an instance of the TabSelection
   * 
   * @return - the instance
   * @throws Exception
   */
  public static TabSelection getInstance() {
    if (instance == null) {
      File jsonFile = JsonAble.getJsonFile(TabSelection.class.getSimpleName());
      if (jsonFile.canRead()) {
        JsonManager<TabSelection> jmTabSelection = new JsonManagerImpl<TabSelection>(
            TabSelection.class);
        try {
          instance = jmTabSelection.fromJsonFile(jsonFile);
        } catch (Exception e) {
          ErrorHandler.handle(e);
        }
      }
      if (instance == null)
        instance = new TabSelection();
    }
    return instance;
  }
  
}