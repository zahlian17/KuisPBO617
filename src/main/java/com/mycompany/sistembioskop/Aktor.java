package com.mycompany.sistembioskop;

import java.io.Serializable;

public abstract class Aktor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;

    public Aktor(String id, String nama) {
        setId(id);
        setNama(nama);
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID aktor tidak boleh kosong.");
        }
        this.id = id.trim();
    }

    public String getNama() {
        return nama;
    }

    public final void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama aktor tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public abstract String tampilkanInfo();
}

