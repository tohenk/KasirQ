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

import java.util.ArrayList;
import java.util.List;

import id.my.kasirq.model.Invoice;
import id.my.kasirq.model.Item;
import id.my.kasirq.model.Store;
import id.my.kasirq.util.RawPrinter;

public class Invoices {
    
    private static Invoices instance;

    private final List<Model> models = new ArrayList<>();
    private Model active = null;

    public List<Model> getModels() {
        return models;
    }

    public Model getModel() {
        return active;
    }

    public void setModel(String name) {
        for (Model model: models) {
            if (model.getName().equals(name)) {
                active = model;
                break;
            }
        }
    }

    private void registerModels() {
        models.add(new Invoice58FontA());
        models.add(new Invoice58FontB());
    }

    public static Invoices getInstance() {
        if (instance == null) {
            instance = new Invoices();
            instance.registerModels();
        }
        return instance;
    }

    public abstract class Model {

        public static final int LINE_NONE = 0;
        public static final int LINE_CODED = 1;
        public static final int LINE_MULTI = 2;
        public static final int LINE_PADDED = 4;
        public static final int LINE_JUSTIFIED = 8;

        protected final String name;
        protected String eol = "\n";
        protected String justifySep = ":";
        protected Invoice inv;
        protected String lines;
        protected int lineLength = 0;
        protected String note;

        public Model(String n) {
            name = n;
        }

        public Model(String n, int length) {
            name = n;
            lineLength = length;
        }

        public String getName() {
            return name;
        }

        public Invoice getInvoice() {
            return inv;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String s) {
            note = s;
        }

        protected int getLineLength() {
            return lineLength;
        }

        protected String repeat(String s, int len) {
            String res = "";
            while (res.length() < len) {
                res += s;
            }
            return res;
        }

        protected String padLeft(String s, int length) {
            if (s != null && !s.isEmpty() && s.length() < length) {
                s = repeat(" ", length - s.length()) + s;
            }
            return s;
        }

        protected String padLine(String s, int length) {
            if (s != null && !s.isEmpty() && s.length() < length) {
                s += repeat(" ", length - s.length());
            }
            return s;
        }

        protected String trimLine(String s) {
            if (s != null && !s.isEmpty() && s.length() > lineLength) {
                s = s.substring(0, lineLength);
            }
            return s;
        }

        protected String rtrim(String s) {
            while (s.substring(s.length() - 1).equals(" ")) {
                s = s.substring(0, s.length() - 1);
            }
            return s;
        }

        protected void addRawLine(String line) {
            if (lines == null) {
                lines = line;
            } else {
                lines += eol + line;
            }
        }

        protected void addTrimmedLine(String line) {
            addRawLine(trimLine(rtrim(line)));
        }

        protected void addMultiLine(String line) {
            if (line != null && !line.isEmpty()) {
                while (true) {
                    String l;
                    if (line.length() > lineLength) {
                        l = line.substring(0, lineLength);
                        line = line.substring(lineLength);
                    } else {
                        l = line;
                        line = null;
                    }
                    addRawLine(l);
                    if (line == null || line.isEmpty()) break;
                }
            }
        }

        protected void addLine(String line, int type) {
            if (line == null || line.isEmpty()) {
                addRawLine("");
            } else {
                if ((type & LINE_CODED) == LINE_CODED) {
                    line = RawPrinter.getInstance().getDriver().applyCode(line);
                    addRawLine(line);
                } else if ((type & LINE_PADDED) == LINE_PADDED) {
                    addTrimmedLine(padLeft(line, lineLength));
                } else if ((type & LINE_JUSTIFIED) == LINE_JUSTIFIED) {
                    int idx = line.indexOf(justifySep);
                    if (idx >= 0) {
                        String left = line.substring(0, idx + 1);
                        String right = line.substring(idx + 1);
                        line = padLine(left, lineLength - right.length()) +
                                right;
                    }
                    addTrimmedLine(line);
                } else if ((type & LINE_MULTI) == LINE_MULTI) {
                    addMultiLine(line);
                } else {
                    addTrimmedLine(line);
                }
            }
        }

        protected void addLine(String line) {
            addLine(line, LINE_NONE);
        }

        protected void doPrint() {
        }

        public void print(Invoice i) {
            inv = i;
            lines = null;
            doPrint();
            if (lines != null && !lines.isEmpty()) {
                RawPrinter.getInstance().print(lines);
            }
        }
    }

    public class BaseModel extends Model {

        protected final String fontType;

        public BaseModel(String n, String font) {
            super(n);
            fontType = font;
        }

        @Override
        protected void doPrint() {
            Store store = AppData.getInstance().getActiveStore();
            addLine(String.format("<c:reset><c:%s><c:boldOn>%s<c:boldOff>",
                    fontType, store.getNama()), LINE_CODED);
            addLine(store.getAlamat());
            addLine(store.getKota());
            addLine("Telepon: " + store.getTelepon());
            addLine("NPWP: " + store.getNpwp());
            addLine("");
            addLine(inv.getId() + " " +
                    AppUtil.formatDate(inv.getTanggal()) + " " +
                    (inv.getCreatedBy() != null ? inv.getCreatedBy() : ""));
            addLine(repeat("-", lineLength));
            for (Item it: inv.getItems()) {
                addLine(it.getKode() + " " + it.getNama());
                addLine(String.format("%4s x %8s = %10s",
                        AppUtil.formatNumber(it.getQty()),
                        AppUtil.formatNumber(it.getHarga()),
                        AppUtil.formatNumber(it.getTotal())), LINE_PADDED);
            }
            addLine(repeat("-", lineLength));
            addLine(String.format("Total: %10s",
                    AppUtil.formatNumber(inv.getTotal())), LINE_JUSTIFIED);
            addLine(String.format("Bayar: %10s",
                    AppUtil.formatNumber(inv.getBayar())), LINE_JUSTIFIED);
            addLine(String.format("Kembali: %10s",
                    AppUtil.formatNumber(inv.getKembali())), LINE_JUSTIFIED);
            addLine("");
            if (note != null && !note.isEmpty()) {
                addLine(note, LINE_MULTI);
                addLine("");
            }
            addLine("Terima kasih atas kunjungan anda.", LINE_MULTI);
            addLine("");
            addLine("");
            addLine("");
        }
    }

    public class Invoice58FontA extends BaseModel {

        public Invoice58FontA() {
            super("Invoice 58mm Font A", "fontA");
            lineLength = 32;
        }
    }

    public class Invoice58FontB extends BaseModel {

        public Invoice58FontB() {
            super("Invoice 58mm Font B", "fontB");
            lineLength = 40;
        }
    }
}
