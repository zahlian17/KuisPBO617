package com.mycompany.sistembioskop;

import java.io.Serializable;

public abstract class ObjekBioskop implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int kuota;

    public ObjekBioskop(String kode, String nama, int kuota) {
        setKode(kode);
        setNama(nama);
        setKuota(kuota);
    }

    public String getKode() {
        return kode;
    }

    public final void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode objek tidak boleh kosong.");
        }
        this.kode = kode.trim();
    }

    public String getNama() {
        return nama;
    }

    public final void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama objek tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public int getKuota() {
        return kuota;
    }

    public final void setKuota(int kuota) {
        if (kuota < 0) {
            throw new IllegalArgumentException("Kuota tidak boleh negatif.");
        }
        this.kuota = kuota;
    }

    public void kurangiKuota(int jumlah) throws TransaksiException {
        if (jumlah <= 0) {
            throw new TransaksiException("Jumlah transaksi harus lebih dari 0.");
        }
        if (jumlah > kuota) {
            throw new TransaksiException("Kuota " + kode + " tidak mencukupi. Sisa kuota: " + kuota);
        }
        kuota -= jumlah;
    }

    public abstract String tampilkanInfo();
}
