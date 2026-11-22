/**
 * 
 */
package io.github.markbernard.jedit.theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Mark Bernard
 */
public class ColorPicker extends JComponent implements MouseListener {
    private static final long serialVersionUID = 7970091432560046242L;
    
    private Set<ChangeListener> listenerList;
    private Color colorValue = Color.WHITE;

    /**
     * 
     */
    public ColorPicker() {
        listenerList = new HashSet<>();
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createLineBorder(Color.BLACK, 2)));
        addMouseListener(this);
    }

    /**
     * @param color
     */
    public ColorPicker(Color color) {
        this();
        colorValue = color;
    }
    
    /**
     * @param colorString
     */
    public ColorPicker(String colorString) {
        this(new Color(Integer.parseInt(colorString.substring(0, 2), 16), Integer.parseInt(colorString.substring(2, 4), 16), Integer.parseInt(colorString.substring(4, 6), 16)));
    }
    
    /**
     * @return the colorValue
     */
    public Color getColor() {
        return colorValue;
    }

    /**
     * @param colorValue the colorValue to set
     */
    public void setColor(Color colorValue) {
        this.colorValue = colorValue;
        repaint();
    }

    /**
     * @return the colorValue
     */
    public String getColorString() {
        return String.format("%02x%02x%02x", colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue());
    }

    /**
     * @param colorString the colorString to set
     */
    public void setColorString(String colorString) {
        colorValue = new Color(Integer.parseInt(colorString.substring(0, 2), 16), Integer.parseInt(colorString.substring(2, 4), 16), Integer.parseInt(colorString.substring(4, 6), 16));
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(colorValue);
        Insets insets = getInsets();
        g.fillRect(insets.left, insets.top, getWidth() - (insets.right + insets.left), getHeight() - (insets.bottom + insets.top));
    }

    /**
     * 
     * @param listener
     */
    public void addChangeListener(ChangeListener listener) {
        if (listener != null) {
            listenerList.add(listener);
        }
    }
    
    /**
     * @param listener
     */
    public void removeChangeListener(ChangeListener listener) {
        if (listener != null) {
            listenerList.remove(listener);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        final JColorChooser chooser = new JColorChooser(colorValue);
        AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
        chooser.removeChooserPanel(panels[0]);
        final ColorPicker self = this;
        JDialog dialog = JColorChooser.createDialog(this, "Choose color", true, chooser, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        colorValue = chooser.getColor();
                        repaint();
                        for (ChangeListener listener : listenerList) {
                            listener.stateChanged(new ChangeEvent(self));
                        }
                    }
                });
            }
        }, null);
        dialog.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
