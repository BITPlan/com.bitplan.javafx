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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bitplan.i18n.Translator;
import com.bitplan.states.StopWatch;

import eu.hansolo.OverviewDemo;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * test the GUI
 * 
 * @author wf
 *
 */
@SuppressWarnings("restriction")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGUI {
  static int SHOW_TIME = 4000;

  @Before
  public void initGUI() {
    WaitableApp.toolkitInit();
    Translator.initialize("i18n", "en");
  }
  
  @Test
  public void testNumberMatchers() {
    String texts[]= {"1","a","1.2","2,5","1.,2"};
    boolean iexpected[]= {true,false,false,false,false};
    boolean dexpected[]= {true,false,true,true,false};
    Pattern pi=Pattern.compile(GenericControl.INTEGER_MATCH);
    Pattern pd=Pattern.compile(GenericControl.DOUBLE_MATCH);
      
    int i=0;
    for (String text:texts) {
      Matcher matcheri=pi.matcher(text);
      Matcher matcherd=pd.matcher(text);
      assertEquals("integer "+text,iexpected[i],matcheri.matches());
      assertEquals("double "+text,dexpected[i],matcherd.matches());
      i++;
    }
  }
  
  @Test
  public void testStopWatch() {
    StopWatch stopWatch = new JFXStopWatch("StopTime");
    stopWatch.halt();
    stopWatch.reset();
    // System.out.println(stopWatch.asIsoDateStr());
    // assertEquals(0l,stopWatch.getTime());
    long times[] = { 90000 * 1000, 7200000, 0, 2000, 500, 1000, 2000 };
    for (long time : times) {
      stopWatch.setTime(time);
      // System.out.println(stopWatch.asIsoDateStr());
      assertEquals(time, stopWatch.getTime());
    }
  }
  
  @Test
  public void testMedusa() throws Exception {
    OverviewDemo demo = new OverviewDemo();
    demo.init();
    GridPane demoPane = demo.getDemoPane();
    SampleApp.createAndShow("Controls", demoPane, SHOW_TIME*10);
  }

  @Test
  public void testIcons() throws Exception {
    GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    ConstrainedGridPane gridPane = new ConstrainedGridPane();
    org.controlsfx.glyphfont.FontAwesome.Glyph[] sglyphs = FontAwesome.Glyph
        .values();
    List<Node> icons = new ArrayList<Node>();
    for (FontAwesome.Glyph glyph : sglyphs) {
      Glyph icon = fontAwesome.create(glyph);
      icons.add(icon);
      icon.setId(glyph.name());
      icon.setFontSize(24);
      icon.setTooltip(new Tooltip(glyph.name()));
    }
    XYTabPane xyTabPane = new XYTabPane(24);
    for (Entry<String, Node> iconentry : xyTabPane.getIconMap().entrySet()) {
      icons.add(iconentry.getValue());
      iconentry.getValue().setId(iconentry.getKey());
    }
    int i = 0;
    for (Node icon : icons) {
      int row = i / 30;
      int col = i % 30;
      i++;
      Button button = new Button();
      button.setGraphic(icon);
      button.setTooltip(new Tooltip(icon.getId()));
      gridPane.add(button, col, row);
    }
    SampleApp.createAndShow("icons", gridPane, SHOW_TIME);
  }

  @Test
  public void testVerticalTabs() throws Exception {
    // https://stackoverflow.com/questions/16708578/javafx-2-0-tabpane-tabs-at-left-and-keep-tab-header-horizontal
    XYTabPane xyTabPane = new XYTabPane(64);

    String[] rowNames = { "DASHBOARD", "PLUG", "exchange", "EYE", "GEAR" };
    String[] colNames = { "ROAD", "LOCK", "CAMERA", "MAP_MARKER", "QUESTION",
        "SIGNAL", "AREA_CHART", "BAR_CHART", "LINE_CHART", "KEY",
        "FILE_TEXT_ALT", "CLOCK_ALT", "POWER_OFF", "INFO", "ADJUST", "BOLT",
        "PLAY", "CHECK", "DASHBOARD", "EDIT", "FILE_PHOTO_ALT", "FILE_TEXT_ALT",
        "FLAG_CHECKERED", "MALE", "SMILE_ALT", "MOON_ALT", "SEARCH_MINUS",
        "SEARCH_PLUS", "SLIDERS", "CHAIN", "EDIT", "CAR", "USER", "WIFI",
        "WRENCH", "battery0" };
    int col = 0;
    for (String rowName : rowNames) {
      TabPane hTabPane = xyTabPane.addTabPane(rowName, rowName, rowName);
      for (int i = 0; i < 7; i++) {
        xyTabPane.addTab(hTabPane, colNames[col], null, colNames[col++],
            new Pane());
      }
    }
    // xyTabPane.tabMap.get("battery25").getGraphic().setRotate(90);
    SampleApp sampleApp = new SampleApp("vertical tabs", xyTabPane);
    sampleApp.show();
    sampleApp.waitOpen();
    Set<String> tabids = xyTabPane.getTabMap().keySet();
    for (String tabId : tabids) {
      xyTabPane.selectTab(tabId);
      Thread.sleep(SHOW_TIME / tabids.size());
    }
    sampleApp.close();
  }
  
  @Test
  public void testXAlert() throws Exception {
    Pane pane=new Pane();
    SampleApp sampleApp = new SampleApp("alert", pane);
    sampleApp.show();
    sampleApp.waitOpen();
    GenericDialog.showAlert(sampleApp.stage,"Alert", "test", "test");
    Thread.sleep(SHOW_TIME);
    sampleApp.close();
  }
  
  @Test
  public void testYException() throws Exception {
    Pane pane=new Pane();
    SampleApp sampleApp = new SampleApp("alert", pane);
    sampleApp.show();
    sampleApp.waitOpen();
    ExceptionController.debug=false;
    GenericDialog.showException(sampleApp.stage,"exception", "an exception", new Exception("Test"), null);
    Thread.sleep(SHOW_TIME);
    sampleApp.close();
  }
}
