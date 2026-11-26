/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Mark Bernard
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

import javax.swing.AbstractAction;
import javax.swing.Action;

import io.github.markbernard.jedit.IconGenerator;
import io.github.markbernard.jedit.JEdit;
import io.github.markbernard.jedit.Translations;

/**
 * @author Mark Bernard
 */
public class HelpAction extends AbstractAction {
    private static final long serialVersionUID = -4083560520034375524L;

    /**
     * 
     */
    public HelpAction() {
        putValue(NAME, Translations.getString("HelpAction.constuctor.help"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** provides menu controls but needs no action */
    }
    
    /**
     * @author Mark Bernard
     */
    public static class ViewHelpAction extends AbstractAction {
        private static final long serialVersionUID = -1640517285601598321L;
        private final transient JEdit jEdit;

        /**
         * @param jEdit
         */
        public ViewHelpAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(NAME, Translations.getString("HelpAction.ViewHelpAction.constuctor.viewhelp"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_V);
            putValue(Action.SHORT_DESCRIPTION, Translations.getString("HelpAction.ViewHelpAction.constuctor.open"));
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/HelpSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/Help.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::help, "Help").start();
        }
    }


    /**
     * @author Mark Bernard
     */
    public static class AboutAction extends AbstractAction {
        private static final long serialVersionUID = -1640517285601598321L;
        private final transient JEdit jEdit;

        /**
         * @param jEdit
         */
        public AboutAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(NAME, Translations.getString("HelpAction.AboutAction.constuctor.about"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/AboutSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/About.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::about, "About").start();
        }
    }
}
