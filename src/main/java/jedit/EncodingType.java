/**
 * 
 */
package jedit;

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
    /** */
    public static Set<Charset> supportedEncodings = new TreeSet<>((o1, o2) -> o1.displayName().compareTo(o2.displayName()));
    /** */
    public static Map<Charset, Set<String>> encodingAliasMap = new HashMap<>();
    
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
}
