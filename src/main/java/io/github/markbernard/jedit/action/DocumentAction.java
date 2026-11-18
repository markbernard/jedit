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
package io.github.markbernard.jedit.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.Action;

import io.github.markbernard.jedit.ApplicationPreferences;
import io.github.markbernard.jedit.IconGenerator;
import io.github.markbernard.jedit.JEdit;
import io.github.markbernard.jedit.LanguageInfo;

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
        
        private JEdit jNotepad;

        /**
         * @param jNotepad
         * @return Single instance
         */
        public static WordWrapAction getInstance(JEdit jNotepad) {
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
        private WordWrapAction(JEdit jNotepad) {
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

            private JEdit jNotepad;
            private LanguageInfo languageInfo;

            /**
             * @param jNotepad
             * @param languageInfo
             */
            public LanguageChoiceAction(JEdit jNotepad, LanguageInfo languageInfo) {
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

            private JEdit jNotepad;
            private Charset charset;

            /**
             * @param jNotepad
             * @param charset
             */
            public CharsetAction(JEdit jNotepad, Charset charset) {
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
