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

public class Store extends Audited {

    protected Long id;
    protected String nama;
    protected String alamat;
    protected String kota;
    protected String telepon;
    protected String npwp;

    public Long getId() {
        return id;
    }

    public void setId(long value) {
        id = value;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String value) {
        nama = value;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String value) {
        alamat = value;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String value) {
        kota = value;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String value) {
        telepon = value;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String value) {
        npwp = value;
    }
}
