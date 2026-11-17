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
    /**
     * @param language
     * @param name
     */
    public LanguageInfo(String language, String name) {
        super();
        this.language = language;
        this.name = name;
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
        return "LanguageInfo [language=" + language + ", name=" + name + ", description=" + description + "]";
    }
}
