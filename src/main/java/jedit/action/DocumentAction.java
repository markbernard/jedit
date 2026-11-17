/**
 * 
 */
package jedit.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.Action;

import jedit.ApplicationPreferences;
import jedit.IconGenerator;
import jedit.JNotepad;
import jedit.LanguageInfo;

/**
 * 
 */
public class DocumentAction extends AbstractAction {
    private static final long serialVersionUID = 6466036707974043765L;

    /**
     * Set up values appropriate to the Undo menu item.
     */
    public DocumentAction() {
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(NAME, "Document");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * @author Mark Bernard
     */
    public static class WordWrapAction extends AbstractAction {
        private static final long serialVersionUID = -7155740870533895459L;
        private static WordWrapAction instance;
        
        private JNotepad jNotepad;

        /**
         * @param jNotepad
         * @return Single instance
         */
        public static WordWrapAction getInstance(JNotepad jNotepad) {
            if (instance == null) {
                instance = new WordWrapAction(jNotepad);
            }
            
            return instance;
        }
        
        /**
         * Set up values for the Word Wrap menu
         * 
         * @param jNotepad 
         */
        private WordWrapAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
            putValue(Action.NAME, "Word Wrap");
            putValue(Action.SHORT_DESCRIPTION, "Toggle soft word wrap on or off.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/WordWrapSmall" +
                    (ApplicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/WordWrap" +
                    (ApplicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApplicationPreferences.setWordWrap(!ApplicationPreferences.isWordWrap());
                    putValue(Action.SMALL_ICON, new IconGenerator("/icons/WordWrapSmall" +
                            (ApplicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
                    putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/WordWrap" +
                            (ApplicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
                    jNotepad.wordWrap();
                }
            }, "Word Wrap").start();
        }
        
    }

    /**
     * 
     */
    public static class LanguageAction extends AbstractAction {
        private static final long serialVersionUID = 7931332636444460703L;
        
        /**
         * @param jNotepad
         */
        public LanguageAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(NAME, "Language");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
        
        /**
         * 
         */
        public static class LanguageChoiceAction extends AbstractAction {
            private static final long serialVersionUID = -6070781993396455988L;

            private JNotepad jNotepad;
            private LanguageInfo languageInfo;

            /**
             * @param jNotepad
             * @param languageInfo
             */
            public LanguageChoiceAction(JNotepad jNotepad, LanguageInfo languageInfo) {
                this.jNotepad = jNotepad;
                this.languageInfo = languageInfo;
                putValue(NAME, languageInfo.getName());
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    jNotepad.setLanguage(languageInfo);
                },
                "Change language to " + languageInfo.getName()).start();
            }
        
        }
    }
    
    /**
     * 
     */
    public static class EncodingAction extends AbstractAction {
        private static final long serialVersionUID = 5811551375928838430L;
        
        /**
         * 
         */
        public EncodingAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_E);
            putValue(NAME, "Encoding");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
        /**
         * 
         */
        public static class CharsetAction extends AbstractAction {
            private static final long serialVersionUID = 8848351864190448190L;

            private JNotepad jNotepad;
            private Charset charset;

            /**
             * @param jNotepad
             * @param charset
             */
            public CharsetAction(JNotepad jNotepad, Charset charset) {
                this.jNotepad = jNotepad;
                this.charset = charset;
                putValue(NAME, charset.displayName());
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    jNotepad.setCharset(charset);
                },
                "Change Character set to " + charset.displayName()).start();
            }
        }
    }
}
