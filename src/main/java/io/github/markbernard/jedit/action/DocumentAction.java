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
import io.github.markbernard.jedit.Translations;

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
        putValue(NAME, Translations.getString("DialogAction.constuctor.document"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** provides menu controls but needs no action */
    }

    /**
     * @author Mark Bernard
     */
    public static class WordWrapAction extends AbstractAction {
        private static final long serialVersionUID = -7155740870533895459L;
        private static WordWrapAction instance;
        
        private final transient JEdit jEdit;
        private final transient ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();

        /**
         * @param jEdit
         * @return Single instance
         */
        public static WordWrapAction getInstance(JEdit jEdit) {
            if (instance == null) {
                instance = new WordWrapAction(jEdit);
            }
            
            return instance;
        }
        
        /**
         * Set up values for the Word Wrap menu
         * 
         * @param jEdit 
         */
        private WordWrapAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
            putValue(Action.NAME, Translations.getString("DialogAction.WordWrapAction.wordwrap"));
            putValue(Action.SHORT_DESCRIPTION, Translations.getString("DialogAction.WordWrapAction.constuctor.wordwrap.description"));
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/WordWrapSmall" + (applicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/WordWrap" + (applicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                applicationPreferences.setWordWrap(!applicationPreferences.isWordWrap());
                putValue(Action.SMALL_ICON, new IconGenerator("/icons/WordWrapSmall" + (applicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
                putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/WordWrap" + (applicationPreferences.isWordWrap() ? "On" : "Off") + ".png").loadImage());
                jEdit.wordWrap();
            }, Translations.getString("DialogAction.WordWrapAction.wordwrap")).start();
        }
        
    }

    /**
     * 
     */
    public static class LanguageAction extends AbstractAction {
        private static final long serialVersionUID = 7931332636444460703L;
        
        /**
         * @param jEdit
         */
        public LanguageAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(NAME, Translations.getString("DialogAction.LanguageAction.constuctor.language"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /** provides menu controls but needs no action */
        }
        
        
        /**
         * 
         */
        public static class LanguageChoiceAction extends AbstractAction {
            private static final long serialVersionUID = -6070781993396455988L;

            private final transient JEdit jEdit;
            private final transient LanguageInfo languageInfo;

            /**
             * @param jEdit
             * @param languageInfo
             */
            public LanguageChoiceAction(JEdit jEdit, LanguageInfo languageInfo) {
                this.jEdit = jEdit;
                this.languageInfo = languageInfo;
                putValue(NAME, languageInfo.getName());
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    jEdit.setLanguage(languageInfo);
                }, "Change Language").start();
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
            putValue(NAME, Translations.getString("DialogAction.EncodingAction.constuctor.encoding"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /** provides menu controls but needs no action */
        }
        
        /**
         * 
         */
        public static class CharsetAction extends AbstractAction {
            private static final long serialVersionUID = 8848351864190448190L;

            private final transient JEdit jEdit;
            private final transient Charset charset;

            /**
             * @param jNotepad
             * @param charset
             */
            public CharsetAction(JEdit jNotepad, Charset charset) {
                this.jEdit = jNotepad;
                this.charset = charset;
                putValue(NAME, charset.displayName());
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    jEdit.setCharset(charset);
                }, Translations.getString("DialogAction.EncodingAction.CharsetAction.actionPerformed.changeset", charset.displayName())).start();
            }
        }
    }
}
