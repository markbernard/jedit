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
import javax.swing.Action;
import javax.swing.KeyStroke;

import io.github.markbernard.jedit.IconGenerator;
import io.github.markbernard.jedit.JEdit;

/**
 * Takes care of all actions for the File menu. Menu actions will be internal classes of this class.
 * 
 * @author Mark Bernard
 */
public class FileAction extends AbstractAction {
    private static final long serialVersionUID = -7881302815437732429L;

    /**
     * Set up values appropriate to the File menu.
     */
    public FileAction() {
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(Action.NAME, "File");
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        /** provides menu controls but needs no action */
    }

    /**
     * Actions for the File > New menu item.
     * 
     * @author Mark Bernard
     */
    public static class NewAction extends AbstractAction {
        private static final long serialVersionUID = 8935736532927739892L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the New menu item.
         * 
         * @param jEdit Access to the application to perform actions. 
         */
        public NewAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.NAME, "New");
            putValue(Action.SHORT_DESCRIPTION, "Create a new blank document.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/NewFileSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/NewFile.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::newDocument, "New Document").start();
        }
    }

    /**
     * Actions for the File > Open menu item.
     * 
     * @author Mark Bernard
     */
    public static class OpenAction extends AbstractAction {
        private static final long serialVersionUID = 3954227951651230619L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Open menu item.
         * 
         * @param jEdit Access to the application to perform actions. 
         */
        public OpenAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.NAME, "Open...");
            putValue(Action.SHORT_DESCRIPTION, "Load a document from disk.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/OpenFileSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/OpenFile.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::load, "Open").start();
        }
    }

    /**
     * Actions for the File > Save menu item.
     * 
     * @author Mark Bernard
     */
    public static class SaveAction extends AbstractAction {
        private static final long serialVersionUID = 980383255637754695L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Save menu item.
         * 
         * @param jEdit 
         */
        public SaveAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.NAME, "Save");
            putValue(Action.SHORT_DESCRIPTION, "Save a document to disk.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/SaveSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/Save.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::save, "Save").start();
        }
    }

    /**
     * Actions for the File > Save As menu item.
     * 
     * @author Mark Bernard
     */
    public static class SaveAsAction extends AbstractAction {
        private static final long serialVersionUID = 8295016518814119408L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Save As menu item.
         * 
         * @param jEdit 
         */
        public SaveAsAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 5);
            putValue(Action.NAME, "Save As...");
            putValue(Action.SHORT_DESCRIPTION, "Save the document with a new name.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/SaveAsSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/SaveAs.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::saveAs, "Save As").start();
        }
    }

    /**
     * Actions for the File > Page Setup menu item.
     * 
     * @author Mark Bernard
     */
    public static class PageSetupAction extends AbstractAction {
        private static final long serialVersionUID = -6577539419280079060L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Page Setup menu item.
         * 
         * @param jEdit 
         */
        public PageSetupAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(Action.NAME, "Page Setup...");
            putValue(Action.SHORT_DESCRIPTION, "Edit printer page settings.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/PageSetupSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/PageSetup.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::pageSetup, "Page Setup").start();
        }
    }

    /**
     * Actions for the File > Print menu item.
     * 
     * @author Mark Bernard
     */
    public static class PrintAction extends AbstractAction {
        private static final long serialVersionUID = -6449830362022451492L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Print menu item.
         * 
         * @param jEdit 
         */
        public PrintAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.NAME, "Print...");
            putValue(Action.SHORT_DESCRIPTION, "Open the print dialog.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/PrintSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/Print.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::doPrint, "Print").start();
        }
    }

    /**
     * Actions for the File > Exit menu item.
     * 
     * @author Mark Bernard
     */
    public static class ExitAction extends AbstractAction {
        private static final long serialVersionUID = -3795345737189627905L;
        private final transient JEdit jEdit;

        /**
         * Set up values appropriate to the Exit menu item.
         * 
         * @param jEdit Access to the application to perform actions. 
         */
        public ExitAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(Action.NAME, "Exit");
            putValue(Action.SHORT_DESCRIPTION, "Exit JNotepad.");
            putValue(Action.SMALL_ICON, new IconGenerator("/icons/ExitSmall.png").loadIcon());
            putValue(Action.LARGE_ICON_KEY, new IconGenerator("/icons/Exit.png").loadIcon());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(jEdit::exit, "Exit").start();
        }
    }
    
    /**
     * Actions for the File > Recent Documents sub menu.
     * 
     * @author Mark Bernard
     */
    public static class FileRecentDocumentsAction extends AbstractAction {
        private static final long serialVersionUID = -8538598403541393623L;

        /**
         * Set up values appropriate to the Recent Documents sub menu.
         */
        public FileRecentDocumentsAction() {
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.NAME, "Recent Documents");
        }
        @Override
        public void actionPerformed(ActionEvent arg0) {
            /** provides menu controls but needs no action */
        }
        
        /**
         * Actions for the File > Recent Documents > selected document menu item.
         * 
         * @author Mark Bernard
         */
        public static class FileRecentDocumentsOpenDocumentAction extends AbstractAction {
            private static final long serialVersionUID = -8560848798824824961L;

            private final String filePath;
            private final transient JEdit jEdit;

            /**
             * Set up values appropriate to the selected document menu item.
             * 
             * @param item
             * @param filePath
             * @param jEdit
             */
            public FileRecentDocumentsOpenDocumentAction(int item, String filePath, JEdit jEdit) {
                this.filePath = filePath;
                this.jEdit = jEdit;
                if (item != -1) {
                    putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1 + item);
                    putValue(Action.NAME, (item + 1) + ". " + filePath);
                } else {
                    putValue(Action.NAME, filePath);
                }
                putValue(Action.SHORT_DESCRIPTION, "Open document at " + filePath + ".");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    jEdit.openRecentDocument(filePath);
                }, "Open " + filePath).start();
            }
        }
    }
}
