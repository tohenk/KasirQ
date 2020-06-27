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

import java.util.TreeMap;

import id.my.kasirq.collection.Json;
import id.my.kasirq.collection.JsonStorage;
import id.my.kasirq.model.Category;
import id.my.kasirq.model.Invoice;
import id.my.kasirq.model.Item;
import id.my.kasirq.model.Product;
import id.my.kasirq.model.Store;
import id.my.kasirq.model.User;
import id.my.kasirq.storage.StorageProp;

public class AppData {
    
    public static final String CFG_INVOICE_PRINTER = "invoice.printer";
    public static final String CFG_INVOICE_PRINTER_DRIVER = "invoice.printer.driver";
    public static final String CFG_INVOICE_MODEL = "invoice.model";
    public static final String CFG_INVOICE_NOTE = "invoice.note";

    private static final String DATA_CONFIG = "config.prop";
    private static final String DATA_STORE = "store.json";
    private static final String DATA_USER = "users.json";
    private static final String DATA_PRODUCT = "products.json";
    private static final String DATA_INVOICE = "sales.json";

    private static AppData instance;

    private StorageProp config;
    private JsonStorage<User> userStorage;
    private JsonStorage<Store> storeStorage;
    private JsonStorage<Product> productStorage;
    private JsonStorage<Invoice> invoiceStorage;
    private Json<Item> itemStorage;
    private final TreeMap<String, Category> categories = new TreeMap<>();
    private final TreeMap<String, Product> barcodes = new TreeMap<>();
    private Store activeStore = null;
    private User activeUser = null;

    public StorageProp getConfig() {
        if (config == null) {
            config = new StorageProp(DATA_CONFIG);
        }
        return config;
    }

    public JsonStorage<User> getUser() {
        if (userStorage == null) {
            userStorage = new JsonStorage<User>(User.class, "username");
            userStorage.setStoragePath(DATA_USER);
        }
        return userStorage;
    }

    public JsonStorage<Store> getStore() {
        if (storeStorage == null) {
            storeStorage = new JsonStorage<Store>(Store.class, "id") {
                @Override
                protected void onAdd(Store s) {
                    if (activeStore == null) {
                        activeStore = s;
                    }
                }
            };
            storeStorage.setStoragePath(DATA_STORE);
        }
        return storeStorage;
    }

    public JsonStorage<Product> getProduct() {
        if (productStorage == null) {
            productStorage = new JsonStorage<Product>(Product.class, "kode") {
                @Override
                protected void onAdd(Product p) {
                    addCategoryFromProduct(p);
                    addBarcodeFromProduct(p);
                }

                @Override
                protected void onRemove(Product p) {
                    removeBarcodeFromProduct(p);
                }
            };
            productStorage.setStoragePath(DATA_PRODUCT);
        }
        return productStorage;
    }
    
    public JsonStorage<Invoice> getInvoice() {
        if (invoiceStorage == null) {
            invoiceStorage = new JsonStorage<Invoice>(Invoice.class, "id") {
                @Override
                protected Json getModel(String name) {
                    if (itemStorage == null) {
                        itemStorage = new Json<>(Item.class, "kode");
                    }
                    return itemStorage;
                }
            };
            invoiceStorage.setStoragePath(DATA_INVOICE);
        }
        return invoiceStorage;
    }

    public TreeMap<String, Category> getCategories() {
        return categories;
    }

    public TreeMap<String, Product> getBarcodes() {
        return barcodes;
    }
    
    public Store getActiveStore() {
        return activeStore;
    }
    
    public void setActiveStore(Store store) {
        activeStore = store;
    }

    public User getActiveUser() {
        return activeUser;
    }
    
    public void setActiveUser(User user) {
        activeUser = user;
    }
    
    public void addCategoryFromProduct(Product p) {
        if (p.getKategori() != null && !p.getKategori().isEmpty()) {
            String k = p.getKategori().trim().toLowerCase();
            if (!categories.containsKey(k)) {
                categories.put(k, new Category(p.getKategori()));
            }
        }
    }

    public void addBarcodeFromProduct(Product p) {
        if (p.getBarcode() != null && !p.getBarcode().isEmpty()) {
            barcodes.put(p.getBarcode(), p);
        }
    }
    
    public void removeBarcodeFromProduct(Product p) {
        if (p.getBarcode() != null && !p.getBarcode().isEmpty() &&
                barcodes.containsKey(p.getBarcode())) {
            barcodes.remove(p.getBarcode());
        }
    }

    public Object getNewId(Object o) {
        Object res = null;
        if (o instanceof Store) {
            Store last = getStore().getLast();
            res = last != null ? last.getId() + 1 : 1L;
        }
        if (o instanceof Invoice) {
            Invoice last = getInvoice().getLast();
            res = last != null ? last.getId() + 1 : 1L;
        }
        return res;
    }

    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }
}
