/**
 * The MIT License (MIT)
 * Copyright (c) 2026 Mark Bernard
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, copy, 
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.markbernard.jedit;

import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to save and load application state between executions.
 * 
 * @author Mark Bernard
 */
@Root
public class ApplicationPreferences {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationPreferences.class);
    /** Define in one spot to use everywhere */
    public static final String FILE_PATH_SEPARATOR = "/";

    private static final String USER_HOME = System.getProperty("user.home");
    private static final String CONFIG_FILE_NAME = "jedit_config.xml";
    
    private static ApplicationPreferences instance;
    
    private File configurationPath;

    @Element(required = false)
    private int windowX;
    @Element(required = false)
    private int windowY;
    @Element(required = false)
    private int windowWidth;
    @Element(required = false)
    private int windowHeight;
    @Element(required = false)
    private boolean windowMaximized;
    @Element(required = false)
    private String currentFileName;
    @Element(required = false)
    private String currentFilePath;
    @Element(required = false)
    private boolean wordWrap;
    @Element(required = false)
    private boolean statusBar;
    @Element(required = false)
    private String currentFontName;
    @Element(required = false)
    private int currentFontStyle;
    @Element(required = false)
    private int currentFontSize;
    private Font currentFont;
    @ElementList(name = "recentDocuments", entry = "document", required = false)
    private List<String> recentDocuments = new ArrayList<>();
    @Element(required = false)
    private int themeDialogX;
    @Element(required = false)
    private int themeDialogY;
    @Element(required = false)
    private int themeDialogWidth;
    @Element(required = false)
    private int themeDialogHeight;
    @Element(required = false)
    private int themeDialogMainDividerLocation;
    @Element(required = false)
    private int themeDialogStyleDividerLocation;
    @Element(required = false)
    private boolean showStatusBar;
    @Element(required = false)
    private boolean spacesInsteadOfTabs;
    @Element(required = false)
    private int tabSize;
    @Element(required = false)
    private boolean convertTabsOrSpacesOnSave;
    @Element(required = false)
    private boolean enableLineNumbers;
    @Element(required = false)
    private int configurationDialogX;
    @Element(required = false)
    private int configurationDialogY;
    
    /**
     * @return The one instance
     */
    public static ApplicationPreferences getInstance() {
        if (instance == null) {
            instance = new ApplicationPreferences();
            LoggerFactory.getLogger(ApplicationPreferences.class).info("JEdit started");
        }
        
        return instance;
    }
    
    private ApplicationPreferences() {
        String os = System.getProperty("os.name", "Linux");
        if (os.toLowerCase().contains("mac")) {
            configurationPath = new File(USER_HOME + "/Library/Preferences/jedit");
        } else if (os.toLowerCase().contains("windows")) {
            configurationPath = new File(System.getenv("localappdata") + "/jedit");
        } else {
            //Default to Linux
            String configPath = System.getenv("XDG_CONFIG_HOME");
            if (configPath == null) {
                configPath = USER_HOME + "/.config";
            }
            configurationPath = new File(configPath + "/jedit");
        }
        
        if (!configurationPath.exists()) {
            configurationPath.mkdirs();
        }
        
        resetToDefaults();
    }
    
    /**
     * Remove all configurations and set to defaults.
     */
    public void resetToDefaults() {
        windowX = 0;
        windowY = 0;
        windowWidth = 1024;
        windowHeight = 768;
        windowMaximized = false;
        currentFileName = "Untitled";
        currentFilePath = USER_HOME;
        wordWrap = false;
        statusBar = true;
        currentFontName = "Hack";
        currentFontStyle = Font.PLAIN;
        currentFontSize = 16;
        currentFont = new Font(currentFontName, currentFontStyle, currentFontSize);
        recentDocuments = new ArrayList<>();
        themeDialogX = 0;
        themeDialogY = 0;
        themeDialogWidth = 1024;
        themeDialogHeight = 768;
        themeDialogMainDividerLocation = 120;
        themeDialogStyleDividerLocation = 425;
        showStatusBar = true;
        spacesInsteadOfTabs = true;
        tabSize = 4;
        convertTabsOrSpacesOnSave = true;
        enableLineNumbers = true;
        configurationDialogX = 0;
        configurationDialogY = 0;
    }
    
    private void copy(ApplicationPreferences copyFrom) {
        windowX = copyFrom.windowX;
        windowY = copyFrom.windowY;
        windowWidth = copyFrom.windowWidth;
        windowHeight = copyFrom.windowHeight;
        windowMaximized = copyFrom.windowMaximized;
        currentFileName = copyFrom.currentFileName;
        currentFilePath = copyFrom.currentFilePath;
        wordWrap = copyFrom.wordWrap;
        statusBar = copyFrom.statusBar;
        currentFontName = copyFrom.currentFontName;
        currentFontStyle = copyFrom.currentFontStyle;
        currentFontSize = copyFrom.currentFontSize;
        currentFont = new Font(currentFontName, currentFontStyle, currentFontSize);
        recentDocuments = copyFrom.recentDocuments;
        themeDialogX = copyFrom.themeDialogX;
        themeDialogY = copyFrom.themeDialogY;
        themeDialogWidth = copyFrom.themeDialogWidth;
        themeDialogHeight = copyFrom.themeDialogHeight;
        themeDialogMainDividerLocation = copyFrom.themeDialogMainDividerLocation;
        themeDialogStyleDividerLocation = copyFrom.themeDialogStyleDividerLocation;
        showStatusBar = copyFrom.showStatusBar;
        spacesInsteadOfTabs = copyFrom.spacesInsteadOfTabs;
        tabSize = copyFrom.tabSize;
        convertTabsOrSpacesOnSave = copyFrom.convertTabsOrSpacesOnSave;
        enableLineNumbers = copyFrom.enableLineNumbers;
        configurationDialogX = copyFrom.configurationDialogX;
        configurationDialogY = copyFrom.configurationDialogY;
    }
    
    /**
     * Load state from the user preferences store and apply that state to 
     * the provided application window. Use sensible defaults as required.
     * 
     * @param frame The application window.
     */
    public void loadPrefs(JFrame frame) {
        if (configurationPath.exists()) {
            File configFile = new File(configurationPath.getAbsolutePath() + FILE_PATH_SEPARATOR + CONFIG_FILE_NAME);
            try {
                if (configFile.exists()) {
                    Serializer serializer = new Persister();
                    ApplicationPreferences temp = serializer.read(ApplicationPreferences.class, configFile);
                    copy(temp);
                }
                frame.setBounds(windowX, windowY, windowWidth, windowHeight);
                if (windowMaximized) {
                    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            } catch (Exception e) {
                LOGGER.error("Could not load configuration file: {}", configFile.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Read state from the provided application window and store it
     * to the user preferences store.
     * 
     * @param frame The main application window.
     */
    public void savePrefs(JFrame frame) {
        if (configurationPath.exists()) {
            File configFile = new File(configurationPath.getAbsolutePath() + FILE_PATH_SEPARATOR + CONFIG_FILE_NAME);
            try {
                windowMaximized = frame.getExtendedState() == Frame.MAXIMIZED_BOTH;
                if (!windowMaximized) {
                    windowX = frame.getX();
                    windowY = frame.getY();
                    windowWidth = frame.getWidth();
                    windowHeight = frame.getHeight();
                }
                Serializer serializer = new Persister();
                serializer.write(this, configFile);
            } catch (Exception e) {
                LOGGER.error("Could not save to config file: {}", configFile.getAbsolutePath(), e);
            }
        }
    }

    /**
     * @return the windowX
     */
    public int getWindowX() {
        return windowX;
    }

    /**
     * @param windowX the windowX to set
     */
    public void setWindowX(int windowX) {
        this.windowX = windowX;
    }

    /**
     * @return the windowY
     */
    public int getWindowY() {
        return windowY;
    }

    /**
     * @param windowY the windowY to set
     */
    public void setWindowY(int windowY) {
        this.windowY = windowY;
    }

    /**
     * @return the windowWidth
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * @param windowWidth the windowWidth to set
     */
    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    /**
     * @return the windowHeight
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * @param windowHeight the windowHeight to set
     */
    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    /**
     * @return the windowMaximized
     */
    public boolean isWindowMaximized() {
        return windowMaximized;
    }

    /**
     * @param windowMaximized the windowMaximized to set
     */
    public void setWindowMaximized(boolean windowMaximized) {
        this.windowMaximized = windowMaximized;
    }

    /**
     * @return the currentFileName
     */
    public String getCurrentFileName() {
        return currentFileName;
    }

    /**
     * @param currentFileName the currentFileName to set
     */
    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    /**
     * @return the currentFilePath
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * @param currentFilePath the currentFilePath to set
     */
    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    /**
     * @return the wordWrap
     */
    public boolean isWordWrap() {
        return wordWrap;
    }

    /**
     * @param wordWrap the wordWrap to set
     */
    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    /**
     * @return the statusBar
     */
    public boolean isStatusBar() {
        return statusBar;
    }

    /**
     * @param statusBar the statusBar to set
     */
    public void setStatusBar(boolean statusBar) {
        this.statusBar = statusBar;
    }

    /**
     * @return the currentFont
     */
    public Font getCurrentFont() {
        if (currentFont == null) {
            currentFont = new Font(currentFontName, currentFontStyle, currentFontSize);
        }
        
        return currentFont;
    }

    /**
     * @param currentFont the currentFont to set
     */
    public void setCurrentFont(Font currentFont) {
        this.currentFontName = currentFont.getFontName();
        this.currentFontStyle = currentFont.getStyle();
        this.currentFontSize = currentFont.getSize();
    }

    /**
     * @return the recentDocuments
     */
    public List<String> getRecentDocuments() {
        return recentDocuments;
    }

    /**
     * @param recentDocuments the recentDocuments to set
     */
    public void setRecentDocuments(List<String> recentDocuments) {
        this.recentDocuments = recentDocuments;
    }
    
    /**
     * @param document
     */
    public void addDocument(String document) {
        recentDocuments.remove(document);
        recentDocuments.add(0, document);
    }

    /**
     * @return the configurationPath
     */
    public File getConfigurationPath() {
        return configurationPath;
    }
    
    /**
     * @param dialog
     */
    public void setThemeDialogLocation(JDialog dialog) {
        themeDialogX = dialog.getX();
        themeDialogY = dialog.getY();
        themeDialogWidth = dialog.getWidth();
        themeDialogHeight = dialog.getHeight();
    }

    /**
     * @param dialog
     */
    public void getThemeDialogLocation(JDialog dialog) {
        dialog.setBounds(themeDialogX, themeDialogY, themeDialogWidth, themeDialogHeight);
    }

    /**
     * @return the themeDialogMainDividerLocation
     */
    public int getThemeDialogMainDividerLocation() {
        return themeDialogMainDividerLocation;
    }

    /**
     * @param themeDialogMainDividerLocation the themeDialogMainDividerLocation to set
     */
    public void setThemeDialogMainDividerLocation(int themeDialogMainDividerLocation) {
        this.themeDialogMainDividerLocation = themeDialogMainDividerLocation;
    }

    /**
     * @return the themeDialogStyleDividerLocation
     */
    public int getThemeDialogStyleDividerLocation() {
        return themeDialogStyleDividerLocation;
    }

    /**
     * @param themeDialogStyleDividerLocation the themeDialogStyleDividerLocation to set
     */
    public void setThemeDialogStyleDividerLocation(int themeDialogStyleDividerLocation) {
        this.themeDialogStyleDividerLocation = themeDialogStyleDividerLocation;
    }

    /**
     * @return true if the status bar is to be shown
     */
    public boolean isShowStatusBar() {
        return showStatusBar;
    }

    /**
     * @param showStatusBar
     */
    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
    }

    /**
     * @return the spacesInsteadOfTabs
     */
    public boolean isSpacesInsteadOfTabs() {
        return spacesInsteadOfTabs;
    }

    /**
     * @param spacesInsteadOfTabs the spacesInsteadOfTabs to set
     */
    public void setSpacesInsteadOfTabs(boolean spacesInsteadOfTabs) {
        this.spacesInsteadOfTabs = spacesInsteadOfTabs;
    }

    /**
     * @return the tabSize
     */
    public int getTabSize() {
        return tabSize;
    }

    /**
     * @param tabSize the tabSize to set
     */
    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    /**
     * @return the convertTabsOrSpacesOnSave
     */
    public boolean isConvertTabsOrSpacesOnSave() {
        return convertTabsOrSpacesOnSave;
    }

    /**
     * @param convertTabsOrSpacesOnSave the convertTabsOrSpacesOnSave to set
     */
    public void setConvertTabsOrSpacesOnSave(boolean convertTabsOrSpacesOnSave) {
        this.convertTabsOrSpacesOnSave = convertTabsOrSpacesOnSave;
    }

    /**
     * @return the enableLineNumbers
     */
    public boolean isEnableLineNumbers() {
        return enableLineNumbers;
    }

    /**
     * @param enableLineNumbers the enableLineNumbers to set
     */
    public void setEnableLineNumbers(boolean enableLineNumbers) {
        this.enableLineNumbers = enableLineNumbers;
    }

    /**
     * @return the configurationDialogX
     */
    public int getConfigurationDialogX() {
        return configurationDialogX;
    }

    /**
     * @param configurationDialogX the configurationDialogX to set
     */
    public void setConfigurationDialogX(int configurationDialogX) {
        this.configurationDialogX = configurationDialogX;
    }

    /**
     * @return the configurationDialogY
     */
    public int getConfigurationDialogY() {
        return configurationDialogY;
    }

    /**
     * @param configurationDialogY the configurationDialogY to set
     */
    public void setConfigurationDialogY(int configurationDialogY) {
        this.configurationDialogY = configurationDialogY;
    }

}
