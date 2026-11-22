/**
 * 
 */
package io.github.markbernard.jedit.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import io.github.markbernard.jedit.ApplicationPreferences;
import io.github.markbernard.jedit.DocumentFormatType;
import io.github.markbernard.jedit.ResourceCleanup;
import io.github.markbernard.jedit.Translations;
import io.github.markbernard.jedit.theme.ColorPicker;
import io.github.markbernard.jedit.theme.RSyntaxTheme;
import io.github.markbernard.jedit.theme.ThemeManager;

/**
 * 
 */
public class ThemeDialog extends JDialog implements WindowListener {
    private static final long serialVersionUID = -3171638741388684292L;
    
    private RSyntaxTheme currentTheme;
    private RSyntaxTheme loadedTheme;
    private String currentExtension = "java";
    
    private JSplitPane mainSplit;
    private JSplitPane styleSplit;
    private RSyntaxTextArea syntaxTextArea;
    private JList<String> languageList;
    private JSpinner baseFontSize;
    private ColorPicker backgroundColor;
    private ColorPicker caretColor;
    private ColorPicker selectionBg;
    private ColorPicker selectionFg;
    private JCheckBox selectionRoundedEdges;
    private JCheckBox selectionUseFg;
    private ColorPicker currentLineHighlightColor;
    private JCheckBox currentLineHighlightFade;
    private ColorPicker tabLineColor;
    private ColorPicker marginLineFg;
    private ColorPicker markAllHighlightColor;
    private JCheckBox markOccurrencesHighlightBorder;
    private ColorPicker markOccurrencesHighlightColor;
    private JCheckBox matchedBracketAnimate;
    private ColorPicker matchedBracketBg;
    private ColorPicker matchedBracketFg;
    private JCheckBox matchedBracketHighlightBoth;
    private ColorPicker hyperlinksFg;
    private ColorPicker gutterBackgroundColor;
    private ColorPicker gutterBorderColor;
    private ColorPicker lineNumbersFg;
    private JComboBox<String> lineNumbersFontFamily;
    private JSpinner lineNumbersFontSize;
    private ColorPicker foldIndicatorFg;
    private ColorPicker foldIndicatorIconBg;
    private ColorPicker iconRowHeaderActiveLineRange;
    private JCheckBox iconRowHeaderInheritsGutterBg;
    private ColorPicker[] secondaryLanguagesBg;
    private JCheckBox[] tokenStylesBold;
    private ColorPicker[] tokenStylesFg;
    private JCheckBox[] tokenStylesItalic;
    private String[] tokenStylesToken;

