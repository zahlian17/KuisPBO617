package com.mycompany.sistembioskop;

import java.io.Serializable;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private Penonton penonton;
    private JadwalFilm jadwalFilm;
    private int jumlah;

    public Transaksi(Penonton penonton, JadwalFilm jadwalFilm, int jumlah) {
        if (penonton == null || jadwalFilm == null) {
            throw new IllegalArgumentException("Penonton dan jadwal film wajib tersedia.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah transaksi harus lebih dari 0.");
        }
        this.penonton = penonton;
        this.jadwalFilm = jadwalFilm;
        this.jumlah = jumlah;
    }

    public Penonton getPenonton() {
        return penonton;
    }

    public JadwalFilm getJadwalFilm() {
        return jadwalFilm;
    }

    public int getJumlah() {
        return jumlah;
    }

    @Override
    public String toString() {
        return penonton.getId() + " | " + jadwalFilm.getKode() + " | Jumlah: " + jumlah;
    }
}
