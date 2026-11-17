/**
 * 
 */
package jedit;

import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 * 
 */
public class DocumentFormatType {
    /** Map of all file types that are supported */
    public static final Map<String, LanguageInfo> FILE_TYPE_MAP = new HashMap<>();
    
    static {
        FILE_TYPE_MAP.put("asm", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86, "Assembler"));
        FILE_TYPE_MAP.put("c", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_C, "C"));
        FILE_TYPE_MAP.put("cpp", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "C++"));
        FILE_TYPE_MAP.put("cs", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSHARP, "C#"));
        FILE_TYPE_MAP.put("css", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSS, "CSS"));
        FILE_TYPE_MAP.put("csv", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSV, "CSV"));
        FILE_TYPE_MAP.put("dockerfile", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, "Dockerfile"));
        FILE_TYPE_MAP.put("dtd", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_DTD, "Document Type Definition"));
        FILE_TYPE_MAP.put("env", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_ENV, "Environment file"));
        FILE_TYPE_MAP.put("go", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_GO, "Go"));
        FILE_TYPE_MAP.put("groovy", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_GROOVY, "Groovy"));
        FILE_TYPE_MAP.put("hosts", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HOSTS, "Hosts"));
        FILE_TYPE_MAP.put("htaccess", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTACCESS, "HTAccess"));
        FILE_TYPE_MAP.put("html", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTML, "Hypertext Markup Language"));
        FILE_TYPE_MAP.put("htm", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTML, "Hypertext Markup Language"));
        FILE_TYPE_MAP.put("ini", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_INI, "INI"));
        FILE_TYPE_MAP.put("java", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JAVA, "Java"));
        FILE_TYPE_MAP.put("js", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, "Javascript"));
        FILE_TYPE_MAP.put("json", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSON, "Javascript Object Notation"));
        FILE_TYPE_MAP.put("jshintrc", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS, "Javascript Object Notation with comments"));
        FILE_TYPE_MAP.put("jsp", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSP, "Java Server Page"));
        FILE_TYPE_MAP.put("kotlin", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_KOTLIN, "Kotlin"));
        FILE_TYPE_MAP.put("less", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_LESS, "Less"));
        FILE_TYPE_MAP.put("lua", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_LUA, "Lua"));
        FILE_TYPE_MAP.put("makefile", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, "Makefile"));
        FILE_TYPE_MAP.put("md", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, "Markdown"));
        FILE_TYPE_MAP.put("markdown", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, "Markdown"));
        FILE_TYPE_MAP.put("mxml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MXML, "MXML"));
        FILE_TYPE_MAP.put("perl", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PERL, "Perl"));
        FILE_TYPE_MAP.put("php", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PHP, "PHP Hypertext Processor"));
        FILE_TYPE_MAP.put("properties", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, "Properties"));
        FILE_TYPE_MAP.put("rb", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_RUBY, "Ruby"));
        FILE_TYPE_MAP.put("rust", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_RUST, "Rust"));
        FILE_TYPE_MAP.put("sas", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_SAS, "SAS"));
        FILE_TYPE_MAP.put("scala", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_SCALA, "Scala"));
        FILE_TYPE_MAP.put("tcl", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_TCL, "TCL"));
        FILE_TYPE_MAP.put("ts", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT, "Typescript"));
        FILE_TYPE_MAP.put("sh", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, "Shell SCript"));
        FILE_TYPE_MAP.put("vb", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC, "Visual Basic"));
        FILE_TYPE_MAP.put("bat", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH, "Windows Batch"));
        FILE_TYPE_MAP.put("xml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_XML, "Extensible Markup Language"));
        FILE_TYPE_MAP.put("yml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_YAML, "Yaml"));
    }
    
    /**
     * Figure out the format type based on the extension
     * 
     * @param filePath
     * @return the format type
     */
    public static LanguageInfo determineFormatFromFile(String filePath) {
        LanguageInfo result = new LanguageInfo("None", SyntaxConstants.SYNTAX_STYLE_NONE);
        
        int index = filePath.lastIndexOf(".");
        if (index > -1) {
            String extension = filePath.substring(index + 1).toLowerCase();
            LanguageInfo languageInfo = FILE_TYPE_MAP.get(extension);
            if (languageInfo != null) {
                result = languageInfo;
            }
        }
        
        return result;
    }
}
