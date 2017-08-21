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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
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
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");

  private TabPane vTabPane; // vertical TabPane
  List<TabPane> hTabpanes; // horizontal TabPanes - for each vertical tab there
                           // is one horizontal tab
  private int iconSize;

  Map<String, Node> iconMap = null;
  private GlyphFont fontAwesome;
  Map<String, Tab> tabMap = new HashMap<String, Tab>();
  Map<String, Tab> vTabMap = new HashMap<String, Tab>();
  Map<String, TabPane> tabPaneMap = new HashMap<String, TabPane>();

  public TabPane getvTabPane() {
    return vTabPane;
  }

  public void setvTabPane(TabPane vTabPane) {
    this.vTabPane = vTabPane;
  }

  /**
   * get an initial Map of Icons
   * 
   * @return
   */
  public Map<String, Node> getIconMap() {
    if (iconMap == null) {
      iconMap = new HashMap<String, Node>();
      char[] codes = { '\uf240', '\uf241', '\uf242', '\uf243', '\uf244',
          '\uf0ec' };
      String[] names = { "battery-full", "battery75", "battery50", "battery25",
          "battery0", "exchange" };
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
    setvTabPane(this.addTabPane("vTabPane"));
    getvTabPane().setSide(Side.LEFT);
    Tab filler = new Tab();
    Rectangle r = new Rectangle();
    r.setWidth(iconSize);
    r.setHeight(iconSize);
    r.setFill(Color.TRANSPARENT);
    r.setStroke(Color.TRANSPARENT);
    filler.setGraphic(r);
    filler.setDisable(true);
    getvTabPane().getTabs().add(filler);
    fontAwesome = GlyphFontRegistry.font("FontAwesome");
    super.getChildren().add(getvTabPane());
  }

  /**
   * get the icon for the given glyph
   * 
   * @param glyph
   * @param fontSize
   *          - the fontSize of the glyph
   * @return the Glyph
   */
  public Node getIcon(String name, int fontSize) {
    Node icon = this.getIconMap().get(name + "x" + fontSize);
    // if (icon == null) {
    icon = this.getIconMap().get(name);
    if (icon == null) {
      try {
        org.controlsfx.glyphfont.FontAwesome.Glyph fglyph = FontAwesome.Glyph
            .valueOf(name);
        icon = fontAwesome.create(fglyph);
      } catch (Throwable th) {
        LOGGER.log(Level.WARNING,
            "could not get FontAwesomeGlyph for icon " + name);
      }
    }
    if (icon==null) {
      URL iconUrl = this.getClass()
          .getResource("/icons/" + name+ ".png");
      if (iconUrl != null) {
        ImageView iconImage = new ImageView(iconUrl.toString());
        iconImage.setFitHeight(fontSize);
        iconImage.setFitWidth(fontSize);
        icon=iconImage;
      }
    }
    if (icon != null) {
      if (icon instanceof Glyph)
        ((Glyph) icon).setFontSize(fontSize);
      iconMap.put(name + "x" + fontSize, icon);
    }
    return icon;
  }

  /**
   * set the tab Glyph for the given Tabinfo
   * 
   * @param tab
   * @param glyphInfo
   */
  public void setTabGlyph(Tab tab, String glyphInfo) {
    Node icon = getIcon(glyphInfo, iconSize);
    setTabIcon(tab, icon);
  }

  /**
   * set the icon for the tab
   * 
   * @param tab
   * @param icon
   */
  public void setTabIcon(Tab tab, Node icon) {
    tab.setGraphic(icon);
  }

  /**
   * add a tabPane with the given group Id and iconName
   * 
   * @param title
   * @param groupId
   * @param iconName
   *          - the name of the icon
   * @return - the tabPane
   */
  public TabPane addTabPane(String groupId, String title, String iconName) {
    TabPane tabPane = addTabPane(groupId);
    Tab tab = addTab(vTabPane, title, iconName, tabPane);
    vTabMap.put(groupId, tab);
    return tabPane;
  }

  /**
   * add a tab Pane with the given groupId
   * 
   * @param groupId
   * @return the tabPane
   */
  public TabPane addTabPane(String groupId) {
    TabPane tabPane = new TabPane();
    int margin = iconSize / 4;
    tabPane.setTabMinHeight(iconSize + margin);
    tabPane.setTabMaxHeight(iconSize + margin);
    tabPane.setTabMinWidth(iconSize + margin);
    tabPane.setTabMaxWidth(iconSize + margin);
    // make sure it grows e.g. when Icons are set
    // https://stackoverflow.com/a/25164425/1497139
    VBox.setVgrow(tabPane, Priority.ALWAYS);
    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    this.tabPaneMap.put(groupId, tabPane);
    return tabPane;
  }

  /**
   * get the tabPane with the given groupId
   * 
   * @param groupId
   * @return the tabPane
   */
  public TabPane getTabPane(String groupId) {
    TabPane tabPane = this.tabPaneMap.get(groupId);
    return tabPane;
  }

  /**
   * select the Vertical Tab with the given groupId
   * 
   * @param groupId
   */
  public void selectVTab(String groupId) {
    Tab tab = vTabMap.get(groupId);
    if (tab == null)
      throw new IllegalArgumentException(
          "unknown groupId for vertical tab " + groupId);
    vTabPane.getSelectionModel().select(tab);
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
    if (glyphName != null) {
      this.setTabGlyph(tab, glyphName);
      tabMap.put(glyphName, tab);
    }
    if (title != null) {
      tab.setTooltip(new Tooltip(title));
      if (tab.getGraphic() == null) {
        tab.setText(title);
      }
    }
    tab.setContent(content);
    if (index > 0)
      tabPane.getTabs().add(index, tab);
    else
      tabPane.getTabs().add(tab);

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
