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

package id.my.kasirq.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StorageJson extends Storage {
    
    public static final int JSON_OBJECT = 1;
    public static final int JSON_ARRAY = 2;

    private JsonReader reader;
    private JsonWriter writer;
    private int dataType = JSON_ARRAY;

    public StorageJson(String storagePath) {
        super(storagePath);
    }
    
    public int getDataType() {
        return dataType;
    }

    public void setDataType(int type) {
        dataType = type;
    }
    
    protected JsonReader getReader() {
        return reader;
    }

    protected JsonWriter getWriter() {
        return writer;
    }
    
    protected void onObjectLoad(JsonReader r) throws IOException {
    }

    protected void onObjectSave(JsonWriter w) throws IOException {
    }

    @Override
    protected void onStorageLoad(FileInputStream in) throws IOException {
        reader = new JsonReader(new InputStreamReader(in));
    }

    @Override
    protected void onStorageSave(FileOutputStream out) throws IOException {
        writer = new JsonWriter(new OutputStreamWriter(out));
    }

    @Override
    protected void onStorageLoaded() {
        try {
            JsonReader r = getReader();
            if (dataType == JSON_OBJECT) {
                r.beginObject();
                onObjectLoad(r);
                r.endObject();
            } else {
                r.beginArray();
                while (r.hasNext()) {
                    onObjectLoad(r);
                }
                r.endArray();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStorageSaved() {
        try {
            JsonWriter w = getWriter();
            if (dataType == JSON_OBJECT) {
                w.beginObject();
                onObjectSave(w);
                w.endObject();
            } else {
                w.beginArray();
                onObjectSave(w);
                w.endArray();
            }
            w.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}