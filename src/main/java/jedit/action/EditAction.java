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
package jedit.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;

import jedit.IconGenerator;
import jedit.JNotepad;
import jedit.dialog.SearchDialog;

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
    }

    /**
     * Actions for the Edit > Undo menu item.
     * 
     * @author Mark Bernard
     */
    public static class UndoAction extends AbstractAction {
        private static final long serialVersionUID = 5936711805304319306L;
        
        private JNotepad jNotepad;
        
        /**
         * Set up values appropriate to the Undo menu item.
         * 
         * @param jNotepad 
         */
        public UndoAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Undo");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/UndoSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/Undo.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.undo();
                }
            }, "Undo").start();
        }
    }

    /**
     * Actions for the Edit > Redo menu item.
     * 
     * @author Mark Bernard
     */
    public static class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 6599265610327179684L;

        private JNotepad jNotepad;
        
        /**
         * Set up values appropriate to the Undo menu item.
         * 
         * @param jNotepad 
         */
        public RedoAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Redo");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/RedoSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/Redo.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.redo();
                }
            }, "Redo").start();
        }
    }

    /**
     * Actions for the Edit > Cut menu item.
     * 
     * @author Mark Bernard
     */
    public static class CutAction extends AbstractAction {
        private static final long serialVersionUID = 6520482354999730690L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Cut menu item.
         * 
         * @param jNotepad 
         */
        public CutAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_T);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Cut");
            putValue(SHORT_DESCRIPTION, "Remove the current selection from the document and put it in the clipboard.");
            putValue(SMALL_ICON, new IconGenerator("/icons/CutSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Cut.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.cut();
                }
            }, "Cut").start();
        }
    }

    /**
     * Actions for the Edit > Copy menu item.
     * 
     * @author Mark Bernard
     */
    public static class CopyAction extends AbstractAction {
        private static final long serialVersionUID = -2906245309033009826L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Copy menu item.
         * 
         * @param jNotepad 
         */
        public CopyAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Copy");
            putValue(SHORT_DESCRIPTION, "Copy the current selection from the document and put it in the clipboard.");
            putValue(SMALL_ICON, new IconGenerator("/icons/CopySmall.png").loadImage());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Copy.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.copy();
                }
            }, "Copy").start();
        }
    }

    /**
     * Actions for the Edit > Paste menu item.
     * 
     * @author Mark Bernard
     */
    public static class PasteAction extends AbstractAction {
        private static final long serialVersionUID = -555624621599811242L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Paste menu item.
         * 
         * @param jNotepad 
         */
        public PasteAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Paste");
            putValue(SHORT_DESCRIPTION, "Paste the clipboard contents into the current cursor location or selection.");
            putValue(SMALL_ICON, new IconGenerator("/icons/PasteSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Paste.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.paste();
                }
            }, "Paste").start();
        }
    }

    /**
     * Actions for the Edit > Find menu item.
     * 
     * @author Mark Bernard
     */
    public static class FindAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Find menu item.
         * 
         * @param jNotepad 
         */
        public FindAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Find...");
            putValue(SHORT_DESCRIPTION, "Open the find dialog.");
            putValue(SMALL_ICON, new IconGenerator("/icons/FindSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/Find.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.find();
                }
            }, "Find").start();
        }
    }

    /**
     * Actions for the Edit > Find Next menu item.
     * 
     * @author Mark Bernard
     */
    public static class FindNextAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;
        private SearchDialog searchDialog;

        /**
         * Set up values appropriate to the Find Next menu item.
         * 
         * @param jNotepad 
         * @param searchDialog 
         */
        public FindNextAction(JNotepad jNotepad, SearchDialog searchDialog) {
            this.jNotepad = jNotepad;
            this.searchDialog = searchDialog;
            if (searchDialog == null) {
                putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            } else {
                putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            }
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Find Next");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/FindNextSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/FindNext.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (searchDialog != null) {
                        jNotepad.setFindTerm(searchDialog.getFindTerm());
                        jNotepad.setFindDownDirection(searchDialog.isFindDirectionDown());
                        jNotepad.setMatchCase(searchDialog.isMatchCase());
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            jNotepad.findNext();
                        }
                    });
                }
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
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Replace menu item.
         * 
         * @param jNotepad 
         */
        public ReplaceAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Replace...");
            putValue(SHORT_DESCRIPTION, "Open the replace dialog.");
            putValue(SMALL_ICON, new IconGenerator("/icons/TextReplaceSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, new IconGenerator("/icons/TextReplace.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.replace();
                }
            }, "Replace (menu)").start();
        }
    }

    /**
     * Actions for the Edit > Go To menu item.
     * 
     * @author Mark Bernard
     */
    public static class GoToAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Go To menu item.
         * 
         * @param jNotepad 
         */
        public GoToAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Go To Line...");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/GoToSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/GoTo.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.goTo();
                }
            }, "Go To (menu)").start();
        }
    }

    /**
     * Actions for the Edit > Select All menu item.
     * 
     * @author Mark Bernard
     */
    public static class SelectAllAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jNotepad 
         */
        public SelectAllAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Select All");
            putValue(SMALL_ICON, 
                    new IconGenerator("/icons/SelectAllSmall.png").loadImage());
            putValue(LARGE_ICON_KEY, 
                    new IconGenerator("/icons/SelectAll.png").loadImage());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.selectAll();
                }
            }, "Select All").start();
        }
    }

    /**
     * Actions for the Edit > Uppercase menu item.
     * 
     * @author Mark Bernard
     */
    public static class UppercaseAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jNotepad 
         */
        public UppercaseAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
            putValue(NAME, "Uppercase");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.uppercase();
                }
            }, "Uppercase").start();
        }
    }

    /**
     * Actions for the Edit > Lowercase menu item.
     * 
     * @author Mark Bernard
     */
    public static class LowercaseAction extends AbstractAction {
        private static final long serialVersionUID = 1215020210624036643L;
        private JNotepad jNotepad;

        /**
         * Set up values appropriate to the Select All menu item.
         * 
         * @param jNotepad 
         */
        public LowercaseAction(JNotepad jNotepad) {
            this.jNotepad = jNotepad;
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
            putValue(NAME, "Lowercase");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jNotepad.lowercase();
                }
            }, "Lowercase").start();
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
         * @param jNotepad 
         */
        public ToggleCommentAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(NAME, "Toggle Comment");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        }
    }
}
