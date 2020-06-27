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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import id.my.kasirq.model.User;
import id.my.kasirq.model.Store;
import id.my.kasirq.util.FontAwesome;
import id.my.kasirq.util.RawPrinter;

public class App {

    public static final String NAME = "KasirQ";
    private static App instance;

    private final List<Initializer> init = new ArrayList<>();
    private AppMain m;
    private AppData d;
    private Splash splash;
    private LoginDialog.Authenticator authenticator;    
    private final boolean secured = true;

    public App() {
        initialize();
    }

    private void initialize() {
        init.add(new Initializer(this, "Inisialisasi splash") {
            @Override
            protected void preInit() {
                app.splash = new Splash();
                Thread tr = new Thread(app.splash);
                tr.start();
            }
        });
        init.add(new Initializer(this, "Inisialisasi FontAwesome") {
            @Override
            protected void onInit() {
                FontAwesome.getInstance().registerFonts();
            }
        });
        init.add(new Initializer(this, "Inisialisasi data aplikasi") {
            @Override
            protected void onInit() {
                app.d = AppData.getInstance();
            }
        });
        init.add(new Initializer(this, "Memuat data konfigurasi") {
            @Override
            protected void onInit() {
                app.d.getConfig().load();
            }
        });
        init.add(new Initializer(this, "Inisialisasi user") {
            @Override
            protected void onInit() {
                app.d.getUser().load();
                if (app.d.getUser().isEmpty()) {
                    User u = new User();
                    u.setName("Administrator");
                    u.setUsername("admin");
                    u.setPlainPassword("admin");
                    u.audit(null);
                    if (app.d.getUser().add(u)) {
                        app.d.getUser().save();
                    }
                    JOptionPane.showMessageDialog(null, "Saat ini tidak ada " +
                            "data user, User default berhasil dibuat!",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        init.add(new Initializer(this, "Memuat data toko") {
            @Override
            protected void onInit() {
                app.d.getStore().load();
            }
        });
        init.add(new Initializer(this, "Memuat data barang") {
            @Override
            protected void onInit() {
                app.d.getProduct().load();
            }
        });
        init.add(new Initializer(this, "Memuat data penjualan") {
            @Override
            protected void onInit() {
                app.d.getInvoice().load();
            }
        });
        init.add(new Initializer(this, "Memuat data printer invoice") {
            @Override
            protected void onInit() {
                try {
                    String filename = "printers.json";
                    InputStream in;
                    File f = new File(filename);
                    if (f.exists()) {
                        in = new FileInputStream(f);
                    } else {
                        in = getClass().getResourceAsStream("/" + filename);
                    }
                    if (in != null) {
                        RawPrinter.getInstance().load(in);
                        app.log(String.format("%d printer berhasil dimuat",
                                RawPrinter.getInstance().getDrivers().size()));
                    }
                }
                catch (FileNotFoundException e) {
                }
            }
        });
        init.add(new Initializer(this, "Masuk ke aplikasi") {
            @Override
            protected void onInit() throws InitializerException {
                if (!LoginDialog.doLogin(getAuthenticator())) {
                    throw new InitializerException();
                }
            }
            @Override
            protected boolean isEnabled() {
                return secured;
            }
        });
        init.add(new Initializer(this, "Membuat form utama aplikasi") {
            @Override
            protected void onInit() {
                m = new AppMain();
                m.setImage(app.splash.getImage());
                m.updateTitle();
                m.setVisible(true);
            }
        });
        init.add(new Initializer(this, "Memeriksa pengaturan") {
            @Override
            protected void onInit() {
                String invPrinter = (String) app.d.getConfig()
                        .get(AppData.CFG_INVOICE_PRINTER);
                String invDriver = (String) app.d.getConfig()
                        .get(AppData.CFG_INVOICE_PRINTER_DRIVER);
                String invModel = (String) app.d.getConfig()
                        .get(AppData.CFG_INVOICE_MODEL);
                if (invPrinter == null || invDriver == null || invModel == null) {
                    JOptionPane.showMessageDialog(app.m,
                            "Pengaturan perlu dilakukan terlebih dahulu!",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    SetupDialog.getInstance().setVisible(true);
                }
            }
        });
        init.add(new Initializer(this, "Memeriksa konfigurasi toko") {
            @Override
            protected void onInit() {
                Store store = app.d.getActiveStore();
                if (store == null || store.getNama() == null) {
                    JOptionPane.showMessageDialog(app.m, "Toko perlu " +
                            "dikonfigurasi terlebih dahulu!", "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                    StoreDialog.getInstance().setVisible(true);
                }
            }
        });
        init.add(new Initializer(this, "Inisialisasi selesai") {
            @Override
            protected void onInit() {
                app.splash.setActive(false);
            }
        });
    }

    public Splash getSplash() {
        return splash;
    }

    public LoginDialog.Authenticator getAuthenticator() {
        if (authenticator == null) {
            authenticator = new LoginDialog.Authenticator() {
                @Override
                public boolean onAuthenticate(String username, String password) {
                    boolean authenticated = false;
                    for (User u: d.getUser().getObjects()) {
                        if (u.getUsername().equals(username) &&
                                u.getPlainPassword().equals(password))
                        {
                            d.setActiveUser(u);
                            authenticated = true;
                            break;
                        }
                    }
                    return authenticated;
                }
            };
        }
        return authenticator;
    }

    public void run() {
        for (Initializer i: init) {
            try {
                i.init();
            }
            catch (InitializerException e) {
                if (e.getMessage() != null) {
                    JOptionPane.showMessageDialog(null, e.getMessage(),
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void log(String message) {
        System.out.printf("%s: %s...\n", NAME, message);
    }
    
    private class Initializer {

        protected App app;
        protected String status;

        public Initializer(App a, String s) {
            app = a;
            status = s;
        }

        protected void preInit() throws InitializerException {
        }

        protected void onInit() throws InitializerException {
        }

        protected boolean isEnabled() {
            return true;
        }

        public void init() throws InitializerException {
            if (isEnabled()) {
                app.log(status);
                preInit();
                if (app.splash != null) {
                    app.splash.setStatus(status);
                }
                onInit();
            } else {
                app.log("!!! " + status);
            }
        }
    }

    private class InitializerException extends Exception {

        public InitializerException() {
            super();
        }

        public InitializerException(String message) {
            super(message);
        }
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException |
                ClassNotFoundException |
                InstantiationException |
                IllegalAccessException e) {
            // handle exception
        }
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.printf("%s (c) 1461900005 Toha\n" +
                "Fakultas Teknik Informatika\n" +
                "Universitas 17 Agustus 1945 Surabaya\n" +
                "------------------------------------\n\n", App.NAME);

        App.setLookAndFeel();

        App app = App.getInstance();
        app.log("Mulai");
        app.run();
        app.log("Ready");
    }
}
