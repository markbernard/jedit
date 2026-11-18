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
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Utilities;

import org.apache.tika.detect.DefaultEncodingDetector;
import org.apache.tika.detect.EncodingDetector;
import org.apache.tika.detect.EncodingResult;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RUndoManager;
import org.fife.ui.rtextarea.TextMode;

import io.github.markbernard.jedit.dialog.EncodingDialog;
import io.github.markbernard.jedit.dialog.GoToDialog;

/**
 * @author Mark Bernard
 */
public class TextDocument extends JPanel implements DocumentListener, KeyListener {
    /** */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final long serialVersionUID = -6937922244390572212L;
    private static final String[] NEW_LINE_CHAR = {"\n", "\r\n"};
//    private static final String[] REGEX_NEW_LINE_CHAR = {"\\n", "\\r\\n"};

    
    private JNotepad jNotepad;
    private RSyntaxTextArea syntaxTextArea;
    private RTextScrollPane textScrollPane;
    private LanguageInfo languageInfo;
    private int newLineTypeUsed = 0;
//    private int newLineTypeNotUsed = 1;

    private String newFileName = "";
    private String fileName;
    private String filePath = "";
    private boolean dirty;
    private boolean readOnly;
    private RUndoManager undoManager;
    private Charset charset;
    private Point caratPosition;

    /**
     * @param jNotepad 
     * @param documentNumber Number to place beside new in the new file name.
     */
    public TextDocument(JNotepad jNotepad, int documentNumber) {
        this.jNotepad = jNotepad;
        newFileName = "new " + documentNumber;
        fileName = newFileName;
        charset = StandardCharsets.UTF_8;
        createGui();
    }

    /**
     * @param jNotepad 
     * @param file Full path to the file
     */
    public TextDocument(JNotepad jNotepad, File file) {
        this.jNotepad = jNotepad;
        parseFileName(file.getAbsolutePath().replace("\\", "/"));
        createGui();
        loadFile(file);
    }

    
    private void createGui() {
        dirty = false;
        setLayout(new BorderLayout());
        syntaxTextArea = new RSyntaxTextArea(20, 60);
        syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        syntaxTextArea.setCodeFoldingEnabled(true);
        textScrollPane = new RTextScrollPane(syntaxTextArea);
        add(textScrollPane, BorderLayout.CENTER);
        syntaxTextArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateStatusBar(e.getDot());
            }
        });
        syntaxTextArea.addKeyListener(jNotepad);
        syntaxTextArea.addKeyListener(this);
        updateStatusBar(syntaxTextArea.getCaretPosition());
        syntaxTextArea.addKeyListener(jNotepad);
        syntaxTextArea.addKeyListener(this);
        undoManager = syntaxTextArea.getUndoManager();
        syntaxTextArea.setFont(ApplicationPreferences.getCurrentFont());
        removeKeyStrokes(syntaxTextArea);
        syntaxTextArea.getDocument().addDocumentListener(this);
        updateStatusBar(syntaxTextArea.getCaretPosition());
    }
    
    /**
     * @return The title for this document.
     */
    public String getTitle() {
        return (dirty ? "*" : "") + fileName;
    }

    /**
     * @return The text.
     */
