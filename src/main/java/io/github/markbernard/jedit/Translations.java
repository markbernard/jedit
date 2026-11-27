/**
 * 
 */
package io.github.markbernard.jedit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class Translations {
    private static final Logger LOGGER = LoggerFactory.getLogger(Translations.class);
    private static ResourceBundle bundle = null;
    
    private Translations() {}
    
    /**
     * @param key
     * @return the language string for the given key
     */
    public static String getString(String key) {
        return getString(key, (String[]) null);
    }

    /**
     * @param key
     * @param params
     * @return the language string for the given key, inserting parameters where needed
     */
    public static String getString(String key,  String... params) {
        String result = key;
        
        if (bundle == null) {
            try {
                Locale locale = Locale.getDefault();
                InputStream stream =  Translations.class.getResourceAsStream("/text/jedit_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
                if (stream == null) {
                    stream =  Translations.class.getResourceAsStream("/text/jedit_" + locale.getLanguage() + ".properties");
                    if (stream == null) {
                        stream = Translations.class.getResourceAsStream("/text/jedit.properties");
                    }
                }
                bundle = new PropertyResourceBundle(stream);
            } catch (IOException e) {
                LOGGER.error("Could not load translation file for: {}", key, e);
            }
        }
        
        if (bundle.containsKey(key)) {
            result = bundle.getString(key);

            if (params != null) {
                for (int i=0;i<params.length;i++) {
                    result = result.replaceAll("\\$\\{" + i + "\\}", params[i]);
                }
            }
        }
        
        return result;
    }
}
