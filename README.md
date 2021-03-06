### com.bitplan.javafx
[javafx controls and utilities - with binding to platform independent gui declaration](http://www.bitplan.com/Com.bitplan.javafx)

[![Travis (.org)](https://img.shields.io/travis/BITPlan/com.bitplan.javafx.svg)](https://travis-ci.org/BITPlan/com.bitplan.javafx)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitplan.gui/com.bitplan.javafx.svg)](https://search.maven.org/artifact/com.bitplan.gui/com.bitplan.javafx/0.0.33/jar)
[![codecov](https://codecov.io/gh/BITPlan/com.bitplan.javafx/branch/master/graph/badge.svg)](https://codecov.io/gh/BITPlan/com.bitplan.javafx)
[![GitHub issues](https://img.shields.io/github/issues/BITPlan/com.bitplan.javafx.svg)](https://github.com/BITPlan/com.bitplan.javafx/issues)
[![GitHub issues](https://img.shields.io/github/issues-closed/BITPlan/com.bitplan.javafx.svg)](https://github.com/BITPlan/com.bitplan.javafx/issues/?q=is%3Aissue+is%3Aclosed)
[![GitHub](https://img.shields.io/github/license/BITPlan/com.bitplan.javafx.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![BITPlan](http://wiki.bitplan.com/images/wiki/thumb/3/38/BITPlanLogoFontLessTransparent.png/198px-BITPlanLogoFontLessTransparent.png)](http://www.bitplan.com)

### Documentation
* [Wiki](http://www.bitplan.com/Com.bitplan.javafx)
* [com.bitplan.javafx Project pages](https://BITPlan.github.io/com.bitplan.javafx)
* [Javadoc](https://BITPlan.github.io/com.bitplan.javafx/apidocs/index.html)
* [Test-Report](https://BITPlan.github.io/com.bitplan.javafx/surefire-report.html)
### Maven dependency

Maven dependency
```xml
<!-- javafx controls and utilities - with binding to platform independent gui declaration http://www.bitplan.com/Com.bitplan.javafx -->
<dependency>
  <groupId>com.bitplan.gui</groupId>
  <artifactId>com.bitplan.javafx</artifactId>
  <version>0.0.33</version>
</dependency>
```

[Current release at repo1.maven.org](http://repo1.maven.org/maven2/com/bitplan/gui/com.bitplan.javafx/0.0.33/)

### How to build
```
git clone https://github.com/BITPlan/com.bitplan.javafx
cd com.bitplan.javafx
mvn install
```
### Version History
| Date       | Version | changes
| -----------| ------: | ----------
| 2017-08-20 |  0.0.1  | initial release
| 2017-08-21 |  0.0.2  | adds XYTabPane
| 2017-08-22 |  0.0.3  | improves icon handling
| 2017-08-22 |  0.0.4  | adds GenericApp
| 2017-08-23 |  0.0.5  | add JFXML
| 2017-08-24 |  0.0.6  | fixes selectRandomTab
| 2018-08-05 |  0.0.7  | upgrades to com.bitplan.gui 0.0.4 and refactors
| 2018-08-05 |  0.0.8  | fixes incomplete release to maven central
| 2018-08-06 |  0.0.9  | improves I18n handling and fixes GenericApp
| 2018-08-06 | 0.0.10  | refactors to use getI18nId()
| 2018-08-07 | 0.0.11  | refactors to use getInstance - improves I18n
| 2018-08-07 | 0.0.12  | includes medusa gauges and some extensions of it
| 2018-08-07 | 0.0.13  | refactors to include JFXStopWatch
| 2018-08-10 | 0.0.14  | fixes Double handling - trim text
| 2018-08-10 | 0.0.15  | upgrades com.bitplan.gui to 0.0.10 to fix getInstance
| 2018-08-12 | 0.0.16  | upgrades com.bitplan.gui to 0.0.11 to improve JsonAble
| 2018-08-15 | 0.0.17  | refactors WaitableApp to allow showTime directly
| 2018-08-22 | 0.0.18  | upgrades com.bitplan.gui to 0.0.12 to improve JsonAble
| 2018-08-25 | 0.0.19  | saveAsPng for nodes - fixes #1
| 2018-08-26 | 0.0.20  | fixes #2 - makes args available in Main
| 2018-09-06 | 0.0.22  | fixes #4 - fixes translation issue
| 2018-12-05 | 0.0.23  | fixes #5 - adds Link
| 2019-01-09 | 0.0.24  | upgrades com.bitplan.gui to 0.0.13
| 2019-01-09 | 0.0.25  | re-releases due to pom config errors
| 2019-01-09 | 0.0.26  | upgrades com.bitplan.gui to 0.0.15
| 2019-01-24 | 0.0.27  | adds tab selection handling
| 2019-01-30 | 0.0.28  | fixes #6 - Nullpointer Exception in selectTab
| 2019-02-20 | 0.0.29  | adds ImageViewPane
| 2019-03-06 | 0.0.30  | adds SwingFXUtils (e.g. for raspberry)
| 2019-04-01 | 0.0.31  | fixes #7 Allow selection of subrectangles for an ImageViewPane
| 2019-04-23 | 0.0.32  | fixes #8 and #9 GenericApp close issues
| 2019-05-16 | 0.0.33  | adds simpler savePng method 
| 2020-01-23 |         | check whether travis is broken 

