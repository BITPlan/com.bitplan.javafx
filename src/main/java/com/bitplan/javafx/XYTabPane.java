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
import java.util.List;
import java.util.Map;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * this is a pane that has a tabular setup for TabPanes - a vertical one to
 * select the corresponding horizontal tabpane this way each tab can be
 * addressed as an x/y coordinate in table (but be aware this doesn't have to be
 * a full matrix some rows might be shorter than others although a nice layout
 * calls for same lenght rows e.g. with help tabs at the same position for each
 * row.
 * 
 * @author wf
 *
 */
public class XYTabPane extends Pane {
  TabPane vTabPane; // vertical TabPane
  List<TabPane> hTabpanes; // horizontal TabPanes - for each vertical tab there
                           // is one horizontal tab
  private int iconSize;

  Map<String, Glyph> iconMap = null;
  private GlyphFont fontAwesome;
  Map<String,Tab> tabMap=new HashMap<String,Tab>();

  /**
   * get an initial Map of Icons
   * 
   * @return
   */
  public Map<String, Glyph> getIconMap() {
    if (iconMap == null) {
      iconMap = new HashMap<String, Glyph>();
      char[] codes = { '\uf240', '\uf241', '\uf242', '\uf243', '\uf244' };
      String[] names = { "battery-full", "battery75", "battery50", "battery25",
          "battery0" };
      int i = 0;
      for (char code : codes) {
        Glyph icon = fontAwesome.create(code);
        iconMap.put(names[i], icon);
        icon.setTooltip(new Tooltip(names[i++]));
      }
    }
    return iconMap;
  }

  /**
   * create an XYTab Pane with the given iconSize
   * 
   * @param iconSize
   *          - e.g. 48
   */
  public XYTabPane(int iconSize) {
    super();
    this.iconSize = iconSize;
    vTabPane = this.addTabPane("vTabPane");
    vTabPane.setSide(Side.LEFT);
    Tab filler=new Tab();
    Rectangle r = new Rectangle();
    r.setWidth(iconSize);
    r.setHeight(iconSize);
    r.setFill(Color.TRANSPARENT);
    r.setStroke(Color.TRANSPARENT);
    filler.setGraphic(r);
    filler.setDisable(true);
    vTabPane.getTabs().add(filler);
    fontAwesome = GlyphFontRegistry.font("FontAwesome");
    super.getChildren().add(vTabPane);
  }

  /**
   * get the icon for the given glyph
   * 
   * @param glyph
   * @param fontSize
   *          - the fontSize of the glyph
   * @return the Glyph
   */
  public Glyph getIcon(String name, int fontSize) {
    Glyph icon = this.getIconMap().get(name + "x" + fontSize);
    //if (icon == null) {
      icon = this.getIconMap().get(name);
      if (icon == null) {
        org.controlsfx.glyphfont.FontAwesome.Glyph fglyph = FontAwesome.Glyph
            .valueOf(name);
        icon = fontAwesome.create(fglyph);
      }
      icon.setFontSize(fontSize);
      iconMap.put(name + "x" + fontSize, icon);
    //}     
    return icon;
  }

  /**
   * set the tab Glyph for the given Tabinfo
   * 
   * @param tab
   * @param glyphInfo
   */
  public void setTabGlyph(Tab tab, String glyphInfo) {
    Glyph icon = getIcon(glyphInfo, iconSize);
    setTabGlyph(tab, icon);
  }

  /**
   * set the glyph for the tab
   * 
   * @param tab
   * @param glyph
   */
  public void setTabGlyph(Tab tab, Glyph glyph) {
    tab.setGraphic(glyph);
  }

  /**
   * add a tabPane with the given group Id
   * 
   * @param groupId
   * @return - the tabPane
   */
  public TabPane addTabPane(String groupId) {
    TabPane tabPane = new TabPane();
    tabPane.setTabMinHeight(iconSize + 4);
    tabPane.setTabMaxHeight(iconSize + 4);
    // make sure it grows e.g. when Icons are set
    // https://stackoverflow.com/a/25164425/1497139
    VBox.setVgrow(tabPane, Priority.ALWAYS);
    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    return tabPane;
  }

  /**
   * add a tab
   * 
   * @param the
   *          TabPane to add a Tab to
   * @param index
   * @param title
   * @param content
   * @return
   */
  public Tab addTab(TabPane tabPane, int index, String title, String glyphName,
      Node content) {
    Tab tab = new Tab();
    if (title != null)
      tab.setText(title);
    tab.setContent(content);
    if (index > 0)
      tabPane.getTabs().add(index, tab);
    else
      tabPane.getTabs().add(tab);
    if (glyphName != null)
      this.setTabGlyph(tab, glyphName);
    tabMap.put(glyphName, tab);
    return tab;
  }

  /**
   * add a tab to the given tabPane
   * 
   * @param tabPane
   * @param title
   * @param glyphName
   * @param content
   * @return
   */
  public Tab addTab(TabPane tabPane, String title, String glyphName,
      Node content) {
    return addTab(tabPane, -1, title, glyphName, content);
  }
}
