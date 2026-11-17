/**
 * 
 */
package jedit;

import java.util.Objects;

/**
 * 
 */
public class LanguageInfo {
    private String language;
    private String name;
    private String description;
    private String extension;
    
    /**
     * @param language
     * @param name
     * @param extension 
     */
    public LanguageInfo(String language, String name, String extension) {
        this.language = language;
        this.name = name;
        this.extension = extension;
    }
    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }
    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    @Override
    public int hashCode() {
        return Objects.hash(language);
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
        LanguageInfo other = (LanguageInfo) obj;
        return Objects.equals(language, other.language);
    }
    @Override
    public String toString() {
        return "LanguageInfo "
                + "[language=" + language 
                + ", name=" + name 
                + ", description=" + description
                + ", extension=" + extension
                + "]";
    }
}
