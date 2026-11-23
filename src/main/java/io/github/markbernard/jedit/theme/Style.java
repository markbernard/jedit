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

import java.util.Objects;

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
    
    @Override
    public int hashCode() {
        return Objects.hash(bold, fg, italic, token);
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
        Style other = (Style) obj;

        return Objects.equals(bold, other.bold) && Objects.equals(fg, other.fg) && Objects.equals(italic, other.italic) && Objects.equals(token, other.token);
    }
}
