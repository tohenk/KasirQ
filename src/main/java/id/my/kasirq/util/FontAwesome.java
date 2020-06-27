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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.stream.JsonReader;

public class FontAwesome {

    private static FontAwesome instance;

    private final boolean useZip = true;
    private Zip zip;
    private final HashMap<String, Font> fonts = new HashMap<>();
    private final HashMap<String, IconCode> icons = new HashMap<>();

    private void addFont(String code, String name, FontStream stream) {
        if (!fonts.containsKey(code)) {
            fonts.put(code, new Font(name, stream));
        }
    }
    
    private void loadFont(String code, String name, String[] paths) throws IOException {
        addFont(code, name, new FontStreamZip(zip, paths));
    }
    
    private void loadFont(String code, String name, String path) {
        addFont(code, name, new FontStreamResource(path));
    }

    private void loadIcons(InputStream in) throws IOException {
        if (in != null) {
            IconsMetadata ic = new IconsMetadata(in) {
                @Override
                protected void addIcon(String name, String label, char code,
                        String style) {
                    if (!icons.containsKey(name) && fonts.containsKey(style)) {
                        String font = fonts.get(style).getFontFamily();
                        IconCode ic = new IconCode(font, label, code);
                        icons.put(name, ic);
                    }
                }
            };
            ic.load();
        }
    }

    private void loadIcons(String[] paths) throws IOException {
        loadIcons(zip.getStream(paths));
    }

    private void loadIcons(String path) throws IOException {
        InputStream in = FontAwesome.class.getResourceAsStream(path);
        if (in != null) {
            loadIcons(in);
        }
    }

    private void load() {
        try {
            if (useZip) {
                if (zip == null) zip = new Zip();
                loadFont("regular", "FontAwesomeRegular",
                        new String[] {"otfs", "Font Awesome 5 Free-Regular-400.otf"});
                loadFont("solid", "FontAwesomeSolid",
                        new String[] {"otfs", "Font Awesome 5 Free-Solid-900.otf"});
                loadFont("brands", "FontAwesomeBrands",
                        new String[] {"otfs", "Font Awesome 5 Brands-Regular-400.otf"});
                loadIcons(new String[] {"metadata", "icons.json"});
            } else {
                loadFont("regular", "FontAwesomeRegular",
                        "/fontawesome/webfonts/fa-regular-400.ttf");
                loadFont("solid", "FontAwesomeSolid",
                        "/fontawesome/webfonts/fa-solid-900.ttf");
                loadFont("brands", "FontAwesomeBrands",
                        "/fontawesome/webfonts/fa-brands-400.ttf");
                loadIcons("/fontawesome/metadata/icons.json");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerFonts() {
        for (Map.Entry<String, Font> f: fonts.entrySet()) {
            jiconfont.swing.IconFontSwing.register(f.getValue());
        }
    }
    
    public static FontAwesome getInstance() {
        if (instance == null) {
            instance = new FontAwesome();
            instance.load();
        }
        return instance;
    }
    
    public static IconCode get(String name) {
        if (getInstance().icons.containsKey(name)) {
            IconCode ic = getInstance().icons.get(name);
            return ic;
        } else {
            System.err.printf("Icon doesn't exist: %s!\n", name);
        }
        return null;
    }

    private static String cleanFilename(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx >= 0) {
            filename = filename.substring(0, idx);
        }
        return filename;
    }

    public class IconCode implements jiconfont.IconCode {

        private final String fontName;
        private final String iconName;
        private final char iconChar;

        public IconCode(String font, String name, char code) {
            fontName = font;
            iconName = name;
            iconChar = code;
        }

        @Override
        public String name() {
            return iconName;
        }

        @Override
        public char getUnicode() {
            return iconChar;
        }

        @Override
        public String getFontFamily() {
            return fontName;
        }
    }
    
    interface FontStream {
        public InputStream getFontInputStream();
    }
    
    class FontStreamResource implements FontStream {

        private final String resName;

        public FontStreamResource(String res) {
            resName = res;
        }
        
        @Override
        public InputStream getFontInputStream() {
            return FontAwesome.class.getResourceAsStream(resName);
        }
    }

    class FontStreamZip implements FontStream {

        private final Zip zip;
        private final String path[];

        public FontStreamZip(Zip z, String[] p) {
            zip = z;
            path = p;
        }
        
        @Override
        public InputStream getFontInputStream() {
            return zip.getStream(path);
        }
    }

    class Font implements jiconfont.IconFont {

        private final String fontName;
        private final FontStream fontStream;

        public Font(String name, FontStream stream) {
            fontName = name;
            fontStream = stream;
        }
        
        @Override
        public String getFontFamily() {
            return fontName;
        }

        @Override
        public InputStream getFontInputStream() {
            return fontStream.getFontInputStream();
        }
    }

    class Zip {

        static final String FONT_AWESOME_ZIP =
                "/fontawesome/fontawesome-free-5.13.0-desktop.zip";

        private final String root;

        public Zip() throws IOException {
            File f = new File(FONT_AWESOME_ZIP);
            root = FontAwesome.cleanFilename(f.getName());
        }

        public InputStream getStream(String[] names) {
            InputStream res = null;
            String entryName = root;
            for (String n: names) {
                if (entryName.length() > 0) entryName += "/";
                entryName += n;
            }
            InputStream is = getClass().getResourceAsStream(FONT_AWESOME_ZIP);
            ZipInputStream z = new ZipInputStream(is);
            ZipEntry entry;
            try {
                while ((entry = z.getNextEntry()) != null) {
                    if (entry.getName().equals(entryName)) {
                        int n;
                        byte[] buf = new byte[1024 * 1024];
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        while ((n = z.read(buf, 0, buf.length)) != -1) {
                            out.write(buf, 0, n);
                        }
                        res = new ByteArrayInputStream(out.toByteArray());
                        break;
                    }
                }
                z.close();
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
    }

    class IconsMetadata {

        private final JsonReader json;

        public IconsMetadata(InputStream in) {
            InputStreamReader reader = new InputStreamReader(in);
            json = new JsonReader(reader);
        }

        private ArrayList<String> getStyles() throws IOException {
            ArrayList<String> styles = new ArrayList<>();
            json.beginArray();
            while (json.hasNext()) {
                styles.add(json.nextString());
            }
            json.endArray();
            return styles;
        }
        
        private void readIcon(String iconKey) throws IOException {
            ArrayList<String> styles = null;
            String iconName = null;
            char iconCode = 0;
            String key;
            json.beginObject();
            while (json.hasNext()) {
                key = json.nextName();
                switch (key) {
                    case "label":
                        iconName = json.nextString();
                        break;
                    case "unicode":
                        iconCode = (char) Integer.parseInt(json.nextString(), 16);
                        break;
                    case "styles":
                        styles = getStyles();
                        break;
                    default:
                        json.skipValue();
                        break;
                }
            }
            json.endObject();
            if (iconName != null) {
                addIcon(iconKey, iconName, iconCode, styles != null ?
                        styles.get(0) : null);
            }
        }
        
        protected void addIcon(String name, String label, char code, String style) {
        }

        public void load() {
            try {
                json.beginObject();
                while (json.hasNext()) {
                    String iconName = json.nextName();
                    readIcon(iconName);
                }
                json.endObject();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}