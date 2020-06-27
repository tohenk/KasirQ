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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import id.my.kasirq.storage.Storage;
import id.my.kasirq.storage.StorageJson;

public class JsonStorage<T> extends Json<T> {

    private String storagePath;
    private Storage storage = null;

    public JsonStorage(Class c, String key) {
        super(c, key);
    }

    public void setStoragePath(String path) {
        storagePath = path;
    }

    public Storage getStorage() {
        if (storage == null) {
            storage = createJsonStorage();
        }
        return storage;
    }

    private StorageJson createJsonStorage() {
        return new StorageJson(storagePath) {
            @Override
            protected void onObjectLoad(JsonReader r) throws IOException {
                objectFromJson(r);
            }

            @Override
            protected void onObjectSave(JsonWriter w) throws IOException {
                objectToJson(w);
            }
        };
    }

    public void load() {
        getStorage().load();
    }
    
    public void save() {
        getStorage().save();
    }
}
