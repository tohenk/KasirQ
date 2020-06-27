/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Toha <tohenk@yahoo.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package id.my.kasirq.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Window;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;

import jiconfont.IconCode;
import jiconfont.swing.IconFontSwing;

public class IconLoader {

    private static IconLoader instance;

    private final HashMap<String, String> maps = new HashMap<>();

    public void setAppIcon(Window w) {
        try {
            Image im = ImageIO.read(getClass().getResource("/app.png"));
            w.setIconImage(im);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIcon(Object o, AbstractButton btn) {
        setIcon(o, btn, 16);
    }

    public void setIcon(Object o, AbstractButton btn, int size) {
        setIcon(o, btn, size, SystemColor.textHighlight);
    }

    public void setIcon(Object o, AbstractButton btn, int size, Color color) {
        Icon icon;
        String key = o.getClass().getCanonicalName() + "." +
                btn.getActionCommand();
        if (maps.containsKey(key)) {
            IconCode ic = FontAwesome.get(maps.get(key));
            if (ic != null) {
                icon = IconFontSwing.buildIcon(ic, size, color);
                btn.setIcon(icon);
            } else {
                System.err.printf("Unknown icon %s!\n", key);
            }
        }
    }

    public void register(Object o, String name, String icon) {
        String key = o.getClass().getCanonicalName() + "." + name;
        if (!maps.containsKey(key)) {
            maps.put(key, icon);
        }
    }
    
    public static IconLoader getInstance() {
        if (instance == null) {
            instance = new IconLoader();
        }
        return instance;
    }
}