    /**
     * @param owner 
     */
    public ThemeDialog(Frame owner) {
        super(owner, "Theme Editor", true);
        addWindowListener(this);
        setLayout(new BorderLayout());
        ApplicationPreferences.getInstance().getThemeDialogLocation(this);
        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(mainSplit, BorderLayout.CENTER);
        mainSplit.setDividerLocation(ApplicationPreferences.getInstance().getThemeDialogMainDividerLocation());
        
        JPanel languagePanel = new JPanel(new BorderLayout());
        JScrollPane languageScroll = new JScrollPane(languagePanel);
        mainSplit.add(languageScroll, JSplitPane.LEFT);
        languageScroll.getVerticalScrollBar().setUnitIncrement(10);
        List<String> documentTypes = new ArrayList<>(DocumentFormatType.FILE_TYPE_MAP.keySet());
        Collections.sort(documentTypes);
        languageList = new JList<>(documentTypes.toArray(new String[DocumentFormatType.FILE_TYPE_MAP.size()]));
        languageList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (loadedTheme != null && currentTheme != null && !loadedTheme.equals(currentTheme)) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to leave without saving changes?", "Abandon Changes", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
                super.setSelectionInterval(index0, index1);
            }
        });
        languageList.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 40));
        languagePanel.add(languageList, BorderLayout.CENTER);
        languageList.setSelectedValue(currentExtension, true);
        languageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    currentExtension = languageList.getSelectedValue();
                    loadTheme();
                    setThemeValues();
                    loadSample();
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(buttonPanel, BorderLayout.SOUTH);
        JButton okButton = new JButton("Ok");
        buttonPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTheme();
                exit();
            }
        });
        JButton applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTheme();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        
        loadTheme();
        createStyleSelections();
        createSamplePanel();
        setThemeValues();
        loadSample();
    }
    
    private void createStyleSelections() {
        styleSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        styleSplit.setDividerLocation(ApplicationPreferences.getInstance().getThemeDialogStyleDividerLocation());
        mainSplit.add(styleSplit, JSplitPane.RIGHT);
        JPanel styleSelectionsPanel = new JPanel(new GridLayout(0, 3, 5, 2));
        JScrollPane styleSelectionsScroll = new JScrollPane(styleSelectionsPanel);
        styleSplit.add(styleSelectionsScroll, JSplitPane.LEFT);
        styleSelectionsScroll.getVerticalScrollBar().setUnitIncrement(10);
        baseFontSize = new JSpinner(new SpinnerNumberModel(currentTheme.baseFont.size, 1, 144, 1));
        styleSelectionsPanel.add(new JLabel("Base Font"));
        styleSelectionsPanel.add(new JLabel("size"));
        styleSelectionsPanel.add(baseFontSize);
        baseFontSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.baseFont.size = (Integer) baseFontSize.getValue();
                applyTheme();
            }
        });
        backgroundColor = new ColorPicker(currentTheme.background.color);
        styleSelectionsPanel.add(new JLabel("Background"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(backgroundColor);
        backgroundColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.background.color = backgroundColor.getColorString();
                applyTheme();
            }
        });
        caretColor = new ColorPicker(currentTheme.caret.color);
        styleSelectionsPanel.add(new JLabel("Caret"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(caretColor);
        caretColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.caret.color = caretColor.getColorString();
                applyTheme();
            }
        });
        selectionBg = new ColorPicker(currentTheme.selection.bg);
        styleSelectionsPanel.add(new JLabel("Selection"));
        styleSelectionsPanel.add(new JLabel("bg"));
        styleSelectionsPanel.add(selectionBg);
        selectionBg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.selection.bg = selectionBg.getColorString();
                applyTheme();
            }
        });
        selectionFg = new ColorPicker(currentTheme.selection.fg);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(selectionFg);
        selectionFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.selection.fg = selectionFg.getColorString();
                applyTheme();
            }
        });
        selectionRoundedEdges = new JCheckBox("true", currentTheme.selection.roundedEdges);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("rounded edges"));
        styleSelectionsPanel.add(selectionRoundedEdges);
        selectionRoundedEdges.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.selection.roundedEdges = selectionRoundedEdges.isSelected();
                applyTheme();
            }
        });
        selectionUseFg = new JCheckBox("true", currentTheme.selection.useFG);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("Use FG"));
        styleSelectionsPanel.add(selectionUseFg);
        selectionUseFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.selection.useFG = selectionUseFg.isSelected();
                applyTheme();
            }
        });
        currentLineHighlightColor = new ColorPicker(currentTheme.currentLineHighlight.color);
        styleSelectionsPanel.add(new JLabel("Current Line Highlight"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(currentLineHighlightColor);
        currentLineHighlightColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.currentLineHighlight.color = currentLineHighlightColor.getColorString();
                applyTheme();
            }
        });
        currentLineHighlightFade = new JCheckBox("true", currentTheme.currentLineHighlight.fade);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("fade"));
        styleSelectionsPanel.add(currentLineHighlightFade);
        currentLineHighlightFade.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.currentLineHighlight.fade = currentLineHighlightFade.isSelected();
                applyTheme();
            }
        });
        tabLineColor = new ColorPicker(currentTheme.tabLine.color);
        styleSelectionsPanel.add(new JLabel("Tab Line"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(tabLineColor);
        tabLineColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.tabLine.color = tabLineColor.getColorString();
                applyTheme();
            }
        });
        marginLineFg = new ColorPicker(currentTheme.marginLine.fg);
        styleSelectionsPanel.add(new JLabel("Margin Line"));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(marginLineFg);
        marginLineFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.marginLine.fg = marginLineFg.getColorString();
                applyTheme();
            }
        });
        markAllHighlightColor = new ColorPicker(currentTheme.markAllHighlight.color);
        styleSelectionsPanel.add(new JLabel("Mark All Highlight"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(markAllHighlightColor);
        markAllHighlightColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.markAllHighlight.color = markAllHighlightColor.getColorString();
                applyTheme();
            }
        });
        markOccurrencesHighlightBorder = new JCheckBox("true", currentTheme.selection.roundedEdges);
        styleSelectionsPanel.add(new JLabel("Mark Occurrences Highlight"));
        styleSelectionsPanel.add(new JLabel("border"));
        styleSelectionsPanel.add(markOccurrencesHighlightBorder);
        markOccurrencesHighlightBorder.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.markOccurrencesHighlight.border = markOccurrencesHighlightBorder.isSelected();
                applyTheme();
            }
        });
        markOccurrencesHighlightColor = new ColorPicker(currentTheme.markOccurrencesHighlight.color);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(markOccurrencesHighlightColor);
        markOccurrencesHighlightColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.markOccurrencesHighlight.color = markOccurrencesHighlightColor.getColorString();
                applyTheme();
            }
        });
        matchedBracketAnimate = new JCheckBox("true", currentTheme.selection.roundedEdges);
        styleSelectionsPanel.add(new JLabel("Matched Bracket"));
        styleSelectionsPanel.add(new JLabel("animate"));
        styleSelectionsPanel.add(matchedBracketAnimate);
        matchedBracketAnimate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.matchedBracket.animate = matchedBracketAnimate.isSelected();
                applyTheme();
            }
        });
        matchedBracketBg = new ColorPicker(currentTheme.matchedBracket.bg);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("bg"));
        styleSelectionsPanel.add(matchedBracketBg);
        matchedBracketBg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.matchedBracket.bg = matchedBracketBg.getColorString();
                applyTheme();
            }
        });
        matchedBracketFg = new ColorPicker(currentTheme.matchedBracket.fg);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(matchedBracketFg);
        matchedBracketFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.matchedBracket.fg = matchedBracketFg.getColorString();
                applyTheme();
            }
        });
        matchedBracketHighlightBoth = new JCheckBox("true", currentTheme.selection.roundedEdges);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("highlight both"));
        styleSelectionsPanel.add(matchedBracketHighlightBoth);
        matchedBracketHighlightBoth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.matchedBracket.highlightBoth = matchedBracketHighlightBoth.isSelected();
                applyTheme();
            }
        });
        hyperlinksFg = new ColorPicker(currentTheme.hyperlinks.fg);
        styleSelectionsPanel.add(new JLabel("Hyperlinks"));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(hyperlinksFg);
        hyperlinksFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.hyperlinks.fg = hyperlinksFg.getColorString();
                applyTheme();
            }
        });
        gutterBackgroundColor = new ColorPicker(currentTheme.gutterBackground.color);
        styleSelectionsPanel.add(new JLabel("Gutter Background"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(gutterBackgroundColor);
        gutterBackgroundColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.gutterBackground.color = gutterBackgroundColor.getColorString();
                applyTheme();
            }
        });
        gutterBorderColor = new ColorPicker(currentTheme.gutterBorder.color);
        styleSelectionsPanel.add(new JLabel("Gutter Border"));
        styleSelectionsPanel.add(new JLabel("color"));
        styleSelectionsPanel.add(gutterBorderColor);
        gutterBorderColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.gutterBorder.color = gutterBorderColor.getColorString();
                applyTheme();
            }
        });
        lineNumbersFg = new ColorPicker(currentTheme.lineNumbers.fg);
        styleSelectionsPanel.add(new JLabel("Line Numbers"));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(lineNumbersFg);
        lineNumbersFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.lineNumbers.fg = lineNumbersFg.getColorString();
                applyTheme();
            }
        });
        lineNumbersFontFamily = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("font family"));
        styleSelectionsPanel.add(lineNumbersFontFamily);
        lineNumbersFontFamily.setSelectedItem(currentTheme.lineNumbers.fontFamily);
        lineNumbersFontFamily.setEditable(false);
        lineNumbersFontFamily.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                currentTheme.lineNumbers.fontFamily = (String) lineNumbersFontFamily.getSelectedItem();
                applyTheme();
            }
        });
        lineNumbersFontSize = new JSpinner(new SpinnerNumberModel(currentTheme.lineNumbers.fontSize, 1, 144, 1));
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("font size"));
        styleSelectionsPanel.add(lineNumbersFontSize);
        lineNumbersFontSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.lineNumbers.fontSize = (Integer) lineNumbersFontSize.getValue();
                applyTheme();
            }
        });
        foldIndicatorFg = new ColorPicker(currentTheme.foldIndicator.fg);
        styleSelectionsPanel.add(new JLabel("Fold Indicator"));
        styleSelectionsPanel.add(new JLabel("fg"));
        styleSelectionsPanel.add(foldIndicatorFg);
        foldIndicatorFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.foldIndicator.fg = foldIndicatorFg.getColorString();
                applyTheme();
            }
        });
        foldIndicatorIconBg = new ColorPicker(currentTheme.foldIndicator.iconBg);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("icon BG"));
        styleSelectionsPanel.add(foldIndicatorIconBg);
        foldIndicatorIconBg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.foldIndicator.iconBg = foldIndicatorIconBg.getColorString();
                applyTheme();
            }
        });
        iconRowHeaderActiveLineRange = new ColorPicker(currentTheme.iconRowHeader.activeLineRange);
        styleSelectionsPanel.add(new JLabel("Icon Row Header"));
        styleSelectionsPanel.add(new JLabel("active line range"));
        styleSelectionsPanel.add(iconRowHeaderActiveLineRange);
        iconRowHeaderActiveLineRange.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.iconRowHeader.activeLineRange = iconRowHeaderActiveLineRange.getColorString();
                applyTheme();
            }
        });
        iconRowHeaderInheritsGutterBg = new JCheckBox("true", currentTheme.selection.roundedEdges);
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel("inherits gutter bg"));
        styleSelectionsPanel.add(iconRowHeaderInheritsGutterBg);
        iconRowHeaderInheritsGutterBg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentTheme.iconRowHeader.inheritsGutterBG = iconRowHeaderInheritsGutterBg.isSelected();
                applyTheme();
            }
        });
        
        secondaryLanguagesBg = new ColorPicker[currentTheme.secondaryLanguages.size()];
        for (int i=0;i<currentTheme.secondaryLanguages.size();i++) {
            secondaryLanguagesBg[i] = new ColorPicker(currentTheme.secondaryLanguages.get(i).bg);
            if (i == 0) {
                styleSelectionsPanel.add(new JLabel("Secondary Languages"));
            } else {
                styleSelectionsPanel.add(new JLabel(""));
            }
            styleSelectionsPanel.add(new JLabel("bg"));
            styleSelectionsPanel.add(secondaryLanguagesBg[i]);
            final int index = i;
            secondaryLanguagesBg[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    currentTheme.secondaryLanguages.get(index).bg = secondaryLanguagesBg[index].getColorString();
                    applyTheme();
                }
            });
        }
        tokenStylesBold = new JCheckBox[currentTheme.tokenStyles.size()];
        tokenStylesFg = new ColorPicker[currentTheme.tokenStyles.size()];
        tokenStylesItalic = new JCheckBox[currentTheme.tokenStyles.size()];
        tokenStylesToken = new String[currentTheme.tokenStyles.size()];
        styleSelectionsPanel.add(new JLabel("Styles"));
        styleSelectionsPanel.add(new JLabel(""));
        styleSelectionsPanel.add(new JLabel(""));
        for (int i=0;i<currentTheme.tokenStyles.size();i++) {
            if (currentTheme.tokenStyles.get(i).fg != null) {
                final int index = i;
                tokenStylesToken[i] = currentTheme.tokenStyles.get(i).token;
                tokenStylesBold[i] = new JCheckBox("true");
                tokenStylesBold[i].setSelected(currentTheme.tokenStyles.get(i).bold != null && currentTheme.tokenStyles.get(i).bold.booleanValue());
                styleSelectionsPanel.add(new JLabel(Translations.getString(tokenStylesToken[i])));
                styleSelectionsPanel.add(new JLabel("bold"));
                styleSelectionsPanel.add(tokenStylesBold[i]);
                tokenStylesBold[i].addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        currentTheme.tokenStyles.get(index).bold = tokenStylesBold[index].isSelected() ? Boolean.TRUE : null;
                        applyTheme();
                    }
                });
                
                tokenStylesFg[i] = new ColorPicker(currentTheme.tokenStyles.get(i).fg);
                styleSelectionsPanel.add(new JLabel(""));
                styleSelectionsPanel.add(new JLabel("fg"));
                styleSelectionsPanel.add(tokenStylesFg[i]);
                tokenStylesFg[i].addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        currentTheme.tokenStyles.get(index).fg = tokenStylesFg[index].getColorString();
                        applyTheme();
                    }
                });
                
                tokenStylesItalic[i] = new JCheckBox("true");
                tokenStylesItalic[i].setSelected(currentTheme.tokenStyles.get(i).italic != null && currentTheme.tokenStyles.get(i).italic.booleanValue());
                styleSelectionsPanel.add(new JLabel(""));
                styleSelectionsPanel.add(new JLabel("italic"));
                styleSelectionsPanel.add(tokenStylesItalic[i]);
                tokenStylesItalic[i].addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        currentTheme.tokenStyles.get(index).italic = tokenStylesItalic[index].isSelected() ? Boolean.TRUE : null;
                        applyTheme();
                    }
                });
            }
        }        
    }
    
    private void createSamplePanel() {
        JPanel samplePanel = new JPanel(new BorderLayout());
        styleSplit.add(samplePanel, JSplitPane.RIGHT);
        syntaxTextArea = new RSyntaxTextArea();
        RTextScrollPane scrollPane = new RTextScrollPane(syntaxTextArea);
        syntaxTextArea.setEditable(false);
        samplePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadTheme() {
        try {
            loadedTheme = ThemeManager.load(currentExtension);
            if (loadedTheme != null) {
                currentTheme = ThemeManager.load(currentExtension);
            } else {
                loadedTheme = ThemeManager.getDefaultTheme(currentExtension);
                currentTheme = ThemeManager.getDefaultTheme(currentExtension);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setThemeValues() {
        baseFontSize.setValue(currentTheme.baseFont.size);
        backgroundColor.setColorString(currentTheme.background.color);
        caretColor.setColorString(currentTheme.caret.color);
        selectionBg.setColorString(currentTheme.selection.bg);
        selectionFg.setColorString(currentTheme.selection.fg);
        selectionRoundedEdges.setSelected(currentTheme.selection.roundedEdges);
        selectionUseFg.setSelected(currentTheme.selection.useFG);
        currentLineHighlightColor.setColorString(currentTheme.currentLineHighlight.color);
        currentLineHighlightFade.setSelected(currentTheme.currentLineHighlight.fade);
        tabLineColor.setColorString(currentTheme.tabLine.color);
        marginLineFg.setColorString(currentTheme.marginLine.fg);
        markAllHighlightColor.setColorString(currentTheme.markAllHighlight.color);
        markOccurrencesHighlightBorder.setSelected(currentTheme.markOccurrencesHighlight.border);
        markOccurrencesHighlightColor.setColorString(currentTheme.markOccurrencesHighlight.color);
        matchedBracketAnimate.setSelected(currentTheme.matchedBracket.animate);
        matchedBracketBg.setColorString(currentTheme.matchedBracket.bg);
        matchedBracketFg.setColorString(currentTheme.matchedBracket.fg);
        matchedBracketHighlightBoth.setSelected(currentTheme.matchedBracket.highlightBoth);
        hyperlinksFg.setColorString(currentTheme.hyperlinks.fg);
        gutterBackgroundColor.setColorString(currentTheme.gutterBackground.color);
        gutterBorderColor.setColorString(currentTheme.gutterBorder.color);
        lineNumbersFg.setColorString(currentTheme.lineNumbers.fg);
        lineNumbersFontFamily.setSelectedItem(currentTheme.lineNumbers.fontFamily);
        lineNumbersFontSize.setValue(currentTheme.lineNumbers.fontSize);
        foldIndicatorFg.setColorString(currentTheme.foldIndicator.fg);
        foldIndicatorIconBg.setColorString(currentTheme.foldIndicator.iconBg);
        iconRowHeaderActiveLineRange.setColorString(currentTheme.iconRowHeader.activeLineRange);
        iconRowHeaderInheritsGutterBg.setSelected(currentTheme.iconRowHeader.inheritsGutterBG);
        for (int i=0;i<currentTheme.secondaryLanguages.size();i++) {
            secondaryLanguagesBg[i].setColorString(currentTheme.secondaryLanguages.get(i).bg);
        }
        for (int i=0;i<currentTheme.tokenStyles.size();i++) {
            if (currentTheme.tokenStyles.get(i).fg != null) {
                tokenStylesBold[i].setSelected(currentTheme.tokenStyles.get(i).bold != null && currentTheme.tokenStyles.get(i).bold.booleanValue());
                tokenStylesFg[i].setColorString(currentTheme.tokenStyles.get(i).fg);
                tokenStylesItalic[i].setSelected(currentTheme.tokenStyles.get(i).italic != null && currentTheme.tokenStyles.get(i).italic.booleanValue());
            }
        }
        syntaxTextArea.setSyntaxEditingStyle(DocumentFormatType.determineFormatFromFile("." + currentExtension).getLanguage());
    }
    
    private void applyTheme() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try (Writer out = new OutputStreamWriter(bout, StandardCharsets.UTF_8)) {
            Format format = new Format(4, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<!DOCTYPE RSyntaxTheme SYSTEM \"theme.dtd\">");
            Serializer serializer = new Persister(format);
            serializer.write(currentTheme, out);
            out.close();
            Theme theme = Theme.load(new ByteArrayInputStream(bout.toByteArray()));
            theme.apply(syntaxTextArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadSample() {
        Reader in = null;
        try {
            InputStream iin = getClass().getResourceAsStream("/text/sample." + currentExtension);
            if (iin != null) {
                in = new InputStreamReader(iin, StandardCharsets.UTF_8);
                StringBuilder content = new StringBuilder();
                char[] buffer = new char[65536];
                int read = -1;
                while ((read = in.read(buffer)) > -1) {
                    content.append(buffer, 0, read);
                }
                syntaxTextArea.setText(content.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ResourceCleanup.close(in);
        }
    }
    
    private void getThemeValues() {
        currentTheme.baseFont.size = (Integer) baseFontSize.getValue();
        currentTheme.background.color = backgroundColor.getColorString();
        currentTheme.caret.color = caretColor.getColorString();
        currentTheme.selection.bg = selectionBg.getColorString();
        currentTheme.selection.fg = selectionFg.getColorString();
        currentTheme.selection.roundedEdges = selectionRoundedEdges.isSelected();
        currentTheme.selection.useFG = selectionUseFg.isSelected();
        currentTheme.currentLineHighlight.color = currentLineHighlightColor.getColorString();
        currentTheme.currentLineHighlight.fade = currentLineHighlightFade.isSelected();
        currentTheme.tabLine.color = tabLineColor.getColorString();
        currentTheme.marginLine.fg = marginLineFg.getColorString();
        currentTheme.markAllHighlight.color = markAllHighlightColor.getColorString();
        currentTheme.markOccurrencesHighlight.border = markOccurrencesHighlightBorder.isSelected();
        currentTheme.markOccurrencesHighlight.color = markOccurrencesHighlightColor.getColorString();
        currentTheme.matchedBracket.animate = matchedBracketAnimate.isSelected();
        currentTheme.matchedBracket.bg = matchedBracketBg.getColorString();
        currentTheme.matchedBracket.fg = matchedBracketFg.getColorString();
        currentTheme.matchedBracket.highlightBoth = matchedBracketHighlightBoth.isSelected();
        currentTheme.hyperlinks.fg = hyperlinksFg.getColorString();
        currentTheme.gutterBackground.color = gutterBackgroundColor.getColorString();
        currentTheme.gutterBorder.color = gutterBorderColor.getColorString();
        currentTheme.lineNumbers.fg = lineNumbersFg.getColorString();
        currentTheme.lineNumbers.fontFamily = (String) lineNumbersFontFamily.getSelectedItem();
        currentTheme.lineNumbers.fontSize = (Integer) lineNumbersFontSize.getValue();
        currentTheme.foldIndicator.fg = foldIndicatorFg.getColorString();
        currentTheme.foldIndicator.iconBg = foldIndicatorIconBg.getColorString();
        currentTheme.iconRowHeader.activeLineRange = iconRowHeaderActiveLineRange.getColorString();
        currentTheme.iconRowHeader.inheritsGutterBG = iconRowHeaderInheritsGutterBg.isSelected();
        for (int i=0;i<currentTheme.secondaryLanguages.size();i++) {
            currentTheme.secondaryLanguages.get(i).bg = secondaryLanguagesBg[i].getColorString();
        }
        for (int i=0;i<currentTheme.tokenStyles.size();i++) {
            if (currentTheme.tokenStyles.get(i).fg != null) {
                currentTheme.tokenStyles.get(i).bold = tokenStylesBold[i].isSelected() ? Boolean.TRUE : null;
                currentTheme.tokenStyles.get(i).fg = tokenStylesFg[i].getColorString();
                currentTheme.tokenStyles.get(i).italic = tokenStylesItalic[i].isSelected() ? Boolean.TRUE : null;
            }
        }
    }

    private void saveCurrentTheme() {
        if (!loadedTheme.equals(currentTheme)) {
            try {
                getThemeValues();
                ThemeManager.save(currentTheme, currentExtension);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void exit() {
        ApplicationPreferences.getInstance().setThemeDialogLocation(this);
        ApplicationPreferences.getInstance().setThemeDialogMainDividerLocation(mainSplit.getDividerLocation());
        ApplicationPreferences.getInstance().setThemeDialogStyleDividerLocation(styleSplit.getDividerLocation());
        setVisible(false);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

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
}
