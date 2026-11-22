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
package io.github.markbernard.jedit.theme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import io.github.markbernard.jedit.ApplicationPreferences;
import io.github.markbernard.jedit.DocumentFormatType;
import io.github.markbernard.jedit.LanguageInfo;

/**
 * 
 */
public class ThemeManager {
    private static String themePathString = ApplicationPreferences.getInstance().getConfigurationPath().getAbsolutePath() + "/themes";
    private static File themePath;
    
    /**
     * @param theme
     * @param langaugeExtension 
     * @throws Exception 
     */
    public static void save(RSyntaxTheme theme, String langaugeExtension) throws Exception {
        initPath();
        Format format = new Format(4, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<!DOCTYPE RSyntaxTheme SYSTEM \"theme.dtd\">");
        Serializer serializer = new Persister(format);
        try (Writer out = new OutputStreamWriter(new FileOutputStream(new File(themePathString + "/" + langaugeExtension + "_theme.xml")), StandardCharsets.UTF_8)) {
            serializer.write(theme, out);
            out.write("\n");
        }
    }
    
    /**
     * @param langaugeExtension
     * @return the loaded theme or null if not found
     * @throws Exception
     */
    public static RSyntaxTheme load(String langaugeExtension) throws Exception {
        RSyntaxTheme rSyntaxTheme = null;
        initPath();
        File themeFile = new File(themePathString + "/" + langaugeExtension + "_theme.xml");
        if (themeFile.exists()) {
            Serializer serializer = new Persister();
            try (Reader in = new InputStreamReader(new FileInputStream(themeFile), StandardCharsets.UTF_8)) {
                rSyntaxTheme = serializer.read(RSyntaxTheme.class, in);
            }
        }
        
        return rSyntaxTheme;
    }
    
    /**
     * @param langaugeExtension
     * @return the default theme from the RSyntaxTextArea
     */
    public static RSyntaxTheme getDefaultTheme(String langaugeExtension) {
        RSyntaxTheme rSyntaxTheme = null;
        
        try (OutputStream out = new FileOutputStream(new File(themePathString + "/" + langaugeExtension + "_theme.xml"))) {
            RSyntaxTextArea area = new RSyntaxTextArea();
            new RTextScrollPane(area); // required to get all parts the the theme
            LanguageInfo languageInfo =  DocumentFormatType.determineFormatFromFile("." + langaugeExtension);
            area.setSyntaxEditingStyle(languageInfo.getLanguage());
            Theme theme = new Theme(area);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            theme.save(bout);
            out.write(bout.toByteArray());
            Serializer serializer = new Persister();
            rSyntaxTheme = serializer.read(RSyntaxTheme.class, new ByteArrayInputStream(bout.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return rSyntaxTheme;
    }
    
    /**
     * @param langaugeExtension
     * @return the loaded theme or null if not found
     * @throws Exception
     */
    public static Theme loadNativeTheme(String langaugeExtension) throws Exception {
        Theme theme = null;
        initPath();
        File themeFile = new File(themePathString + "/" + langaugeExtension + "_theme.xml");
        if (themeFile.exists()) {
            try (InputStream in = new FileInputStream(themeFile)) {
                theme = Theme.load(in);
            }
        }
        
        return theme;
    }
    
    private static void initPath() {
        if (themePath == null) {
            themePath = new File(themePathString);
            if (!themePath.exists()) {
                themePath.mkdirs();
            }
        }
    }
}
