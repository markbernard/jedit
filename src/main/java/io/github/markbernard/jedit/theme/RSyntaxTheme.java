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
import java.util.Objects;

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
    
    
    @Override
    public int hashCode() {
        return Objects.hash(background, baseFont, caret, currentLineHighlight, foldIndicator, gutterBackground, gutterBorder, hyperlinks, iconRowHeader, lineNumbers, marginLine, markAllHighlight,
                markOccurrencesHighlight, matchedBracket, secondaryLanguages, selection, tabLine, tokenStyles, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RSyntaxTheme other = (RSyntaxTheme) obj;
        return Objects.equals(background, other.background) && Objects.equals(baseFont, other.baseFont) && Objects.equals(caret, other.caret)
                && Objects.equals(currentLineHighlight, other.currentLineHighlight) && Objects.equals(foldIndicator, other.foldIndicator) && Objects.equals(gutterBackground, other.gutterBackground)
                && Objects.equals(gutterBorder, other.gutterBorder) && Objects.equals(hyperlinks, other.hyperlinks) && Objects.equals(iconRowHeader, other.iconRowHeader)
                && Objects.equals(lineNumbers, other.lineNumbers) && Objects.equals(marginLine, other.marginLine) && Objects.equals(markAllHighlight, other.markAllHighlight)
                && Objects.equals(markOccurrencesHighlight, other.markOccurrencesHighlight) && Objects.equals(matchedBracket, other.matchedBracket)
                && Objects.equals(secondaryLanguages, other.secondaryLanguages) && Objects.equals(selection, other.selection) && Objects.equals(tabLine, other.tabLine)
                && Objects.equals(tokenStyles, other.tokenStyles) && Objects.equals(version, other.version);
    }
}