//    public JTextPane getTextArea() {
//        return textPane;
//    }
    
    /**
     * Cut the selected text and place it in the system clipboard
     */
    public void cut() {
        int start = syntaxTextArea.getSelectionStart();
        syntaxTextArea.cut();
        syntaxTextArea.setSelectionStart(start);
        syntaxTextArea.setSelectionEnd(start);
    }

    /**
     * Copy the selected text and place it in the system clipboard
     */
    public void copy() {
        syntaxTextArea.copy();
    }

    /**
     * @param flavor
     * @param clipboard
     */
    public void performPaste(DataFlavor flavor, Clipboard clipboard) {
        try {
            String data = (String)clipboard.getData(flavor);
            int start = syntaxTextArea.getSelectionStart();
            int end = syntaxTextArea.getSelectionEnd();
            int length = end - start;
            Document doc = syntaxTextArea.getDocument();
            try {
                if (length > 0) {
                    doc.remove(start, length);
                }
                doc.insertString(start, data, null);
                int location = start + data.length();
                syntaxTextArea.setSelectionStart(location);
                syntaxTextArea.setSelectionEnd(location);
            } catch (BadLocationException e) {
                //looks like there is nothing to remove
                //if a mistake occurs we can still try standard paste
                syntaxTextArea.paste();
            }
        } catch (UnsupportedFlavorException e) {
            // generally this should not happen since we checked before hand if the flavor passed in was available.
            //if a mistake occurs we can still try standard paste
            syntaxTextArea.paste();
        } catch (IOException e) {
            //if a mistake occurs we can still try standard paste
            syntaxTextArea.paste();
        }
    }

    /**
     * @param findTerm
     * @param matchCase
     * @param findDownDirection
     */
    public void findNext(String findTerm, boolean matchCase, boolean findDownDirection) {
        String localFindTerm = findTerm;
        if (!matchCase) {
            localFindTerm = localFindTerm.toLowerCase();
        }
        if (findDownDirection) {
            int findStart = syntaxTextArea.getSelectionEnd();
            String text = syntaxTextArea.getText();
            if (!matchCase) {
                text = text.toLowerCase();
            }
            int index = text.indexOf(localFindTerm, findStart);
            if (index > -1) {
                syntaxTextArea.setSelectionStart(index);
                syntaxTextArea.setSelectionEnd(index + findTerm.length());
            }
        } else {
            int findStart = syntaxTextArea.getSelectionStart();
            String text = syntaxTextArea.getText().substring(0, findStart);
            if (!matchCase) {
                text = text.toLowerCase();
            }
            int index = text.lastIndexOf(localFindTerm);
            if (index > -1) {
                syntaxTextArea.setSelectionStart(index);
                syntaxTextArea.setSelectionEnd(index + findTerm.length());
            }
        }
    }

    /**
     * @param findTerm
     * @param replaceTerm
     * @param matchCase
     */
    public void performReplace(String findTerm, String replaceTerm, boolean matchCase) {
        if (findTerm != null && replaceTerm != null && !findTerm.isEmpty() && 
                syntaxTextArea.getSelectionStart() != syntaxTextArea.getSelectionEnd()) {
            String selectedText = syntaxTextArea.getSelectedText();
            if ((matchCase && findTerm.equals(selectedText)) || 
                    (!matchCase && findTerm.equalsIgnoreCase(selectedText))) {
                syntaxTextArea.replaceSelection(replaceTerm);
                syntaxTextArea.setSelectionStart(syntaxTextArea.getSelectionEnd());
                jNotepad.findNext();
            }
        }
    }

    /**
     * @param findTerm
     * @param replaceTerm
     * @param matchCase
     */
    public void replaceAll(String findTerm, String replaceTerm, boolean matchCase) {
        if (findTerm != null && replaceTerm != null && !findTerm.isEmpty()) {
            syntaxTextArea.setCaretPosition(0);
            findNext(findTerm, matchCase, true);
            while (findTerm.equals(syntaxTextArea.getSelectedText())) {
                syntaxTextArea.replaceSelection(replaceTerm);
                syntaxTextArea.setSelectionStart(syntaxTextArea.getSelectionEnd());
                findNext(findTerm, matchCase, true);
            }
        }
    }

    /**
     * Place the cursor on the beginning of the line number select by the user.
     * 
     * @param parentFrame 
     */
    public void goTo(JFrame parentFrame) {
        GoToDialog goToDialog = new GoToDialog(parentFrame, jNotepad);
        if (goToDialog.showDialog()) {
            int lineNumber = goToDialog.getLineNumber();
            
            if (lineNumber == 1) {
                syntaxTextArea.setCaretPosition(0);
            } else if (lineNumber >= 0) {
                String[] lines = syntaxTextArea.getText().split("\\n|\\r\\n");
                int pos = 0;
                if (lineNumber <= lines.length) {
                    for (int i=1;i<lineNumber;i++) {
                        pos += lines[i - 1].length();
                        pos++;
                    }
                    syntaxTextArea.setCaretPosition(pos);
                }
            }
        }
        goToDialog.dispose();
    }

    /**
     * Selects all text in the JTextArea.
     */
    public void selectAll() {
        syntaxTextArea.setSelectionStart(0);        
        syntaxTextArea.setSelectionEnd(syntaxTextArea.getText().length());
    }

    /**
     * Toggle word wrapping
     * 
     * @param formatWordWrap 
     * @param toolbar 
     */
    public void wordWrap(JCheckBoxMenuItem formatWordWrap) {
        formatWordWrap.setSelected(ApplicationPreferences.isWordWrap());
        formatWordWrap.repaint();
        syntaxTextArea.setLineWrap(ApplicationPreferences.isWordWrap());
        syntaxTextArea.revalidate();
    }

    /**
     * @param selectedFont
     */
    public void setSelectedFont(Font selectedFont) {
        syntaxTextArea.setFont(selectedFont);
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return The full path to the file.
     */
    public String getFullFilePath() {
        return filePath.replace("/", FILE_SEPARATOR) + FILE_SEPARATOR + fileName;
    }

    /**
     * @return the dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @param dirty the dirty to set
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
        updateStatusBar(syntaxTextArea.getCaretPosition());
    }

    /**
     * Operations to update the GUI when a document tab is selected.
     */
    public void shown() {
        syntaxTextArea.requestFocusInWindow();
        updateStatusBar(syntaxTextArea.getCaretPosition());
        if (!fileName.equals(newFileName)) {
            ApplicationPreferences.setCurrentFileName(fileName);
            ApplicationPreferences.setCurrentFilePath(filePath);
        }
    }
    
    private void updateStatusBar(int position) {
        try {
            //solution for row and column from http://java-sl.com/tip_row_column.html
            caratPosition = calculatePosition(position);
            jNotepad.updateStatusBar(String.format("Ln %d, Col %d", caratPosition.y, caratPosition.x));
        } catch (Exception e) {
            //not critical if the position in the
            //status bar does not get updated.
            e.printStackTrace();
        }
    }
    
    private Point calculatePosition(int position) throws BadLocationException {
        //TODO change
//        String[] lines = syntaxTextArea.getText().split(REGEX_NEW_LINE_CHAR[newLineTypeUsed]);
        
        int line = 0;
        int offset = position;
        while (offset > 0) {
            offset = Utilities.getRowStart(syntaxTextArea, offset - 1);
            line++;
        }
        int rowStart = Utilities.getRowStart(syntaxTextArea, position);
        int column = 1;
        if (rowStart > -1) {
            column = position - rowStart + 1;
        }
        if (column == 1) {
            line++;
        }
        
        return new Point(column, line);
    }

    /**
     * If overwrite mode is active then characters are removed when new characters are typed.
     * 
     * @param position
     * @param length
     */
    public void processInsert(int position, int length) {
        if (!jNotepad.isInsertMode()) {
            jNotepad.setInsertMode(true);
            Document document = syntaxTextArea.getDocument();
            try {
                int trimLength = length;
                if (position + trimLength > syntaxTextArea.getText().length()) {
                    trimLength = syntaxTextArea.getText().length() - position;
                }
                if (trimLength > 0) {
                    document.remove(position, trimLength);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            jNotepad.setInsertMode(false);
        }
    }

    private void loadFile(File path) {
        Reader in = null;
        
        try (TikaInputStream tikaInputStream = TikaInputStream.get(new BufferedInputStream(new FileInputStream(path))) ) {
            EncodingDetector detector = new DefaultEncodingDetector();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            
            tikaInputStream.enableRewind();
            List<EncodingResult> results = detector.detect(tikaInputStream, metadata, context);
            if (!results.isEmpty()) {
                charset = results.get(0).getCharset();
            } else {
                charset = StandardCharsets.UTF_8;
            }
            if (charset.equals(StandardCharsets.ISO_8859_1)) {
                charset = StandardCharsets.UTF_8;
            }
            if (EncodingType.supportedEncodings.contains(charset)) {
                if (!EncodingType.encodingAliasMap.containsKey(charset)) {
                    for (Charset charsetForAlias : EncodingType.encodingAliasMap.keySet()) {
                        Set<String> alias = EncodingType.encodingAliasMap.get(charsetForAlias);
                        if (alias.contains(charsetForAlias.name())) {
                            charset = charsetForAlias;
                            break;
                        }
                    }
                }
            }
            tikaInputStream.rewind();
            in = new InputStreamReader(tikaInputStream);
            StringBuilder content = new StringBuilder();
            String line = null;
            BufferedReader reader = new BufferedReader(in);
            while ((line = reader.readLine()) != null) {
                content.append(line + NEW_LINE_CHAR[newLineTypeUsed]);
            }
            syntaxTextArea.setText(content.toString());
            setLanguageInfo(DocumentFormatType.determineFormatFromFile(path.getAbsolutePath()));
            in.close();
            undoManager.discardAllEdits();
            dirty = false;
            readOnly = !path.canWrite();
            if (!readOnly) {
                RandomAccessFile rout = null;
                FileLock lock = null;
                try {
                    rout = new RandomAccessFile(path, "rw");
                    FileChannel channel = rout.getChannel();
                    lock = channel.tryLock();
                } catch (Exception e) {
                    readOnly = true;
                } finally {
                    if (lock != null) {
                        lock.release();
                    }
                    ResourceCleanup.close(rout);
                }
            }
            updateStatusBar(syntaxTextArea.getCaretPosition());
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to find the file: " + path, "Error loading file", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to load the file: " + path, "Error loading file", JOptionPane.ERROR_MESSAGE);
        } finally {
            ResourceCleanup.close(in);
        }
    }

    private void removeKeyStrokes(RSyntaxTextArea textArea) {
        InputMap targetMap = null;
        InputMap parentMap = textArea.getInputMap();
        
        while (parentMap != null) {
            targetMap = parentMap;
            parentMap = targetMap.getParent();
        }
        
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        targetMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
    }
    
    /**
     * Save an existing document. If there is no existing document then call saveAs.
     * If a call to saveAs is made and the user cancels during the file save dialog 
     * then false will be returned.
     * 
     * @return true if the save was not interrupted, false otherwise
     */
    public boolean save() {
        boolean result = false;
        
        if (checkEncoding()) {
            if (fileName.equals(newFileName)) {
                result = saveAs();
            } else {
                saveFile();
                dirty = false;
                
                result = true;
            }
        }
        
        return result;
    }

    /**
     * Display a dialog to the user asking for a location and name of the file
     * to save. If the user cancels the dialog then return false.
     * 
     * @return false if the user cancels the file save dialog, true otherwise
     */
    public boolean saveAs() {
        boolean result = true;
        
        if (checkEncoding()) {
            String filePath = ApplicationPreferences.getCurrentFilePath();
            JFileChooser fileChooser = new JFileChooser(filePath);
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String selectedFilePath = selectedFile.getAbsolutePath().replace("\\", "/");
                parseFileName(selectedFilePath);
                setLanguageInfo(DocumentFormatType.determineFormatFromFile(selectedFilePath));
                saveFile();
                dirty = false;
            } else {
                result = false;
            }
        }
        
        return result;
    }
    
    private boolean checkEncoding() {
        boolean encodingOk = EncodingType.encodingAliasMap.containsKey(charset);

        if (!encodingOk) {
            if (JOptionPane.showConfirmDialog(this, "<html><p>The current encoding (<b>" + charset.displayName() + "</b>) is not supported for saving.</p><p>To continue saving you must select a new encoding.</p><p>Click <b>OK</b> to change encodings.</p><p>Click <b>Cancel</b> to not save the current changes.</p><html>", "Invalid Encoding", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                EncodingDialog encodingDialog = new EncodingDialog(jNotepad.getParentFrame(), charset);
                if (encodingDialog.showDialog()) {
                    encodingOk = true;
                    charset = encodingDialog.getCharset();
                }
            }
        }
        
        return encodingOk;
    }

    private void saveFile() {
        final JComponent parentComponent = this;
        final String path = filePath + FILE_SEPARATOR + fileName;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Writer out = null;
                
                try {
                    out = new OutputStreamWriter(new FileOutputStream(path), charset);
                    out.write(syntaxTextArea.getText());
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(parentComponent, "Unable to create the file: " + path + "\n" + e.getMessage(), "Error saving file", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(parentComponent, "Unable to save the file: " + path, "Error saving file", JOptionPane.ERROR_MESSAGE);
                } finally {
                    ResourceCleanup.close(out);
                }
            }
        });
    }

    /**
     * Check if this tab can be closed. If it is the last tab
     * a check is made to see if it is a new tab with no changes.
     * If it is false will be returned.
     * 
     * @param isLast true if this is the last open tab
     * @return True if the user is ok to close the document
     */
    public boolean requestClose(boolean isLast) {
        DirtyStatus status = checkDirty();
        
        boolean saveCompleted = true;
        if (status.equals(DirtyStatus.SAVE_FILE)) {
            saveCompleted = save();
        } else if (status.equals(DirtyStatus.DONT_SAVE_FILE)) {
            saveCompleted = true;
        } else if (status.equals(DirtyStatus.CANCEL_ACTION)) {// || (isLast && status.equals(DirtyStatus.DONT_SAVE_FILE) && fileName.equals(newFileName) && !dirty)) {
            saveCompleted = false;
        }
        
        return saveCompleted;
    }

    private DirtyStatus checkDirty() {
        DirtyStatus result = DirtyStatus.DONT_SAVE_FILE;
        
        if (dirty) {
            String filePath = (fileName.equals(newFileName) ? fileName : 
                ApplicationPreferences.getCurrentFilePath() + FILE_SEPARATOR + fileName);
            int choice = JOptionPane.showOptionDialog(this, 
                    "Do you want to save changes to " + filePath + "?", 
                    "JNotepad", 
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[] {"Save", "Don't Save", "Cancel"}, 
                    "Save");
            if (choice == JOptionPane.YES_OPTION) {
                result = DirtyStatus.SAVE_FILE;
            } else if (choice == JOptionPane.NO_OPTION) {
                result = DirtyStatus.DONT_SAVE_FILE;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                result = DirtyStatus.CANCEL_ACTION;
            }
        }
        
        return result;
    }

    private void parseFileName(String path) {
        int index = path.lastIndexOf("/");
        if (index > -1) {
            filePath = path.substring(0, index);
            fileName = path.substring(index + 1);
        } else {
            filePath = "";
            fileName = path;
        }
    }

    /**
     * Undo the last text input
     */
    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }
    
    /**
     * @return true if undo is available
     */
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    /**
     * Undo the last text input
     */
    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
    
    /**
     * @return true if redo is available
     */
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    /**
     * @return the languageInfo
     */
    public LanguageInfo getLanguageInfo() {
        return languageInfo;
    }

    /**
     * @param languageInfo the languageInfo to set
     */
    public void setLanguageInfo(LanguageInfo languageInfo) {
        this.languageInfo = languageInfo;
        syntaxTextArea.setSyntaxEditingStyle(languageInfo.getLanguage());
        updateStatusBar(syntaxTextArea.getCaretPosition());
    }

    /**
     * @return true if in insert mode or false for overwrite mode
     */
    public boolean isInsertMode() {
        return syntaxTextArea.getTextMode() == TextMode.INSERT;
    }
    
    /**
     * @param insertMode
     */
    public void setInsertMode(boolean insertMode) {
        syntaxTextArea.setTextMode(insertMode ? TextMode.INSERT : TextMode.OVERWRITE);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN && Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)) {
            JScrollBar bar = textScrollPane.getVerticalScrollBar();
            int unitIncrement = syntaxTextArea.getFontMetrics(syntaxTextArea.getFont()).getHeight();
            int maximum = bar.getMaximum();
            int value = bar.getValue();
            value += unitIncrement;
            if (value > maximum) {
                value = maximum;
            }
            bar.setValue(value);
        } else if (e.getKeyCode() == KeyEvent.VK_UP && Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)) {
            JScrollBar bar = textScrollPane.getVerticalScrollBar();
            int unitIncrement = syntaxTextArea.getFontMetrics(syntaxTextArea.getFont()).getHeight();
            int minimum = bar.getMinimum();
            int value = bar.getValue();
            value -= unitIncrement;
            if (value < minimum) {
                value = minimum;
            }
            bar.setValue(value);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentUpdated();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentUpdated();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentUpdated();
    }
    
    private void documentUpdated() {
        if (!dirty) {
            dirty = true;
            jNotepad.setTitle();
        }
        repaint();
    }

    /**
     * 
     */
    public void uppercase() {
        int selectionStart = syntaxTextArea.getSelectionStart();
        int selectionEnd = syntaxTextArea.getSelectionEnd();
        
        if (selectionStart != selectionEnd) {
            syntaxTextArea.replaceRange(syntaxTextArea.getSelectedText().toUpperCase(), selectionStart, selectionEnd);
        }
    }
    
    /**
     * 
     */
    public void lowercase() {
        int selectionStart = syntaxTextArea.getSelectionStart();
        int selectionEnd = syntaxTextArea.getSelectionEnd();
        
        if (selectionStart != selectionEnd) {
            syntaxTextArea.replaceRange(syntaxTextArea.getSelectedText().toLowerCase(), selectionStart, selectionEnd);
        }
        
    }

    enum DirtyStatus {
        SAVE_FILE, DONT_SAVE_FILE, CANCEL_ACTION;
    }
}
