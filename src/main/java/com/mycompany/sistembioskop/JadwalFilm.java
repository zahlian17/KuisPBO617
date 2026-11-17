package com.mycompany.sistembioskop;

public class JadwalFilm extends ObjekBioskop {
    private static final long serialVersionUID = 1L;

    public JadwalFilm(String kode, String nama, int kuota) {
        super(kode, nama, kuota);
    }

    @Override
    public String tampilkanInfo() {
        return getKode() + " | " + getNama() + " | Sisa kuota: " + getKuota();
    }
}
