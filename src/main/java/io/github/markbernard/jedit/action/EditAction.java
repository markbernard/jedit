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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;

import io.github.markbernard.jedit.IconGenerator;
import io.github.markbernard.jedit.JEdit;
import io.github.markbernard.jedit.dialog.SearchDialog;

/**
 * Takes care of all actions for the Edit menu. Menu items will be internal classes of this class.
 * 
 * @author Mark Bernard
 */
public class EditAction extends AbstractAction {
    private static final long serialVersionUID = 2228283050016546717L;

    /**
     * Set up values appropriate to the Undo menu item.
     */
    public EditAction() {
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(NAME, "Edit");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** provides menu controls but needs no action */
    }

    /**
     * Actions for the Edit > Undo menu item.
     * 
     * @author Mark Bernard
     */
    public static class UndoAction extends AbstractAction {
        private static final long serialVersionUID = 5936711805304319306L;
        
        private final transient JEdit jEdit;
        
        /**
         * Set up values appropriate to the Undo menu item.
         * 
         * @param jEdit 
         */
        public UndoAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Undo");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/UndoSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/Undo.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::undo, "Undo").start();
        }
    }

    /**
     * Actions for the Edit > Redo menu item.
     * 
     * @author Mark Bernard
     */
    public static class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 6599265610327179684L;

        private final transient JEdit jEdit;
        
        /**
         * Set up values appropriate to the Undo menu item.
         * 
         * @param jEdit 
         */
        public RedoAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Redo");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/RedoSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/Redo.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::redo, "Redo").start();
        }
    }

    /**
     * Actions for the Edit > Cut menu item.
     * 
     * @author Mark Bernard
     */
    public static class CutAction extends AbstractAction {
        private static final long serialVersionUID = 6520482354999730690L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Cut menu item.
         * 
         * @param jEdit 
         */
        public CutAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_T);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Cut");
            putValue(SHORT_DESCRIPTION, "Remove the current selection from the document and put it in the clipboard.");
            putValue(SMALL_ICON, new IconGenerator("/icons/CutSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Cut.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::cut, "Cut").start();
        }
    }

    /**
     * Actions for the Edit > Copy menu item.
     * 
     * @author Mark Bernard
     */
    public static class CopyAction extends AbstractAction {
        private static final long serialVersionUID = -2906245309033009826L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Copy menu item.
         * 
         * @param jEdit 
         */
        public CopyAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Copy");
            putValue(SHORT_DESCRIPTION, "Copy the current selection from the document and put it in the clipboard.");
            putValue(SMALL_ICON, new IconGenerator("/icons/CopySmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Copy.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::copy, "Copy").start();
        }
    }

    /**
     * Actions for the Edit > Paste menu item.
     * 
     * @author Mark Bernard
     */
    public static class PasteAction extends AbstractAction {
        private static final long serialVersionUID = -555624621599811242L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Paste menu item.
         * 
         * @param jEdit 
         */
        public PasteAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Paste");
            putValue(SHORT_DESCRIPTION, "Paste the clipboard contents into the current cursor location or selection.");
            putValue(SMALL_ICON, new IconGenerator("/icons/PasteSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Paste.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::paste, "Paste").start();
        }
    }

    /**
     * Actions for the Edit > Find menu item.
     * 
     * @author Mark Bernard
     */
    public static class FindAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Find menu item.
         * 
         * @param jEdit 
         */
        public FindAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Find...");
            putValue(SHORT_DESCRIPTION, "Open the find dialog.");
            putValue(SMALL_ICON, new IconGenerator("/icons/FindSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Find.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::find, "Find").start();
        }
    }

    /**
     * Actions for the Edit > Find Next menu item.
     * 
     * @author Mark Bernard
     */
    public static class FindNextAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;
        private final SearchDialog searchDialog;

        /**
         * Set up values appropriate to the Find Next menu item.
         * 
         * @param jEdit 
         * @param searchDialog 
         */
        public FindNextAction(JEdit jEdit, SearchDialog searchDialog) {
            this.jEdit = jEdit;
            this.searchDialog = searchDialog;
            if (searchDialog == null) {
                putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            } else {
                putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            }
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Find Next");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/FindNextSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/FindNext.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                    if (searchDialog != null) {
                        jEdit.setFindTerm(searchDialog.getFindTerm());
                        jEdit.setFindDownDirection(searchDialog.isFindDirectionDown());
                        jEdit.setMatchCase(searchDialog.isMatchCase());
                    }
                    SwingUtilities.invokeLater(jEdit::findNext);
            }, "Find Next (Menu)").start();
        }
    }

    /**
     * Actions for the Edit > Replace menu item.
     * 
     * @author Mark Bernard
     */
    public static class ReplaceAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Replace menu item.
         * 
         * @param jEdit 
         */
        public ReplaceAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Replace...");
            putValue(SHORT_DESCRIPTION, "Open the replace dialog.");
            putValue(SMALL_ICON, new IconGenerator("/icons/TextReplaceSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/TextReplace.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::replace, "Replace (menu)").start();
        }
    }

    /**
     * Actions for the Edit > Go To menu item.
     * 
     * @author Mark Bernard
     */
    public static class GoToAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Go To menu item.
         * 
         * @param jEdit 
         */
        public GoToAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Go To Line...");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/GoToSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/GoTo.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::goTo, "Go To (menu)").start();
        }
    }

    /**
     * Actions for the Edit > Select All menu item.
     * 
     * @author Mark Bernard
     */
    public static class SelectAllAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jEdit 
         */
        public SelectAllAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Select All");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/SelectAllSmall.png").loadIcon());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/SelectAll.png").loadIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::selectAll, "Select All").start();
        }
    }

    /**
     * Actions for the Edit > Uppercase menu item.
     * 
     * @author Mark Bernard
     */
    public static class UppercaseAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jEdit 
         */
        public UppercaseAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
            putValue(NAME, "Uppercase");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::uppercase, "Uppercase").start();
        }
    }

    /**
     * Actions for the Edit > Lowercase menu item.
     * 
     * @author Mark Bernard
     */
    public static class LowercaseAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jEdit 
         */
        public LowercaseAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Lowercase");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::lowercase, "Lowercase").start();
        }
    }

    /**
     * Actions for the Edit > Toggle Comment menu item.
     * 
     * @author Mark Bernard
     */
    public static class ToggleCommentAction extends RSyntaxTextAreaEditorKit.ToggleCommentAction {
        private static final long serialVersionUID = 1215020210624036643L;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jEdit 
         */
        public ToggleCommentAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(NAME, "Toggle Comment");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        }
    }
}
