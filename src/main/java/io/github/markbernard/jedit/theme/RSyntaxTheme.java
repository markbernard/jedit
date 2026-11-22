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

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * XML structure for RSyntaxTextArea themes
 * 
 * @author Mark Bernard
 */
@Root
public class RSyntaxTheme {
    /** */
    @Attribute
    public String version;
    /** */
    @Element
    public BaseFont baseFont;
    /** */
    @Element
    public Background background;
    /** */
    @Element
    public Caret caret;
    /** */
    @Element
    public Selection selection;
    /** */
    @Element                      
    public CurrentLineHighlight currentLineHighlight;
    /** */
    @Element
    public TabLine tabLine;
    /** */
    @Element
    public MarginLine marginLine;
    /** */
    @Element
    public MarkAllHighlight markAllHighlight;
    /** */
    @Element
    public MarkOccurrencesHighlight markOccurrencesHighlight;
    /** */
    @Element
    public MatchedBracket matchedBracket;
    /** */
    @Element
    public Hyperlinks hyperlinks;
    /** */
    @Path("secondaryLanguages")
    @ElementList(inline = true)
    public List<Language> secondaryLanguages;
    /** */
    @Element
    public GutterBackground gutterBackground;
    /** */
    @Element
    public GutterBorder gutterBorder;
    /** */
    @Element
    public LineNumbers lineNumbers;
    /** */
    @Element
    public FoldIndicator foldIndicator;
    /** */
    @Element
    public IconRowHeader iconRowHeader;
    /** */
    @Path("tokenStyles")
    @ElementList(inline = true)
    public List<Style> tokenStyles;
    
    /**
     * @param themeToCheck
     * @return true if all fields are equal
     */
    public boolean equals(RSyntaxTheme themeToCheck) {
        boolean result = baseFont.equals(themeToCheck.baseFont);
        result &= background.equals(themeToCheck.background);
        result &= caret.equals(themeToCheck.caret);
        result &= selection.equals(themeToCheck.selection);
        result &= currentLineHighlight.equals(themeToCheck.currentLineHighlight);
        result &= tabLine.equals(themeToCheck.tabLine);
        result &= marginLine.equals(themeToCheck.marginLine);
        result &= markAllHighlight.equals(themeToCheck.markAllHighlight);
        result &= markOccurrencesHighlight.equals(themeToCheck.markOccurrencesHighlight);
        result &= matchedBracket.equals(themeToCheck.matchedBracket);
        result &= hyperlinks.equals(themeToCheck.hyperlinks);
        result &= gutterBackground.equals(themeToCheck.gutterBackground);
        result &= gutterBorder.equals(themeToCheck.gutterBorder);
        result &= lineNumbers.equals(themeToCheck.lineNumbers);
        result &= foldIndicator.equals(themeToCheck.foldIndicator);
        result &= iconRowHeader.equals(themeToCheck.iconRowHeader);
        if (secondaryLanguages.size() == themeToCheck.secondaryLanguages.size()) {
            for (int i=0;i<secondaryLanguages.size();i++) {
                result &= secondaryLanguages.get(i).equals(themeToCheck.secondaryLanguages.get(i));
            }
        } else {
            result = false;
        }
        if (tokenStyles.size() == themeToCheck.tokenStyles.size()) {
            for (int i=0;i<tokenStyles.size();i++) {
                result &= tokenStyles.get(i).equals(themeToCheck.tokenStyles.get(i));
            }
        } else {
            result = false;
        }

        return result;
    }
}
