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

package id.my.kasirq.model;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;

public class Invoice extends Audited {
    
    public static final int NEW = 1;
    public static final int DRAFT = 2;
    public static final int PAID = 3;
    public static final int PRINTED = 4;

    protected Long id;
    protected Date tanggal;
    protected Integer status = NEW;
    protected Double harga;
    protected Double ppn;
    protected Double total;
    protected Double bayar;
    protected Double kembali;
    protected List<Item> items = new LinkedList<>();

    public Long getId() {
        return id;
    }

    public void setId(long value) {
        id = value;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date value) {
        tanggal = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer value) {
        status = value;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double value) {
        harga = value;
    }

    public Double getPpn() {
        return ppn;
    }

    public void setPpn(Double value) {
        ppn = value;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double value) {
        total = value;
    }

    public Double getBayar() {
        return bayar;
    }

    public void setBayar(Double value) {
        bayar = value;
    }

    public Double getKembali() {
        return kembali;
    }

    public void setKembali(Double value) {
        kembali = value;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item i) {
        items.add(i);
    }
}
