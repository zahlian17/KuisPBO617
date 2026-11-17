package com.mycompany.sistembioskop;

public class Penonton extends Aktor {
    private static final long serialVersionUID = 1L;

    public Penonton(String id, String nama) {
        super(id, nama);
    }

    @Override
    public String tampilkanInfo() {
        return "ID: " + getId() + " | Nama: " + getNama();
    }
}
