/**
 * 
 */
package io.github.markbernard.jedit.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import io.github.markbernard.jedit.JEdit;

/**
 * 
 * @author Mark Bernard
 */
public class PreferencesAction extends AbstractAction {
    private static final long serialVersionUID = 6512237486679030523L;
    
    /**
     * 
     */
    public PreferencesAction() {
        putValue(MNEMONIC_KEY, KeyEvent.VK_P);
        putValue(NAME, "Preferences");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** provides menu controls but needs no action */
    }

    /**
     * @author Mark Bernard
     */
    public static class ConfigurationAction extends AbstractAction {
        private static final long serialVersionUID = -5711964150996442266L;
        private final transient JEdit jEdit;

        /**
         * @param jEdit 
         * 
         */
        public ConfigurationAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(NAME, "Configuration");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                jEdit.editConfiguration();
            }, "Show Configuration Dialog").start();
        }
    }

    /**
     * 
     * @author Mark Bernard
     */
    public static class StylesAction extends AbstractAction {
        private static final long serialVersionUID = -5711964150996442266L;
        private final transient JEdit jEdit;

        /**
         * @param jEdit 
         * 
         */
        public StylesAction(JEdit jEdit) {
            this.jEdit = jEdit;
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(NAME, "Styles");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                jEdit.editStyles();
            }, "Show Theme Dialog").start();
        }
    }
}
