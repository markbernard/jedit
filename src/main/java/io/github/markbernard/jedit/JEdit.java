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
package io.github.markbernard.jedit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jorphan.gui.MenuScroller;

import io.github.markbernard.jedit.action.DocumentAction;
import io.github.markbernard.jedit.action.EditAction;
import io.github.markbernard.jedit.action.FileAction;
import io.github.markbernard.jedit.action.HelpAction;
import io.github.markbernard.jedit.dialog.AboutDialog;
import io.github.markbernard.jedit.dialog.SearchDialog;

/**
 * 
 */
public class JEdit implements WindowListener, KeyListener {
    private static final String APPLICATION_TITLE = "JEdit";
    private static final Object TAB_LOCK = new Object();

    private boolean insertPressed = false;
    private JFrame parent;
    private TextDocument currentDocument;
    private JMenu fileRecentDocuments;
    private JMenuItem editUndoMenuItem;
    private JMenuItem editRedoMenuItem;
    private JPanel editorPanel;
    private JTabbedPane tabbedPane;
    private JLabel positionLabel;
    private JLabel capsLockLabel;
    private JLabel insertLabel;
    private JLabel readOnlyLabel;
    private JLabel encodingLabel;
    private JLabel languageLabel;
    private JLabel filePathLabel;
    private JCheckBoxMenuItem formatWordWrap;
    private String findTerm;
    private String replaceTerm;
    private boolean findDownDirection;
    private boolean matchCase;
    private boolean insertMode = true;
    private boolean readOnly = false;
    private int newDocumentCounter;
    
