/**
 * 
 */
package io.github.markbernard.jedit.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;

import io.github.markbernard.jedit.ApplicationPreferences;

/**
 * 
 * @author Mark Bernard
 */
public class ConfigurationDialog extends BasicDialog {
    private static final long serialVersionUID = 6121114107561281395L;
    
    private static final String CONVERT_TO_TABS = "Convert Spaces To Tabs On Save";
    private static final String CONVERT_TO_SPACES = "Convert Tabs To Spaces On Save";
    
    private final transient ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();
    private final JCheckBox showStatusBarCheckbox;
    private final JCheckBox useSpacesInsteadOfTabsCheckbox;
    private final JSpinner tabSizeSPinner;
    private final JCheckBox converTabsOrSpacesCheckbox;
    private final JCheckBox enableLineNumbersCheckbox;
    private boolean saveTriggered;

    /**
     * @param owner
     * @param title
     * @param modal
     */
    public ConfigurationDialog(JFrame owner) {
        super(owner, "Edit Configuration", true);
        saveTriggered = false;
        setLocation(applicationPreferences.getConfigurationDialogX(), applicationPreferences.getConfigurationDialogY());
        
        setLayout(new BorderLayout());
        JPanel settingsPanel = new JPanel(new GridLayout(0, 2));
        add(settingsPanel, BorderLayout.CENTER);
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 5, 10), 
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
                        BorderFactory.createEmptyBorder(4, 4, 4, 4))));
        settingsPanel.add(new JLabel("Show Status Bar"));
        showStatusBarCheckbox = new JCheckBox("true", applicationPreferences.isShowStatusBar());
        settingsPanel.add(showStatusBarCheckbox);
        settingsPanel.add(new JLabel("Use Spaces For Tabs"));
        useSpacesInsteadOfTabsCheckbox = new JCheckBox("true", applicationPreferences.isSpacesInsteadOfTabs());
        settingsPanel.add(useSpacesInsteadOfTabsCheckbox);
        final JLabel convertToTabsOrSpacesLabel = new JLabel(applicationPreferences.isSpacesInsteadOfTabs() ? CONVERT_TO_SPACES : CONVERT_TO_TABS);
        settingsPanel.add(convertToTabsOrSpacesLabel);
        converTabsOrSpacesCheckbox = new JCheckBox("true", applicationPreferences.isConvertTabsOrSpacesOnSave());
        settingsPanel.add(converTabsOrSpacesCheckbox);
        settingsPanel.add(new JLabel("Tab Size"));
        tabSizeSPinner = new JSpinner(new SpinnerNumberModel());
        tabSizeSPinner.setValue(applicationPreferences.getTabSize());
        settingsPanel.add(tabSizeSPinner);
        settingsPanel.add(new JLabel("Enable Line Numbers"));
        enableLineNumbersCheckbox = new JCheckBox("true", applicationPreferences.isEnableLineNumbers());
        settingsPanel.add(enableLineNumbersCheckbox);
        
        useSpacesInsteadOfTabsCheckbox.addChangeListener((ChangeEvent e) -> {
            if (useSpacesInsteadOfTabsCheckbox.isSelected()) {
                convertToTabsOrSpacesLabel.setText(CONVERT_TO_SPACES);
            } else {
                convertToTabsOrSpacesLabel.setText(CONVERT_TO_TABS);
            }
        });
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        JPanel controlButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(controlButtonPanel, BorderLayout.EAST);
        controlButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));
        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> saveAndExit() );
        controlButtonPanel.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((ActionEvent e) -> exit() );
        controlButtonPanel.add(cancelButton);

        JPanel resetButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(resetButtonPanel, BorderLayout.WEST);
        JButton resetButton = new JButton("Reset All Configurations");
        resetButtonPanel.add(resetButton);
        resetButton.addActionListener((ActionEvent e) -> {
            applicationPreferences.resetToDefaults();
            showStatusBarCheckbox.setSelected(applicationPreferences.isShowStatusBar());
            useSpacesInsteadOfTabsCheckbox.setSelected(applicationPreferences.isSpacesInsteadOfTabs());
            converTabsOrSpacesCheckbox.setSelected(applicationPreferences.isConvertTabsOrSpacesOnSave());
            convertToTabsOrSpacesLabel.setText(applicationPreferences.isSpacesInsteadOfTabs() ? CONVERT_TO_SPACES : CONVERT_TO_TABS);
            enableLineNumbersCheckbox.setSelected(applicationPreferences.isEnableLineNumbers());
        });

        pack();
        setResizable(false);
    }

    @Override
    protected void userExit() {
        exit();
    }
    
    private void saveAndExit() {
        saveTriggered = true;
        applicationPreferences.setShowStatusBar(showStatusBarCheckbox.isSelected());
        applicationPreferences.setSpacesInsteadOfTabs(useSpacesInsteadOfTabsCheckbox.isSelected());
        applicationPreferences.setTabSize((Integer) tabSizeSPinner.getValue());
        applicationPreferences.setEnableLineNumbers(enableLineNumbersCheckbox.isSelected());
        exit();
    }
    
    private void exit() {
        Point p = getLocation();
        applicationPreferences.setConfigurationDialogX(p.x);
        applicationPreferences.setConfigurationDialogY(p.y);
        setVisible(false);
    }

    /**
     * @return the saveTriggered
     */
    public boolean isSaveTriggered() {
        return saveTriggered;
    }
}
