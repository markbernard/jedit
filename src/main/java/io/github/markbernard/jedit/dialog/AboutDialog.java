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
package io.github.markbernard.jedit.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;

/**
 * @author Mark Bernard
 *
 */
public class AboutDialog extends BasicDialog {
    private static final long serialVersionUID = -7298025013614141428L;

    /**
     * @param owner
     */
    public AboutDialog(Frame owner) {
        super(owner, "About JEdit", true);
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        JPanel topTitlePanel = new JPanel();
        mainPanel.add(topTitlePanel, BorderLayout.NORTH);
        JLabel titleLabel = new JLabel("<html><h1>JEdit</h1></html>");
        topTitlePanel.add(titleLabel);

        JPanel messagePanel = new JPanel(new BorderLayout());
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setContentType("text/html");
        editorPane.setText(loadTextFromResource("/text/about.html"));
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                String href = e.getDescription(); // Gets the raw href string
                
                if ("action:settings".equals(href)) {
                    System.out.println("Custom action triggered!");
                } else if (e.getURL() != null) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(editorPane);
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        JButton okButton = new JButton("OK");
        buttonPanel.add(okButton);
        okButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });

        addEscapeToActionMap(okButton);
        pack();
        centerDialog();
        setResizable(false);
    }

    @Override
    protected void userExit() {
        //No effect when user clicks 'X'
    }
}
