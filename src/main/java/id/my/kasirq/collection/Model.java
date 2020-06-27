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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Model<T> {

    protected final Class modelClass;
    protected final String modelKey;
    protected final TreeMap<Object, T> items = new TreeMap<>();
    protected final Map<String, Field> fields = new LinkedHashMap<>();
    protected final Map<String, Method> getter = new HashMap<>();
    protected final Map<String, Method> setter = new HashMap<>();
    protected final Map<String, Method> listAdd = new HashMap<>();

    public Model(Class c, String key) {
        modelClass = c;
        modelKey = key;
    }
    
    public Set<Object> getKeys() {
        return items.keySet();
    }

    public Collection<T> getObjects() {
        return items.values();
    }

    public T get(Object key) {
        return items.get(key);
    }

    protected T createModel() {
        T model = null;
        try {
            model = (T) modelClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    protected void getFields(Class c) {
        HashMap<String, Method> methods = new HashMap<>();
        for (Method m: c.getDeclaredMethods()) {
            methods.put(m.getName(), m);
        }
        for (Field f: c.getDeclaredFields()) {
            String methodName;
            String name = f.getName();
            String method = name.substring(0, 1).toUpperCase() +
                    name.substring(1);
            int count = 0;
            methodName = "get" + method;
            if (methods.containsKey(methodName)) {
                getter.put(name, methods.get(methodName));
                count++;
            }
            methodName = "set" + method;
            if (methods.containsKey(methodName)) {
                setter.put(name, methods.get(methodName));
                count++;
            }
            methodName = "add" + method.substring(0, method.length() - 1);
            if (methods.containsKey(methodName)) {
                listAdd.put(name, methods.get(methodName));
                count++;
            }
            if (count > 0) fields.put(name, f);
        }
        if (c.getSuperclass() != null) {
            getFields(c.getSuperclass());
        }
    }

    protected void getModelFields(T model) {
        if (fields.isEmpty()) {
            getFields(model.getClass());
        }
    }

    protected Object getKeyValue(T o) {
        getModelFields(o);
        if (!modelKey.isEmpty() && getter.containsKey(modelKey)) {
            try {
                return getter.get(modelKey).invoke(o, (Object[]) null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean has(T o) {
        Object key = getKeyValue(o);
        return key != null && items.containsKey(key);
    }

    public boolean add(T o) {
        Object key = getKeyValue(o);
        if (key != null && !items.containsKey(key)) {
            items.put(key, o);
            onAdd(o);
            return true;
        }
        return false;
    }

    public boolean addOrUpdate(T o) {
        return has(o) || add(o);
    }

    public boolean remove(T o) {
        Object key = getKeyValue(o);
        if (key != null && items.containsKey(key)) {
            onRemove(items.get(key));
            items.remove(key);
            return true;
        }
        return false;
    }

    protected void onAdd(T o) {
    }

    protected void onRemove(T o) {
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public T getFirst() {
        return !items.isEmpty() ? items.get(items.firstKey()) : null;
    }

    public T getLast() {
        return !items.isEmpty() ? items.get(items.lastKey()) : null;
    }
}