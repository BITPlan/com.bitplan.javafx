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

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.junit.Before;
import org.junit.Test;

import com.bitplan.i18n.Translator;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/**
 * test the GUI
 * 
 * @author wf
 *
 */
public class TestGUI {
  static int SHOW_TIME = 4000;

  @Before
  public void initGUI() {
    WaitableApp.toolkitInit();
    Translator.initialize("i18n", "en");
  }

  @Test
  public void testIcons() throws Exception {
    GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    ConstrainedGridPane gridPane = new ConstrainedGridPane();
    org.controlsfx.glyphfont.FontAwesome.Glyph[] sglyphs = FontAwesome.Glyph
        .values();
    List<Glyph> icons = new ArrayList<Glyph>();
    for (FontAwesome.Glyph glyph : sglyphs) {
      Glyph icon = fontAwesome.create(glyph);
      icons.add(icon);
      icon.setTooltip(new Tooltip(glyph.name()));
    }
    char[] codes = { '\uf240', '\uf241', '\uf242', '\uf243', '\uf244' };
    String[] names = { "battery-full", "battery75", "battery50", "battery25",
        "battery0" };
    int i = 0;
    for (char code : codes) {
      Glyph icon = fontAwesome.create(code);
      icons.add(icon);
      icon.setTooltip(new Tooltip(names[i++]));
    }
    i = 0;
    for (Glyph icon : icons) {
      int row = i / 30;
      int col = i % 30;
      i++;
      Button button = new Button();
      icon.setFontSize(24);
      button.setGraphic(icon);
      button.setTooltip(icon.getTooltip());
      gridPane.add(button, col, row);
    }
    SampleApp.createAndShow("icons", gridPane, SHOW_TIME);
  }

  @Test
  public void testVerticalTabs() throws Exception {
    // https://stackoverflow.com/questions/16708578/javafx-2-0-tabpane-tabs-at-left-and-keep-tab-header-horizontal
    XYTabPane xyTabPane = new XYTabPane(64);

    String[] rowNames = { "DASHBOARD", "PLUG", "battery50", "battery25",
        FontAwesome.Glyph.GEAR.name() };
    String[] colNames = { "ROAD", "LOCK", "CAMERA", "MAP_MARKER", "QUESTION",
        "SIGNAL", "AREA_CHART", "BAR_CHART", "LINE_CHART", "KEY",
        "FILE_TEXT_ALT", "CLOCK_ALT", "POWER_OFF", "INFO", "ADJUST", "BOLT",
        "PLAY", "CHECK", "DASHBOARD", "EDIT", "FILE_PHOTO_ALT", "FILE_TEXT_ALT",
        "FLAG_CHECKERED", "MALE", "SMILE_ALT", "MOON_ALT", "SEARCH_MINUS",
        "SEARCH_PLUS", "SLIDERS", "CHAIN", "EDIT", "CAR", "USER", "WIFI",
        "WRENCH", "battery0" };
    int col = 0;
    for (String rowName : rowNames) {
      TabPane hTabPane = xyTabPane.addTabPane("h" + rowName,rowName, rowName);
      for (int i = 0; i < 7; i++) {
        xyTabPane.addTab(hTabPane, null, colNames[col++], new Pane());
      }
    }
    xyTabPane.tabMap.get("battery50").getGraphic().setRotate(90);
    SampleApp.createAndShow("vertical tabs", xyTabPane, SHOW_TIME);
  }
}
