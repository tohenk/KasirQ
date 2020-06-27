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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobAdapter;

import com.google.gson.stream.JsonReader;

public class RawPrinter {

    private static RawPrinter instance;

    private final Map<Integer, Driver> drivers = new HashMap<>();
    private PrintService ps = null;
    private Driver drv = null;

    public PrintService getPrintService() {
        return ps;
    }

    public void setPrintService(String name) {
        for (PrintService p:
                PrintServiceLookup.lookupPrintServices(null, null)) {
            if (p.getName().equals(name)) {
                ps = p;
                break;
            }
        }
    }

    public Collection<Driver> getDrivers() {
        return drivers.values();
    }

    public Driver getDriver() {
        return drv;
    }

    public void setDriver(String name) {
        for (Driver d: getDrivers()) {
            if (d.getName().equals(name)) {
                drv = d;
                break;
            }
        }
    }

    public void print(String s) {
        System.out.println(s);
        if (ps != null && drv != null) {
            InputStream is = new ByteArrayInputStream(s.getBytes());
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

            // Create the print job
            DocPrintJob job = ps.createPrintJob();
            Doc doc = new SimpleDoc(is, flavor, null);

            PrintJobWatcher watcher = new PrintJobWatcher(job);

            // Print it
            try {
                job.print(doc, null);
                watcher.waitForDone();
                is.close();
            }
            catch (IOException | PrintException e) {
                e.printStackTrace();
            }
        }
    }

    private void addDriver(JsonReader r) throws IOException {
        Driver d = new Driver();
        r.beginObject();
        while (r.hasNext()) {
            String key = r.nextName();
            switch (key) {
                case "id":
                    d.setId(r.nextInt());
                    break;
                case "name":
                    d.setName(r.nextString());
                    break;
                case "parent":
                    int parent = r.nextInt();
                    if (drivers.containsKey(parent)) {
                        d.addCodes(drivers.get(parent).getCodes());
                    }
                    break;
                case "codes":
                    r.beginObject();
                    while (r.hasNext()) {
                        String codeName = r.nextName();
                        String codeValue = r.nextString();
                        if (!codeValue.isEmpty()) {
                            int len = codeValue.length() / 2;
                            byte[] bytes = new byte[len];
                            for (int i = 0; i < len; i++) {
                                String h = codeValue.substring(i * 2, (i + 1) * 2);
                                bytes[i] = (byte) (0xff & Integer.decode("0x" + h));
                            }
                            codeValue = new String(bytes);
                        }
                        d.getCodes().put(codeName, codeValue);
                    }
                    r.endObject();
                    break;
            }
        }
        r.endObject();
        drivers.put(d.getId(), d);
    }

    public void load(InputStream in) {
        JsonReader r = new JsonReader(new InputStreamReader(in));
        try {
            r.beginArray();
            while (r.hasNext()) {
                addDriver(r);
            }
            r.endArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static RawPrinter getInstance() {
        if (instance == null) {
            instance = new RawPrinter();
        }
        return instance;
    }

    public class Driver {

        private int id;
        private String name;
        private final Map<String, String> codes = new HashMap<>();

        public int getId() {
            return id;
        }
        
        public void setId(int value) {
            id = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            name = value;
        }

        public Map<String, String> getCodes() {
            return codes;
        }

        public void addCodes(Map<String, String> values) {
            for (String k: values.keySet()) {
                if (!codes.containsKey(k)) {
                    codes.put(k, values.get(k));
                }
            }
        }

        public String applyCode(String s) {
            Pattern p = Pattern.compile("\\<c\\:([^0-9][a-zA-Z0-9]+)\\>");
            while (true) {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    String match = m.group(0);
                    String code = m.group(1);
                    String value = codes.containsKey(code) ? codes.get(code) : null;
                    s = s.replace(match, value != null ? value : "");
                } else {
                    break;
                }
            }
            return s;
        }
    }

    // http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/DeterminingWhenaPrintJobHasFinished.htm

    class PrintJobWatcher {

        boolean done = false;

        PrintJobWatcher(DocPrintJob job) {
            job.addPrintJobListener(new PrintJobAdapter() {
                @Override
                public void printJobCanceled(PrintJobEvent pje) {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }

                @Override
                public void printJobCompleted(PrintJobEvent pje) {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }

                @Override
                public void printJobFailed(PrintJobEvent pje) {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }

                @Override
                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }
            });
        }

        public synchronized void waitForDone() {
            try {
                while (!done) {
                    wait();
                }
            }
            catch (InterruptedException e) {
            }
        }
    }
}
