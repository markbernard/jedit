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
package jedit.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Mark Bernard
 *
 */
public class EncodingDialog extends BasicDialog {
    private static final long serialVersionUID = 8540171150229302594L;
    
    private JButton okButton;
    private boolean encodingSelected;
    private Charset selectedCharset;

    /**
     * @param owner
     * @param charset 
     */
    public EncodingDialog(Frame owner, Charset charset) {
        super(owner, "Select Encoding", true);
        
        encodingSelected = false;
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel titlePanel = new JPanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(new JLabel("Select the encoding from one of the supported encodings."));
        
        boolean enableOkButton = false;
        JPanel encodingListPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.add(encodingListPanel, BorderLayout.CENTER);
        encodingListPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Encodings"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        ButtonGroup encodingButtonGroup = new ButtonGroup();
        JRadioButton utf8 = createButton(StandardCharsets.UTF_8, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(utf8, charset, StandardCharsets.UTF_8);
        }
        JRadioButton utf16 = createButton(StandardCharsets.UTF_16, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(utf16, charset, StandardCharsets.UTF_16);
        }
        JRadioButton utf16be = createButton(StandardCharsets.UTF_16BE, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(utf16be, charset, StandardCharsets.UTF_16BE);
        }
        JRadioButton utf16le = createButton(StandardCharsets.UTF_16LE, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(utf16le, charset, StandardCharsets.UTF_16LE);
        }
        JRadioButton usAscii = createButton(StandardCharsets.US_ASCII, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(usAscii, charset, StandardCharsets.US_ASCII);
        }
        JRadioButton iso88591 = createButton(StandardCharsets.ISO_8859_1, encodingListPanel, encodingButtonGroup);
        if (!enableOkButton) {
            enableOkButton = checkEncodingName(iso88591, charset, StandardCharsets.ISO_8859_1);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        okButton = new JButton("Ok");
        buttonPanel.add(okButton);
        getRootPane().setDefaultButton(okButton);
        addEscapeToActionMap(okButton);
        okButton.setEnabled(enableOkButton);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encodingSelected = true;
                setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        addEscapeToActionMap(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encodingSelected = false;
                setVisible(false);
            }
        });
        
        centerDialog();
        pack();
    }
    
    private JRadioButton createButton(Charset charset, JPanel encodingListPanel, ButtonGroup encodingButtonGroup) {
        JRadioButton radioButton = new JRadioButton(charset.name());
        encodingButtonGroup.add(radioButton);
        encodingListPanel.add(radioButton);
        addEscapeToActionMap(radioButton);
        radioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButton.setEnabled(true);
                selectedCharset = charset;
            }
        });
        
        return radioButton;
    }
    
    private boolean checkEncodingName(JRadioButton radioButton, Charset charset, Charset charsetCheck) {
        boolean result = false;
        
        if (charset.name().equals(charsetCheck)) {
            radioButton.setSelected(true);
            selectedCharset = charset;
            result = true;
        }
        
        return result;
    }
    
    /**
     * @return true if an encoding was selected.
     */
    public boolean showDialog() {
        setVisible(true);
        dispose();
        
        return encodingSelected;
    }
    /**
     * @return the encoding
     */
    public Charset getCharset() {
        return selectedCharset;
    }

    @Override
    protected void userExit() {
        encodingSelected = false;
    }
}
