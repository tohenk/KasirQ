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

package id.my.kasirq.collection;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Json<T> extends Model<T> {
    
    public Json(Class c, String key) {
        super(c, key);
    }

    protected Json getModel(String name) {
        return null;
    }

    protected void readObject(JsonReader r, T o) throws IOException {
        r.beginObject();
        while (r.hasNext()) {
            String name = r.nextName();
            Object value;
            if (fields.containsKey(name)) {
                String type = fields.get(name).getType().getName();
                try {
                    value = getter.get(name).invoke(o, (Object[]) null);
                    if (value instanceof List && listAdd.containsKey(name)) {
                        Json m = getModel(name);
                        if (m != null) {
                            m.clear();
                            r.beginArray();
                            while (r.hasNext()) {
                                m.objectFromJson(r);
                            }
                            r.endArray();
                            for (Object c: m.getObjects()) {
                                listAdd.get(name).invoke(o, c);
                            }
                            continue;
                        }
                    } else if (type.equals(Date.class.getName())) {
                        value = new Date(r.nextLong());
                    } else if (type.equals(Double.class.getName())) {
                        value = r.nextDouble();
                    } else if (type.equals(Long.class.getName())) {
                        value = r.nextLong();
                    } else if (type.equals(Integer.class.getName())) {
                        value = r.nextInt();
                    } else if (type.equals(Boolean.class.getName())) {
                        value = r.nextBoolean();
                    } else {
                        value = r.nextString();
                    }
                    setter.get(name).invoke(o, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                r.skipValue();
            }
        }
        r.endObject();
    }

    protected void objectFromJson(JsonReader r) throws IOException {
        T o = createModel();
        getModelFields(o);
        readObject(r, o);
        add(o);
    }
    
    protected void writeObject(JsonWriter w, T o) throws IOException {
        w.beginObject();
        for (String name: fields.keySet()) {
            String type = fields.get(name).getType().getName();
            try {
                Object value = getter.get(name).invoke(o, (Object[]) null);
                if (value == null || (value instanceof String &&
                        ((String) value).isEmpty())) {
                    continue;
                }
                w.name(name);
                if (value instanceof List) {
                    Json m = getModel(name);
                    if (m != null) {
                        Iterator it = ((List) value).iterator();
                        w.beginArray();
                        while (it.hasNext()) {
                            m.writeObject(w, it.next());
                        }
                        w.endArray();
                    }
                } else if (type.equals(Date.class.getName())) {
                    w.value(((Date) value).getTime());
                } else if (type.equals(Double.class.getName())) {
                    w.value((Double) value);
                } else if (type.equals(Long.class.getName())) {
                    w.value((Long) value);
                } else if (type.equals(Integer.class.getName())) {
                    w.value((Integer) value);
                } else if (type.equals(Boolean.class.getName())) {
                    w.value((Boolean) value);
                } else {
                    w.value((String) value);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        w.endObject();
    }

    protected void objectToJson(JsonWriter w) throws IOException {
        for (T o: items.values()) {
            writeObject(w, o);
        }
    }
}
