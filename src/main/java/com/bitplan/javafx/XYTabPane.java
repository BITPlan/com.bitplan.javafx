/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except
 *    - the ones in the stackoverflow package
 *    - SwingFXUtils.java from Oracle which is provided e.g. for the raspberry env
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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
  protected static boolean debug = false;
  TabPane vTabPane; // vertical TabPane
  List<TabPane> hTabpanes; // horizontal TabPanes - for each vertical tab there
                           // is one horizontal tab
  int iconSize;

  static Map<String, Node> iconMap = null;
  static GlyphFont fontAwesome;
  // Map of all Tabs by tabId
  Map<String, Tab> tabMap = new HashMap<String, Tab>();
  // reverse map from tab to tab id
  Map<Tab, String> tabIdMap = new HashMap<Tab, String>();
  Map<String, Tab> vTabMap = new HashMap<String, Tab>();
  Map<Tab, TabPane> vTabPaneMapByTab = new HashMap<Tab, TabPane>();
  Map<TabPane, Tab> vTapMapByTabPane = new HashMap<TabPane, Tab>();
  Map<String, TabPane> tabPaneMap = new HashMap<String, TabPane>();
  Map<String, TabPane> tabPaneByTabIdMap = new HashMap<String, TabPane>();
  Button topLeftButton;
  protected TabSelection currentTab;

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    XYTabPane.debug = debug;
  }

  public TabSelection getCurrentTab() {
    return currentTab;
  }

  public void setCurrentTab(TabSelection currentTab) {
    this.currentTab = currentTab;
  }

  /**
   * get the vertical tab Pane
   * 
   * @return - the vertical Tab Pane
   */
  public TabPane getvTabPane() {
    return vTabPane;
  }

  public void setvTabPane(TabPane vTabPane) {
    this.vTabPane = vTabPane;
  }

  /**
   * get the map of Tabs
   * 
   * @return - the tab map
   */
  public Map<String, Tab> getTabMap() {
    return tabMap;
  }

  public Button getTopLeftButton() {
    return topLeftButton;
  }

  public void setTopLeftButton(Button topLeftButton) {
    this.topLeftButton = topLeftButton;
  }

  /**
   * get the tab Size
   * 
   * @return - the size of the tabs
   */
  public int getTabSize() {
    return iconSize * 5 / 4;
  }

  /**
   * get an initial Map of Icons
   * 
   * @return the icon map
   */
  public static Map<String, Node> getIconMap() {
    if (iconMap == null) {
      if (fontAwesome==null)
        fontAwesome = GlyphFontRegistry.font("FontAwesome");
      iconMap = new HashMap<String, Node>();
      char[] codes = { '\uf240', '\uf241', '\uf242', '\uf243', '\uf244',
          '\uf0ec', '\uf2c9' };
      String[] names = { "battery-full", "battery75", "battery50", "battery25",
          "battery0", "exchange", "temp50" };
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
    this.currentTab = TabSelection.getInstance();

    setvTabPane(this.addTabPane("vTabPane"));
    getvTabPane().setSide(Side.LEFT);
    Tab filler = new Tab();
    topLeftButton = new Button();
    int tabSize = getTabSize();
    topLeftButton.setMinSize(tabSize, tabSize);
    topLeftButton.setMaxSize(tabSize, tabSize);
    topLeftButton.setDisable(true);
    filler.setGraphic(topLeftButton);
    filler.setDisable(true);
    getvTabPane().getTabs().add(filler);
    this.addToMaps(filler, vTabPane);
    super.getChildren().add(getvTabPane());
  }

  /**
   * make clicked tabs blue
   * 
   * @param tabPane
   */
  protected void makeBlueOnSelectAndRemember(TabPane tabPane) {
    tabPane.getSelectionModel().selectedItemProperty()
        .addListener((ov, oldTab, newTab) -> {
          if (oldTab != null) {
            setColor(oldTab, Color.BLUE, Color.BLACK);
          }
          setColor(newTab, Color.BLACK, Color.BLUE);
          String tabId = tabIdMap.get(newTab);

          if (tabPaneMap.containsKey(tabId)) {
            currentTab.setTabPaneId(tabId);

            if (debug)
              LOGGER.log(Level.INFO,
                  "tabPane " + currentTab.getTabPaneId() + " selected");
            TabPane selectedTabPane = tabPaneMap.get(tabId);
            Tab selectedTab = selectedTabPane.getSelectionModel()
                .getSelectedItem();
            if (selectedTab != null)
              currentTab.setTabId(selectedTab.getId());
          } else {
            currentTab.setTabId(tabId);
            if (debug)
              LOGGER.log(Level.INFO,
                  "tab " + currentTab.getTabId() + " selected");
          }
        });
  }

  /**
   * set the color of the graphic of the given tab
   *
   * @param tab
   *          - the tab to change
   * @param oldColor
   *          - the old color
   * @param newColor
   *          - the new color
   */
  protected void setColor(Tab tab, Color oldColor, Color newColor) {
    if (debug)
      LOGGER.log(Level.INFO, "changing  tab color for " + tab.getId() + " from "
          + oldColor + " to " + newColor);
    Node graphic = tab.getGraphic();

    if (graphic instanceof Glyph) {
      Glyph glyph = (Glyph) graphic;
      glyph.setColor(newColor);
    } else if (graphic instanceof ImageView) {
      // https://docs.oracle.com/javafx/2/image_ops/jfxpub-image_ops.htm
      ImageView imageView = (ImageView) graphic;
      imageView.setImage(reColor(imageView.getImage(), oldColor, newColor));
    }
  }

  /**
   * reColor the given InputImage from oldColor to the given newColor inspired
   * by https://stackoverflow.com/a/12945629/1497139
   * 
   * @param inputImage
   * @param oldColor
   * @param newColor
   * @return the reColored output Image
   */
  public static Image reColor(Image inputImage, Color oldColor,
      Color newColor) {
    int W = (int) inputImage.getWidth();
    int H = (int) inputImage.getHeight();
    WritableImage outputImage = new WritableImage(W, H);
    PixelReader reader = inputImage.getPixelReader();
    PixelWriter writer = outputImage.getPixelWriter();
    int ob = (int) oldColor.getBlue() * 255;
    int or = (int) oldColor.getRed() * 255;
    int og = (int) oldColor.getGreen() * 255;
    int nb = (int) newColor.getBlue() * 255;
    int nr = (int) newColor.getRed() * 255;
    int ng = (int) newColor.getGreen() * 255;
    for (int y = 0; y < H; y++) {
      for (int x = 0; x < W; x++) {
        int argb = reader.getArgb(x, y);
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        if (g == og && r == or && b == ob) {
          r = nr;
          g = ng;
          b = nb;
        }

        argb = (a << 24) | (r << 16) | (g << 8) | b;
        writer.setArgb(x, y, argb);
      }
    }
    return outputImage;
  }

  /**
   * get the icon for the given name and fontSize
   * 
   * @param name
   *          - the name of the glyph
   * @param fontSize
   *          - the fontSize of the glyph
   * @return the Glyph
   */
  public static Node getIcon(String name, int fontSize) {
    Node icon = getIconMap().get(name + "x" + fontSize);
    // if (icon == null) {
    icon = getIconMap().get(name);
    if (icon == null) {
      try {
        org.controlsfx.glyphfont.FontAwesome.Glyph fglyph = FontAwesome.Glyph
            .valueOf(name.toUpperCase());
        icon = fontAwesome.create(fglyph);
      } catch (Throwable th) {
        if (debug)
          LOGGER.log(Level.INFO,
              "Could not get FontAwesomeGlyph for icon " + name);
      }
    }
    if (icon == null) {
      URL iconUrl = XYTabPane.class.getClass().getResource("/icons/" + name + ".png");
      if (iconUrl != null) {
        ImageView iconImage = new ImageView(iconUrl.toString());
        iconImage.setFitHeight(fontSize);
        iconImage.setFitWidth(fontSize);
        iconImage.setId(name);
        icon = iconImage;
      } else {
        if (debug)
          LOGGER.log(Level.WARNING, "could not get resource for icon " + name);
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
    if (icon != null)
      tab.setGraphic(icon);
  }

  /**
   * add a tabPane with the given tabPane Id and iconName
   * 
   * @param title
   * @param tabPaneId
   *          / tabPaneId
   * @param iconName
   *          - the name of the icon
   * @return - the tabPane
   */
  public TabPane addTabPane(String tabPaneId, String title, String iconName) {
    TabPane tabPane = addTabPane(tabPaneId);
    Tab tab = addTab(vTabPane, tabPaneId, title, iconName, tabPane);
    vTabMap.put(tabPaneId, tab);
    addToMaps(tab, tabPane);
    return tabPane;
  }

  /**
   * add the given tab/tabPane combination to the maps
   * 
   * @param tab
   * @param tabPane
   */
  private void addToMaps(Tab tab, TabPane tabPane) {
    this.vTabPaneMapByTab.put(tab, tabPane);
    this.vTapMapByTabPane.put(tabPane, tab);
  }

  /**
   * add a tab Pane with the given tabPaneId
   * 
   * @param tabPaneId
   * @return the tabPane
   */
  public TabPane addTabPane(String tabPaneId) {
    TabPane tabPane = new TabPane();
    tabPane.setId(tabPaneId);
    int tabSize = getTabSize();
    tabPane.setTabMinHeight(tabSize);
    tabPane.setTabMaxHeight(tabSize);
    tabPane.setTabMinWidth(tabSize);
    tabPane.setTabMaxWidth(tabSize);
    // tabPane.setMaxWidth(Control.USE_PREF_SIZE);
    // tabPane.setMaxHeight(Control.USE_PREF_SIZE);
    // make sure it grows e.g. when Icons are set
    // https://stackoverflow.com/a/25164425/1497139
    VBox.setVgrow(tabPane, Priority.ALWAYS);
    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    // add click behavior
    makeBlueOnSelectAndRemember(tabPane);
    // add tab Pane to map
    this.tabPaneMap.put(tabPaneId, tabPane);
    return tabPane;
  }

  /**
   * get the tabPane with the given tabId
   * 
   * @param tabId
   * @return the tabPane
   */
  public TabPane getTabPane(String tabId) {
    TabPane tabPane = this.tabPaneMap.get(tabId);
    return tabPane;
  }

  /**
   * select the Vertical Tab with the given tabPaneId
   * 
   * @param tabPaneId
   */
  public void selectVTab(String tabPaneId) {
    Tab tab = vTabMap.get(tabPaneId);
    if (tab == null)
      throw new IllegalArgumentException(
          "unknown tabPaneId for vertical tab " + tabPaneId);
    vTabPane.getSelectionModel().select(tab);
  }

  /**
   * add a tab
   * 
   * @param tabPane
   *          the TabPane to add a Tab to
   * @param index
   * @param title
   * @param glyphName
   * @param content
   * @return the tab
   */
  public Tab addTab(TabPane tabPane, String tabId, int index, String title,
      String glyphName, Node content) {
    Tab tab = new Tab();
    tab.setId(tabId);
    if (glyphName != null) {
      this.setTabGlyph(tab, glyphName);
      getTabMap().put(tabId, tab);
      // allow reverse lookup
      tabIdMap.put(tab, tabId);
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
    this.tabPaneByTabIdMap.put(tabId, tabPane);
    return tab;
  }

  /**
   * add a tab to the given tabPane
   * 
   * @param tabPane
   * @param title
   * @param glyphName
   * @param content
   * @return the tab
   */
  public Tab addTab(TabPane tabPane, String tabid, String title,
      String glyphName, Node content) {
    return addTab(tabPane, tabid, -1, title, glyphName, content);
  }

  /**
   * get the tab with the given tab Id
   * 
   * @param tabId
   * @return - the tabId
   */
  public Tab getTab(String tabId) {
    return this.getTabMap().get(tabId);
  }

  /**
   * get the select horizontal TabPane
   * 
   * @return - the tabPane
   */
  public TabPane getSelectedTabPane() {
    Tab vTab = vTabPane.getSelectionModel().getSelectedItem();
    TabPane tabPane = this.vTabPaneMapByTab.get(vTab);
    return tabPane;
  }

  /**
   * select the tab with the given tab id
   * 
   * @param tabId
   * @return - the tab;
   */
  public Tab selectTab(String tabId) {
    Tab tab = getTab(tabId);
    TabPane tabPane = this.tabPaneByTabIdMap.get(tabId);
    if (tabPane == null) {
      LOGGER.log(Level.WARNING,"no tabPane for tabId "+tabId+"active");
    } else {
      Tab vtab = this.vTapMapByTabPane.get(tabPane);
      // first select the Vertical tab
      if (debug)
        LOGGER.log(Level.INFO, "selecting tabpane " + tabPane.getId());
      vTabPane.getSelectionModel().select(vtab);
      // then the horizontal one
      if (debug)
        LOGGER.log(Level.INFO, "selecting tab " + tab.getId());
      tabPane.getSelectionModel().select(tab);
    }
    return tab;
  }

  /**
   * select a random tab
   * 
   * @param tabPane
   */
  public Tab selectRandomTab(TabPane tabPane) {
    Random random = new Random();
    SingleSelectionModel<Tab> vsel = tabPane.getSelectionModel();
    int tabIndex = random.nextInt(vTabPane.getTabs().size());
    vsel.select(tabIndex);
    return vsel.getSelectedItem();
  }

  /**
   * get the selected tab of the given tab Pane
   * 
   * @param tabPane
   * @return the selected tab
   */
  public Tab getSelectedTab(TabPane tabPane) {
    SingleSelectionModel<Tab> vsel = tabPane.getSelectionModel();
    return vsel.getSelectedItem();
  }

  /**
   * get the selected Tab
   * 
   * @return - the selected Tab
   */
  public Tab getSelectedTab() {
    Tab vTab = getSelectedTab(this.vTabPane);
    TabPane hTabPane = this.vTabPaneMapByTab.get(vTab);
    return getSelectedTab(hTabPane);
  }

  /**
   * select a random Tab
   */
  public Tab selectRandomTab() {
    Tab vTab = selectRandomTab(this.vTabPane);
    TabPane hTabPane = this.vTabPaneMapByTab.get(vTab);
    Tab hTab = selectRandomTab(hTabPane);
    return hTab;
  }

  public int getIconSize() {
    return this.iconSize;
  }

}
