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
        FILE_TYPE_MAP.put("asm", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86, "Assembler", "asm"));
        FILE_TYPE_MAP.put("c", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_C, "C", "c"));
        FILE_TYPE_MAP.put("cpp", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "C++", "cpp"));
        FILE_TYPE_MAP.put("cs", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSHARP, "C#", "cs"));
        FILE_TYPE_MAP.put("css", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSS, "CSS", "css"));
        FILE_TYPE_MAP.put("csv", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_CSV, "CSV", "csv"));
        FILE_TYPE_MAP.put("dockerfile", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, "Dockerfile", "dockerfile"));
        FILE_TYPE_MAP.put("dtd", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_DTD, "Document Type Definition", "dtd"));
        FILE_TYPE_MAP.put("env", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_ENV, "Environment file", "env"));
        FILE_TYPE_MAP.put("go", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_GO, "Go", "go"));
        FILE_TYPE_MAP.put("groovy", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_GROOVY, "Groovy", "groovy"));
        FILE_TYPE_MAP.put("hosts", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HOSTS, "Hosts", "hosts"));
        FILE_TYPE_MAP.put("htaccess", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTACCESS, "HTAccess", "htaccess"));
        FILE_TYPE_MAP.put("html", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTML, "Hypertext Markup Language", "html"));
        FILE_TYPE_MAP.put("htm", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_HTML, "Hypertext Markup Language", "htm"));
        FILE_TYPE_MAP.put("ini", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_INI, "INI", "ini"));
        FILE_TYPE_MAP.put("java", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JAVA, "Java", "java"));
        FILE_TYPE_MAP.put("js", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, "Javascript", "js"));
        FILE_TYPE_MAP.put("json", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSON, "Javascript Object Notation", "json"));
        FILE_TYPE_MAP.put("jshintrc", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS, "Javascript Object Notation with comments", "jshintrc"));
        FILE_TYPE_MAP.put("jsp", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_JSP, "Java Server Page", "jsp"));
        FILE_TYPE_MAP.put("kotlin", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_KOTLIN, "Kotlin", "kotlin"));
        FILE_TYPE_MAP.put("less", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_LESS, "Less", "less"));
        FILE_TYPE_MAP.put("lua", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_LUA, "Lua", "lua"));
        FILE_TYPE_MAP.put("makefile", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, "Makefile", "makefile"));
        FILE_TYPE_MAP.put("md", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, "Markdown", "md"));
        FILE_TYPE_MAP.put("markdown", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, "Markdown", "markdown"));
        FILE_TYPE_MAP.put("mxml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_MXML, "MXML", "mxml"));
        FILE_TYPE_MAP.put("perl", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PERL, "Perl", "perl"));
        FILE_TYPE_MAP.put("php", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PHP, "PHP Hypertext Processor", "php"));
        FILE_TYPE_MAP.put("properties", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, "Properties", "properties"));
        FILE_TYPE_MAP.put("rb", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_RUBY, "Ruby", "rb"));
        FILE_TYPE_MAP.put("rust", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_RUST, "Rust", "rust"));
        FILE_TYPE_MAP.put("sas", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_SAS, "SAS", "sas"));
        FILE_TYPE_MAP.put("scala", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_SCALA, "Scala", "scala"));
        FILE_TYPE_MAP.put("tcl", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_TCL, "TCL", "tcl"));
        FILE_TYPE_MAP.put("ts", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT, "Typescript", "ts"));
        FILE_TYPE_MAP.put("sh", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, "Shell Script", "sh"));
        FILE_TYPE_MAP.put("vb", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC, "Visual Basic", "vb"));
        FILE_TYPE_MAP.put("bat", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH, "Windows Batch", "bat"));
        FILE_TYPE_MAP.put("xml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_XML, "Extensible Markup Language", "xml"));
        FILE_TYPE_MAP.put("yml", new LanguageInfo(SyntaxConstants.SYNTAX_STYLE_YAML, "Yaml", "yml"));
    }
    
    /**
     * Figure out the format type based on the extension
     * 
     * @param filePath
     * @return the format type
     */
    public static LanguageInfo determineFormatFromFile(String filePath) {
        LanguageInfo result = new LanguageInfo("None", SyntaxConstants.SYNTAX_STYLE_NONE, "");
        
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
