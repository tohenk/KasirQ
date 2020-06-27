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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DateFormatter;

public class AppUtil {

    public static final String FORMTAT_DATETIME = "dd-MM-yyyy HH:mm:ss";

    private static DefaultFormatterFactory numberFactory;
    private static final HashMap<String, DefaultFormatterFactory>
            dateFactories = new HashMap<>();

    private static DefaultFormatterFactory getNumberFactory() {
        if (numberFactory == null) {
            DecimalFormat df = new DecimalFormat();
            NumberFormatter nf = new NumberFormatter(df);
            numberFactory = new DefaultFormatterFactory(nf);
        }
        return numberFactory;
    }

    private static DefaultFormatterFactory getDateTimeFactory(String format) {
        if (!dateFactories.containsKey(format)) {
            SimpleDateFormat f = new SimpleDateFormat(format);
            DateFormatter df = new DateFormatter(f);
            DefaultFormatterFactory dateFactory = new DefaultFormatterFactory(df);
            dateFactories.put(format, dateFactory);
        }
        return dateFactories.get(format);
    }

    public static void applyNumberFormatter(JFormattedTextField ftf) {
        ftf.setFormatterFactory(getNumberFactory());
    }

    public static void applyDateFormatter(JFormattedTextField ftf) {
        ftf.setFormatterFactory(getDateTimeFactory(FORMTAT_DATETIME));
    }

    public static void applyDateFormatter(JFormattedTextField ftf, String format) {
        ftf.setFormatterFactory(getDateTimeFactory(format));
    }

    public static String formatNumber(Number value) {
        try {
            return getNumberFactory().getDefaultFormatter().valueToString(value);
        }
        catch (Exception e) {
        }
        return null;
    }

    public static Number getNumber(String value) {
        try {
            return (Number) getNumberFactory().getDefaultFormatter()
                    .stringToValue(value);
        }
        catch (Exception e) {
        }
        return null;
    }

    public static String formatDate(Date value) {
        return formatDate(value, FORMTAT_DATETIME);
    }

    public static String formatDate(Date value, String format) {
        try {
            return getDateTimeFactory(format).getDefaultFormatter()
                    .valueToString(value);
        }
        catch (Exception e) {
        }
        return null;
    }
}