    /**
     * @param parent 
     */
    public JEdit(JFrame parent) {
        this.parent = parent;
        parent.addWindowListener(this);
        ApplicationPreferences.loadPrefs(parent);
        editorPanel = new JPanel(new BorderLayout());
        parent.add(editorPanel, BorderLayout.CENTER);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setOpaque(false);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                synchronized (TAB_LOCK) {
                    if (tabbedPane.getTabCount() > 0) {
                        currentDocument = (TextDocument) tabbedPane.getSelectedComponent();
                        setTitle();
                        currentDocument.shown();
                    }
                }
            }
        });
        editorPanel.add(tabbedPane, BorderLayout.CENTER);
        newDocumentCounter = 1;
        createMenus();
        createToolBar();
        createStatusBar();
        newDocument();
    }
    
    private void createToolBar() {
        JToolBar toolbar = new JToolBar();
        editorPanel.add(toolbar, BorderLayout.NORTH);
        toolbar.add(new FileAction.NewAction(this));
        toolbar.add(new FileAction.OpenAction(this));
        toolbar.add(new FileAction.SaveAction(this));

        toolbar.addSeparator();
        toolbar.add(new FileAction.ExitAction(this));
        
        toolbar.addSeparator();
        toolbar.add(new EditAction.CutAction(this));
        toolbar.add(new EditAction.CopyAction(this));
        toolbar.add(new EditAction.PasteAction(this));

        toolbar.addSeparator();
        toolbar.add(new EditAction.FindAction(this));
        toolbar.add(new EditAction.ReplaceAction(this));

        toolbar.addSeparator();
        toolbar.add(new HelpAction.ViewHelpAction(this));
    }

    private void createMenus() {
        JMenuBar bar = new JMenuBar();
        parent.setJMenuBar(bar);
        
        JMenu fileMenu = new JMenu(new FileAction());
        bar.add(fileMenu);
        JMenuItem fileNewMenuItem = new JMenuItem(new FileAction.NewAction(this));
        fileMenu.add(fileNewMenuItem);
        JMenuItem fileOpenMenuItem = new JMenuItem(new FileAction.OpenAction(this));
        fileMenu.add(fileOpenMenuItem);
        JMenuItem fileSaveMenuItem = new JMenuItem(new FileAction.SaveAction(this));
        fileMenu.add(fileSaveMenuItem);
        JMenuItem fileSaveAsMenuItem = new JMenuItem(new FileAction.SaveAsAction(this));
        fileMenu.add(fileSaveAsMenuItem);
        fileMenu.addSeparator();
        JMenuItem filePageSetupMenuItem = new JMenuItem(new FileAction.PageSetupAction(this));
        fileMenu.add(filePageSetupMenuItem);
        JMenuItem filePrintMenuItem = new JMenuItem(new FileAction.PrintAction(this));
        fileMenu.add(filePrintMenuItem);
        fileMenu.addSeparator();
        JMenuItem fileExitMenuItem = new JMenuItem(new FileAction.ExitAction(this));
        fileMenu.add(fileExitMenuItem);
        fileRecentDocuments = new JMenu(new FileAction.FileRecentDocumentsAction());
        fileMenu.add(fileRecentDocuments);
        updateRecentDocuments();

        JMenu editMenu = new JMenu(new EditAction());
        bar.add(editMenu);
        editUndoMenuItem = new JMenuItem(new EditAction.UndoAction(this));
        editMenu.add(editUndoMenuItem);
        editRedoMenuItem= new JMenuItem(new EditAction.RedoAction(this));
        editMenu.add(editRedoMenuItem);
        editMenu.addSeparator();
        JMenuItem editCutMenuItem = new JMenuItem(new EditAction.CutAction(this));
        editMenu.add(editCutMenuItem);
        JMenuItem editCopyMenuItem = new JMenuItem(new EditAction.CopyAction(this));
        editMenu.add(editCopyMenuItem);
        JMenuItem editPasteMenuItem = new JMenuItem(new EditAction.PasteAction(this));
        editMenu.add(editPasteMenuItem);
        editMenu.addSeparator();
        JMenuItem editFindMenuItem = new JMenuItem(new EditAction.FindAction(this));
        editMenu.add(editFindMenuItem);
        JMenuItem editFindNextMenuItem = new JMenuItem(new EditAction.FindNextAction(this, null));
        editMenu.add(editFindNextMenuItem);
        JMenuItem editReplaceMenuItem = new JMenuItem(new EditAction.ReplaceAction(this));
        editMenu.add(editReplaceMenuItem);
        editMenu.addSeparator();
        JMenuItem editGoToMenuItem = new JMenuItem(new EditAction.GoToAction(this));
        editMenu.add(editGoToMenuItem);
        JMenuItem editSelectAllMenuItem = new JMenuItem(new EditAction.SelectAllAction(this));
        editMenu.add(editSelectAllMenuItem);
        editMenu.addSeparator();
        JMenuItem uppercaseMenuItem = new JMenuItem(new EditAction.UppercaseAction(this));
        editMenu.add(uppercaseMenuItem);
        JMenuItem lowercaseMenuItem = new JMenuItem(new EditAction.LowercaseAction(this));
        editMenu.add(lowercaseMenuItem);
        JMenuItem toggleCommentMenuItem = new JMenuItem(new EditAction.ToggleCommentAction());
        editMenu.add(toggleCommentMenuItem);

        JMenu documentMenu = new JMenu(new DocumentAction());
        bar.add(documentMenu);
        formatWordWrap = new JCheckBoxMenuItem(DocumentAction.WordWrapAction.getInstance(this));
        documentMenu.add(formatWordWrap);
        documentMenu.addSeparator();
        JMenu documentLanguageMenu = new JMenu(new DocumentAction.LanguageAction());
        documentMenu.add(documentLanguageMenu);
        new MenuScroller(documentLanguageMenu, 30);
        List<LanguageInfo> languageList = new ArrayList<>(DocumentFormatType.FILE_TYPE_MAP.values());
        Collections.sort(languageList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        for (LanguageInfo languageInfo : languageList) {
            JMenuItem documentLanguageChoiceMenuItem = new JMenuItem(new DocumentAction.LanguageAction.LanguageChoiceAction(this, languageInfo));
            documentLanguageMenu.add(documentLanguageChoiceMenuItem);
        }
        JMenu encodingMenu = new JMenu(new DocumentAction.EncodingAction());
        documentMenu.add(encodingMenu);
        for (Charset charset : EncodingType.supportedEncodings) {
            JMenuItem charsetMenuItem = new JMenuItem(new DocumentAction.EncodingAction.CharsetAction(this, charset));
            encodingMenu.add(charsetMenuItem);
        }

        JMenu helpMenu = new JMenu(new HelpAction());
        bar.add(helpMenu);
        JMenuItem helpAboutMenuItem = new JMenuItem(new HelpAction.AboutAction(this));
        helpMenu.add(helpAboutMenuItem);
    }
    
    private void updateRecentDocuments() {
        fileRecentDocuments.removeAll();
        List<String> recentDocuments = ApplicationPreferences.getRecentDocuments();
        if (recentDocuments.size() > 0) {
            int item = 0;
            for (String recentDocumentPath : recentDocuments) {
                fileRecentDocuments.add(new JMenuItem(new FileAction.FileRecentDocumentsAction.FileRecentDocumentsOpenDocumentAction(item, recentDocumentPath, this)));
                if (item > 7) {
                    item = -1;
                } else if (item != -1) {
                    item++;
                }
            }
            fileRecentDocuments.validate();
        } else {
            JMenuItem emptyItem = new JMenuItem("(" + Translations.getString("JEdit.updateRecentDocuments.empty") + ")");
            fileRecentDocuments.add(emptyItem);
            emptyItem.setEnabled(false);
        }
    }
    
    private void createStatusBar() {
        Font statusBarFont = new Font("Consolas", Font.PLAIN, 14);
        JPanel mainStatusBarPanel = new JPanel(new BorderLayout(0, 0));
        setStausBorder(mainStatusBarPanel);
        JPanel statusBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mainStatusBarPanel.add(statusBarPanel, BorderLayout.WEST);
        JPanel positionPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(positionPanel);
        setStausBorder(positionPanel);
        positionLabel = new JLabel("");
        positionPanel.add(positionLabel, BorderLayout.WEST);
        positionLabel.setFont(statusBarFont);
        
        JPanel capsLockPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(capsLockPanel);
        setStausBorder(capsLockPanel);
        capsLockLabel = new JLabel("");
        capsLockPanel.add(capsLockLabel, BorderLayout.WEST);
        capsLockLabel.setFont(statusBarFont);
        
        JPanel insertPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(insertPanel);
        setStausBorder(insertPanel);
        insertLabel = new JLabel("");
        insertPanel.add(insertLabel, BorderLayout.WEST);
        insertLabel.setFont(statusBarFont);
        
        JPanel readOnlyPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(readOnlyPanel);
        setStausBorder(readOnlyPanel);
        readOnlyLabel = new JLabel("");
        readOnlyPanel.add(readOnlyLabel, BorderLayout.WEST);
        readOnlyLabel.setFont(statusBarFont);
        
        JPanel encodingPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(encodingPanel);
        setStausBorder(encodingPanel);
        encodingLabel = new JLabel("");
        encodingPanel.add(encodingLabel, BorderLayout.WEST);
        encodingLabel.setFont(statusBarFont);

        JPanel languagePanel = new JPanel(new BorderLayout());
        statusBarPanel.add(languagePanel);
        setStausBorder(languagePanel);
        languageLabel = new JLabel("");
        languagePanel.add(languageLabel, BorderLayout.WEST);
        languageLabel.setFont(statusBarFont);

        JPanel filePathPanel = new JPanel(new BorderLayout());
        mainStatusBarPanel.add(filePathPanel, BorderLayout.CENTER);
        setStausBorder(filePathPanel);
        filePathLabel = new JLabel("");
        filePathPanel.add(filePathLabel, BorderLayout.WEST);
        filePathLabel.setFont(statusBarFont);

        //        if (ApplicationPreferences.isStatusBar()) {
            editorPanel.add(mainStatusBarPanel, BorderLayout.SOUTH);
//        }
    }

    private void setStausBorder(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
                BorderFactory.createEmptyBorder(1, 3, 1, 3)));
    }

    /**
     * @param position
     */
    public void updateStatusBar(String position) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (currentDocument != null) {
                    editUndoMenuItem.setEnabled(currentDocument.canUndo());
                    editRedoMenuItem.setEnabled(currentDocument.canRedo());
                    insertMode = currentDocument.isInsertMode();
                    updateCursorLocation(position);
                    updateCapsLock();
                    updateInsertMode();
                    updateReadOnly();
                    updateEncoding();
                    updateLanguage();
                    updateFilePath();
                }
            }
        });
    }
    
    private void updateCursorLocation(String position) {
        positionLabel.setText(position);
    }
    
    private void updateCapsLock() {
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            capsLockLabel.setForeground(Color.BLACK);
            capsLockLabel.setText(Translations.getString("JEdit.updateCapsLock.capson"));
        } else {
            capsLockLabel.setForeground(Color.LIGHT_GRAY);
            capsLockLabel.setText(Translations.getString("JEdit.updateCapsLock.capsoff"));
        }
    }
    
    private void updateInsertMode() {
        currentDocument.setInsertMode(insertMode);
        if (insertMode) {
            insertLabel.setForeground(Color.LIGHT_GRAY);
            insertLabel.setText(Translations.getString("JEdit.updateInsertMode.ins"));
        } else {
            insertLabel.setForeground(Color.BLACK);
            insertLabel.setText(Translations.getString("JEdit.updateInsertMode.ovr"));
        }
    }

    private void updateReadOnly() {
        if (readOnly) {
            readOnlyLabel.setForeground(Color.BLACK);
            readOnlyLabel.setText(Translations.getString("JEdit.updateReadOnly.readonly"));
        } else {
            readOnlyLabel.setForeground(Color.LIGHT_GRAY);
            readOnlyLabel.setText(Translations.getString("JEdit.updateReadOnly.readwrite"));
        }
    }
    
    private void updateEncoding() {
        if (currentDocument != null) {
            encodingLabel.setText(currentDocument.getCharset().displayName());
        }
    }
    
    private void updateLanguage() {
        if (currentDocument != null && currentDocument.getLanguageInfo() != null) {
            languageLabel.setText(currentDocument.getLanguageInfo().getName());
        }
    }

    private void updateFilePath() {
        if (currentDocument != null) {
            String path = currentDocument.getFilePath();
            if (path.length() > 0) {
                path += "/";
            }
            filePathLabel.setText(path + currentDocument.getFileName());
            filePathLabel.setToolTipText(path + currentDocument.getFileName());
        }
    }

    /**
     * @param findTerm the findTerm to set
     */
    public void setFindTerm(String findTerm) {
        this.findTerm = findTerm;
    }

    /**
     * @param replaceTerm the replaceTerm to set
     */
    public void setReplaceTerm(String replaceTerm) {
        this.replaceTerm = replaceTerm;
    }

    /**
     * @param matchCase the matchCase to set
     */
    public void setMatchCase(boolean matchCase) {
        this.matchCase = matchCase;
    }

    /**
     * @return the insertMode
     */
    public boolean isInsertMode() {
        return insertMode;
    }

    /**
     * @param insertMode the insertMode to set
     */
    public void setInsertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }

    /**
     * @param findDownDirection the findDownDirection to set
     */
    public void setFindDownDirection(boolean findDownDirection) {
        this.findDownDirection = findDownDirection;
    }

    /**
     * Undo the last text input
     */
    public void undo() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.undo();
            }
        });
    }
    
    /**
     * Redo the last text input
     */
    public void redo() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.redo();
            }
        });
    }
    
    /**
     * Cut the selected text and place it in the system clipboard
     */
    public void cut() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.cut();
            }
        });
    }
    
    /**
     * Copy the selected text and place it in the system clipboard
     */
    public void copy() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.copy();
            }
        });
    }
    
    /**
     * Take contents of the system clipboard, if it is text, and place it at the cursor.
     */
    public void paste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor[] flavors = clipboard.getAvailableDataFlavors();
        for (DataFlavor flavor : flavors) {
            if (flavor.isFlavorTextType() && flavor.isMimeTypeSerializedObject()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        currentDocument.performPaste(flavor, clipboard);
                    }
                });
            }
        }
    }
    
    /**
     * Display a dialog for the user to search the text for something
     */
    public void find() {
        JEdit self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SearchDialog dialog = new SearchDialog(parent, self, false);
                dialog.setVisible(true);
            }
        });
    }
    
    /**
     * Due to nesting issues this method does not use SwingUtilities.invokeLater.
     * Calling methods should wrap any calls to this method in SwingUtilities.invokeLater to make sure it runs on the EDT
     */
    public void findNext() {
        if (findTerm != null && !findTerm.isEmpty()) {
            currentDocument.findNext(findTerm, matchCase, findDownDirection);
        } else {
            find();
        }
    }

    /**
     * 
     */
    public void replace() {
        JEdit self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SearchDialog dialog = new SearchDialog(parent, self, true);
                dialog.setVisible(true);
            }
        });
    }
    
    /**
     * 
     */
    public void performReplace() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.performReplace(findTerm, replaceTerm, matchCase);
            }
        });
    }
    
    /**
     * 
     */
    public void replaceAll() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.replaceAll(findTerm, replaceTerm, matchCase);
            }
        });
    }
    
    /**
     * Place the cursor on the beginning of the line number select by the user.
     */
    public void goTo() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.goTo(parent);
            }
        });
    }

    /**
     * Selects all text in the JTextArea.
     */
    public void selectAll() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.selectAll();
            }
        });
    }

    /**
     * Create a new document while checking to see if the current document is saved.
     */
    public void newDocument() {
        TextDocument doc = new TextDocument(this, newDocumentCounter++);
        currentDocument = doc;
        addDocumentToTabs(doc);
    }
    
    private void addDocumentToTabs(TextDocument doc) {
        synchronized (TAB_LOCK) {
            tabbedPane.addTab(null, doc);
            TabComponent tabComponent = new TabComponent(this, doc.getTitle());
            tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(doc), tabComponent);
            tabbedPane.setSelectedComponent(doc);
            SwingUtilities.invokeLater(() -> doc.shown());
        }
    }

    /**
     * @param filePath
     */
    public void openRecentDocument(String filePath) {
        boolean found = false;
        if (tabbedPane.getTabCount() > 0) {
            for (int i=0;i<tabbedPane.getTabCount();i++) {
                TextDocument doc = (TextDocument) tabbedPane.getComponentAt(i);
                if (filePath.equals(doc.getFullFilePath())) {
                    tabbedPane.setSelectedComponent(doc);
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            loadFile(new File(filePath));
        }
    }

    /**
     * Show a file dialog and load the selected file.
     */
    public void load() {
        String filePath = ApplicationPreferences.getCurrentFilePath();
        JFileChooser fileChooser = new JFileChooser(filePath);
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(editorPanel) == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            for (File selectedFile : selectedFiles) {
                loadFile(selectedFile);
            }
        }
    }
    
    private void loadFile(File selectedFile) {
        boolean removeInitialNewDoc = false;
        if (tabbedPane.getTabCount() == 1 && Pattern.matches("new \\d+", currentDocument.getTitle()) & !currentDocument.isDirty()) {
            removeInitialNewDoc = true;
        }
        TextDocument doc = new TextDocument(this, selectedFile);
        addDocumentToTabs(doc);
        ApplicationPreferences.setCurrentFilePath(doc.getFilePath());
        ApplicationPreferences.addDocument(doc.getFullFilePath());
        updateRecentDocuments();
        if (removeInitialNewDoc) {
            tabbedPane.removeTabAt(0);
        }
    }
    
    /**
     * Save an existing document. If there is no existing document then call saveAs.
     * If a call to saveAs is made and the user cancels during the file save dialog 
     * then false will be returned.
     * 
     * @return true if the save was not interrupted, false otherwise
     */
    public boolean save() {
        boolean saved = currentDocument.save();

        if (saved) {
            ApplicationPreferences.addDocument(currentDocument.getFullFilePath());
            updateRecentDocuments();
            setTitle();
        }
        
        return saved;
    }
    
    /**
     * Display a dialog to the user asking for a location and name of the file
     * to save. If the user cancels the dialog then return false.
     * 
     * @return false if the user cancels the file save dialog, true otherwise
     */
    public boolean saveAs() {
        boolean saved = currentDocument.saveAs();
        
        if (saved) {
            ApplicationPreferences.addDocument(currentDocument.getFullFilePath());
            updateRecentDocuments();
            setTitle();
        }
        
        return saved;
    }

    /**
     * @param tabComponent
     */
    public void closeTab(TabComponent tabComponent) {
        int index = tabbedPane.indexOfTabComponent(tabComponent);
        if (index > -1) {
            TextDocument doc = (TextDocument) tabbedPane.getComponentAt(index);
            if (doc.requestClose(tabbedPane.getTabCount() == 1)) {
                tabbedPane.removeTabAt(index);
                if (tabbedPane.getTabCount() == 0) {
                    newDocument();
                }
            }
        }
    }
    
    /**
     * @return the parentFrame
     */
    public JFrame getParentFrame() {
        return parent;
    }

    /**
     * Update the main title with the currently selected tab.
     */
    public void setTitle() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                synchronized (TAB_LOCK) {
                    if (currentDocument != null) {
                        parent.setTitle(currentDocument.getTitle() + " - " + APPLICATION_TITLE);
                        int index = tabbedPane.indexOfComponent(currentDocument);
                        if (index > -1) {
                            TabComponent tabComponent = (TabComponent) tabbedPane.getTabComponentAt(index);
                            tabComponent.setName(currentDocument.getTitle());
                        }
                    }
                }
            }
        });
    }

    /**
     * Toggle word wrapping
     */
    public void wordWrap() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentDocument.wordWrap(formatWordWrap);
            }
        });
    }

    /**
     * @param languageInfo
     */
    public void setLanguage(LanguageInfo languageInfo) {
        currentDocument.setLanguageInfo(languageInfo);
    }
    
    /**
     * @param charset
     */
    public void setCharset(Charset charset) {
        currentDocument.setCharset(charset);
    }

    /**
     * 
     */
    public void uppercase() {
        currentDocument.uppercase();
    }
    
    /**
     * 
     */
    public void lowercase() {
        currentDocument.lowercase();
    }

    /**
     * 
     */
    public void about() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AboutDialog aboutDialog = new AboutDialog(parent);
                aboutDialog.setVisible(true);
            }
        });
    }
    /**
     * Exit while saving prefrenecs and checking if save is required.
     */
    public void exit() {
        boolean doExit = true;
        for (int i=tabbedPane.getTabCount() - 1;i>=0;i--) {
            TextDocument doc = (TextDocument) tabbedPane.getComponentAt(i);
            if (!doc.requestClose(i == 0)) {
                doExit = false;
            }
        }
        
        if (doExit) {
            ApplicationPreferences.savePrefs(parent);
            System.exit(0);
        }
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
        currentDocument.shown();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
            updateCapsLock();
        } else if (e.getKeyCode() == KeyEvent.VK_INSERT && !insertPressed) {
            insertPressed = true;
            insertMode = !insertMode;
            updateInsertMode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            insertPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * @param args
     */
    public static void main(String... args) {
        String[] fontFiles = {
                "/fonts/Hack-Bold.ttf",
                "/fonts/Hack-BoldItalic.ttf",
                "/fonts/Hack-Italic.ttf",
                "/fonts/Hack-Regular.ttf"
        };

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (String path : fontFiles) {
            try {
                // Use Font.TRUETYPE_FONT for both .ttf and .otf files
                Font font = Font.createFont(Font.TRUETYPE_FONT, JEdit.class.getResourceAsStream(path));
                ge.registerFont(font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        ApplicationPreferences.setCurrentFont(new Font("Hack", Font.PLAIN, 16));
        try {
            // Make the application look native.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // System look and feel is always present.
        }
        JFrame f = new JFrame("JEdit");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        new JEdit(f);
        SwingUtilities.invokeLater(() -> f.setVisible(true));
    }
}
