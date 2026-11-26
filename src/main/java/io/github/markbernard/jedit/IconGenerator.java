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

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Mark Bernard
 */
public class IconGenerator {
    private static final Map<String, Icon> iconCache = new HashMap<>();
    private static final Map<String, Image> imageCache = new HashMap<>();
    private final String path;

    /**
     * @param path Path to the resource
     */
    public IconGenerator(String path) {
        this.path = path;
    }
    
    /**
     * @return The icon pointed to by the provided path
     */
    public Icon loadIcon() {
        Icon icon = iconCache.get(path);
        
        if (icon == null) {
            try {
                icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path)));
                iconCache.put(path, icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return icon;
    }
    
    /**
     * @return The image pointed to by the provided path
     */
    public Image loadImage() {
        Image image = imageCache.get(path);
        
        if (image == null) {
            try {
                image = ImageIO.read(getClass().getResourceAsStream(path));
                imageCache.put(path, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return image;
    }
}
