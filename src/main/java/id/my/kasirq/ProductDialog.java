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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import id.my.kasirq.model.Category;
import id.my.kasirq.model.Product;
import id.my.kasirq.util.IconLoader;

public class ProductDialog extends JDialog implements ActionListener,
        PropertyChangeListener {

    private static ProductDialog instance;
    
    private String kode;
    private Product pr = new Product();
    private boolean updating = true;
    private boolean isNew = false;
    private boolean isModified = false;

    public ProductDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initialize();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Kode");

        jComboBox1.setEditable(true);
        jComboBox1.setActionCommand("PR_KODE");

        jButton1.setActionCommand("BROWSE");
        jButton1.setFocusable(false);

        jLabel2.setText("Nama");

        jTextField1.setActionCommand("PR_NAMA");

        jLabel3.setText("Kategori");

        jComboBox2.setEditable(true);
        jComboBox2.setActionCommand("PR_KATEGORI");

        jLabel4.setText("Barcode");

        jTextField2.setActionCommand("PR_BARCODE");

        jLabel5.setText("Harga");

        jLabel6.setText("PPN");

        jLabel7.setText("Harga Total");

        jButton2.setText("Simpan");
        jButton2.setActionCommand("SAVE");

        jButton3.setText("Batal");
        jButton3.setActionCommand("CANCEL");

        jButton4.setText("Hapus");
        jButton4.setActionCommand("DELETE");

        jButton5.setText("Tutup");
        jButton5.setActionCommand("CLOSE");

        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jFormattedTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                        .addComponent(jButton5))
                    .addComponent(jFormattedTextField2)
                    .addComponent(jFormattedTextField1)
                    .addComponent(jFormattedTextField3)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField1))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)
                        .addComponent(jButton4)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initialize() {
        setTitle("Produk");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        Dimension sz = jComboBox1.getSize();
        Dimension bsz = new Dimension(sz.height, sz.height);
        jButton1.setSize(bsz);
        jButton1.setPreferredSize(bsz);
        IconLoader iconLoader = IconLoader.getInstance();
        iconLoader.register(this, "BROWSE", "folder-open");
        iconLoader.register(this, "SAVE", "save");
        iconLoader.register(this, "CANCEL", "ban");
        iconLoader.register(this, "DELETE", "trash");
        iconLoader.register(this, "CLOSE", "times-circle");
        iconLoader.setAppIcon(this);
        AppUtil.applyNumberFormatter(jFormattedTextField1);
        AppUtil.applyNumberFormatter(jFormattedTextField2);
        AppUtil.applyNumberFormatter(jFormattedTextField3);
        for (Field f: ProductDialog.class.getDeclaredFields()) {
            try {
                Object o = f.get(this);
                if (o instanceof JButton) {
                    ((JButton) o).addActionListener(this);
                    iconLoader.setIcon(this, (JButton) o);
                }
                if (o instanceof JTextField) {
                    ((JTextField) o).addActionListener(this);
                }
                if (o instanceof JComboBox) {
                    ((JComboBox) o).addActionListener(this);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fillKeys();
                fillCategories();
                displayLastProduct();
            }
        });
        jFormattedTextField1.addPropertyChangeListener("value", this);
        jFormattedTextField2.addPropertyChangeListener("value", this);
        jFormattedTextField3.addPropertyChangeListener("value", this);
    }

    private void fillKeys() {
        jComboBox1.removeAllItems();
        for (Product p: AppData.getInstance().getProduct().getObjects()) {
            jComboBox1.addItem(p.getKode());
        }
    }

    private void fillCategories() {
        jComboBox2.removeAllItems();
        for (Category cat: AppData.getInstance().getCategories().values()) {
            jComboBox2.addItem(cat.getNama());
        }
    }
    
    private void addComboItem(JComboBox<String> combo, String value) {
        for (int i = 0; i < combo.getModel().getSize(); i++) {
            if (combo.getModel().getElementAt(i).equals(value)) {
                return;
            }
        }
        combo.addItem(value);
    }

    private void removeComboItem(JComboBox<String> combo, String value) {
        for (int i = 0; i < combo.getModel().getSize(); i++) {
            if (combo.getModel().getElementAt(i).equals(value)) {
                combo.removeItemAt(i);
                return;
            }
        }
    }

    private void displayProduct() {
        updating = true;
        try {
            jComboBox1.setSelectedItem(pr.getKode());
            jTextField1.setText(pr.getNama());
            jComboBox2.setSelectedItem(pr.getKategori());
            jTextField2.setText(pr.getBarcode());
            jFormattedTextField1.setValue(pr.getHarga());
            jFormattedTextField2.setValue(pr.getPpn());
            jFormattedTextField3.setValue(pr.getTotal());
            if (!isNew) {
                addComboItem(jComboBox1, pr.getKode());
            }
            if (pr.getKategori() != null) {
                addComboItem(jComboBox2, pr.getKategori());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        updating = false;
        updateStates();
    }
    
    private void displayLastProduct() {
        if (jComboBox1.getModel().getSize() > 0) {
            setProduct(jComboBox1.getModel()
                    .getElementAt(jComboBox1.getModel().getSize() - 1));
        }
    }

    private void updateStates() {
        jButton2.setEnabled(isNew || isModified);
        jButton3.setEnabled(isNew || isModified);
        jButton4.setEnabled(!isNew);
    }

    private void changeStates() {
        if (!updating && !isModified) {
            isModified = true;
            updateStates();
        }
    }

    private void setProduct(String k) {
        kode = k;
        isModified = false;
        AppData d = AppData.getInstance();
        pr = d.getProduct().get(kode);
        isNew = pr == null;
        if (isNew) {
            pr = new Product();
            pr.setKode(kode);
        }
        displayProduct();
    }

    private boolean checkKode() {
        String k = (String) jComboBox1.getSelectedItem();
        if (k == null || k.isEmpty()) {
            jComboBox1.requestFocus();
            return false;
        }
        if (!k.equals(kode)) {
            setProduct(k);
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppData d = AppData.getInstance();
        if (e.getActionCommand().equals("SAVE")) {
            if (!checkKode()) return;
            String nama = jTextField1.getText();
            String kategori = (String) jComboBox2.getSelectedItem();
            String barcode = jTextField2.getText();
            String harga = jFormattedTextField1.getText();
            String ppn = jFormattedTextField2.getText();
            String total = jFormattedTextField3.getText();
            if (nama.isEmpty()) {
                jTextField1.requestFocus();
                return;
            }
            if (kategori == null || kategori.isEmpty()) {
                jComboBox2.requestFocus();
                return;
            }
            if (harga.isEmpty()) {
                jFormattedTextField1.requestFocus();
                return;
            }
            if (ppn.isEmpty()) {
                jFormattedTextField2.requestFocus();
                return;
            }
            if (total.isEmpty()) {
                jFormattedTextField3.requestFocus();
                return;
            }
            // save
            pr.setNama(nama);
            pr.setKategori(kategori);
            pr.setBarcode(barcode);
            pr.setHarga(((Number) jFormattedTextField1.getValue())
                    .doubleValue());
            pr.setPpn(((Number) jFormattedTextField2.getValue())
                    .doubleValue());
            pr.setTotal(((Number) jFormattedTextField3.getValue())
                    .doubleValue());
            if (d.getProduct().addOrUpdate(pr)) {
                pr.audit(d.getActiveUser() != null ?
                        d.getActiveUser().getName() : null);
                d.getProduct().save();
                isNew = false;
                isModified = false;
                JOptionPane.showMessageDialog(this, "Produk berhasil disimpan.",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                displayProduct();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Produk tidak dapat disimpan!", "Kesalahan",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("CANCEL")) {
            isModified = false;
            displayProduct();
        }
        if (e.getActionCommand().equals("DELETE")) {
            if (JOptionPane.showConfirmDialog(this,
                    String.format("Apakah akan menghapus %s %s?", pr.getKode(),
                            pr.getNama()),
                    "Konfirmasi", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                if (d.getProduct().remove(pr)) {
                    d.getProduct().save();
                    removeComboItem(jComboBox1, pr.getKode());
                    displayLastProduct();
                }
            }
        }
        if (e.getActionCommand().equals("CLOSE")) {
            setVisible(false);
        }
        if (e.getActionCommand().equals("PR_KODE")) {
            if (jComboBox1.getSelectedItem() != null) {
                if (checkKode()) {
                    KeyboardFocusManager manager = KeyboardFocusManager
                            .getCurrentKeyboardFocusManager();
                    manager.focusNextComponent();
                }
            }
        }
        // fire state changes
        switch (e.getActionCommand()) {
            case "PR_NAMA":
            case "PR_KATEGORI":
            case "PR_BARCODE":
                changeStates();
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        double harga, ppn;
        if (e.getSource() == jFormattedTextField1) {
            if (jFormattedTextField1.getValue() != null) {
                harga = ((Number) jFormattedTextField1.getValue()).doubleValue();
                ppn = harga * 0.1;
                jFormattedTextField2.setValue(ppn);
            }
        }
        if (e.getSource() == jFormattedTextField2) {
            if (jFormattedTextField1.getValue() != null &&
                    jFormattedTextField2.getValue() != null) {
                harga = ((Number) jFormattedTextField1.getValue()).doubleValue();
                ppn = ((Number) jFormattedTextField2.getValue()).doubleValue();
                jFormattedTextField3.setValue(harga + ppn);
            }
        }
        changeStates();
    }

    public static ProductDialog getInstance() {
        if (instance == null) {
            instance = new ProductDialog(new JFrame(), true);
        }
        return instance;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}