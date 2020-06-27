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

package id.my.kasirq;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import id.my.kasirq.util.IconLoader;

public class Splash extends JFrame implements Runnable {

    private final boolean antialiasing = true;
    private final int margin = 10;
    private boolean active = true;
    private float textX;
    private float textY;
    private Image splashImage;
    private Image offImage;
    private String status;

    public Splash() {
        loadSplash();
        setUndecorated(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        IconLoader.getInstance().setAppIcon(this);
    }
    
    private void loadSplash() {
        try {
            splashImage = ImageIO.read(getClass()
                    .getResource("/splash/Splash.png"));
            setSize(splashImage.getWidth(null), splashImage.getHeight(null));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void setAntialiasing(Graphics2D g2, boolean value) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, value ?
                RenderingHints.VALUE_ANTIALIAS_ON :
                RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setAntialiasing(g2, antialiasing);
        g2.setFont(getFont());
        g2.drawImage(splashImage, 0, 0, null);
        if (status.length() > 0) {
            g2.drawString(status, textX, textY);
        }
    }

    protected void render() {
        Graphics g = getGraphics();
        if (g != null) {
            Dimension d = getSize();
            if (offImage == null) {
                offImage = createImage(d.width, d.height);
            }
            Graphics gr = offImage.getGraphics();
            paint(gr);
            g.drawImage(offImage, 0, 0, null);
            gr.dispose();
            g.dispose();
        }
    }

    public void setActive(boolean value) {
        active = value;
    }

    public void setStatus(String value) {
        status = value + "\u2026";
        textX = margin;
        textY = splashImage.getHeight(null) - margin;
    }
    
    public Image getImage() {
        return splashImage;
    }

    @Override
    public void run() {
        if (!isVisible()) setVisible(true);
        while (active) {
            render();
        }
        setVisible(false);
    }
}
