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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * 
 * @author Mark Bernard
 */
@Root
public class Style {
    /** */
    @Attribute(required = false)
    public Boolean bold;
    /** */
    @Attribute(required = false)
    public String fg;
    /** */
    @Attribute(required = false)
    public Boolean italic;
    /** */
    @Attribute(required = true)
    public String token;
    
    /**
     * @param other
     * @return true if all fields are equal
     */
    public boolean equals(Style other) {
        boolean result = true;
        
        result &= ((bold == null && other.bold == null) || bold.equals(other.bold)); 
        result &= (fg == null && other.fg == null) || fg.equals(other.fg);
        result &= ((italic == null && other.italic == null) || italic.equals(other.italic)); 
        result &= token.equals(other.token);
        
        return result;
    }
}
