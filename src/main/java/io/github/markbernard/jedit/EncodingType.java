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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 */
public class EncodingType {
    private EncodingType() {}
    
    /** */
    private static Set<Charset> supportedEncodings = new TreeSet<>((o1, o2) -> o1.displayName().compareTo(o2.displayName()));
    /** */
    private static Map<Charset, Set<String>> encodingAliasMap = new HashMap<>();
    
    static {
        supportedEncodings.add(StandardCharsets.ISO_8859_1);
        supportedEncodings.add(StandardCharsets.US_ASCII);
        supportedEncodings.add(StandardCharsets.UTF_16);
        supportedEncodings.add(StandardCharsets.UTF_16BE);
        supportedEncodings.add(StandardCharsets.UTF_16LE);
        supportedEncodings.add(StandardCharsets.UTF_8);

        encodingAliasMap.put(StandardCharsets.ISO_8859_1, StandardCharsets.ISO_8859_1.aliases());
        encodingAliasMap.put(StandardCharsets.US_ASCII, StandardCharsets.US_ASCII.aliases());
        encodingAliasMap.put(StandardCharsets.UTF_16, StandardCharsets.UTF_16.aliases());
        encodingAliasMap.put(StandardCharsets.UTF_16BE, StandardCharsets.UTF_16BE.aliases());
        encodingAliasMap.put(StandardCharsets.UTF_16LE, StandardCharsets.UTF_16LE.aliases());
        encodingAliasMap.put(StandardCharsets.UTF_8, StandardCharsets.UTF_8.aliases());
    }

    /**
     * @return the supportedEncodings
     */
    public static Set<Charset> getSupportedEncodings() {
        return new TreeSet<>(supportedEncodings);
    }

    /**
     * @return the encodingAliasMap
     */
    public static Map<Charset, Set<String>> getEncodingAliasMap() {
        return new HashMap<>(encodingAliasMap);
    }
}
