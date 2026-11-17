package com.mycompany.sistembioskop;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SistemBioskop implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaBioskop;
    private HashMap<String, Aktor> daftarPenonton;
    private HashMap<String, ObjekBioskop> daftarJadwalFilm;
    private ArrayList<Transaksi> daftarTransaksi;

    public SistemBioskop(String namaBioskop) {
        if (namaBioskop == null || namaBioskop.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama bioskop tidak boleh kosong.");
        }
        this.namaBioskop = namaBioskop;
        this.daftarPenonton = new HashMap<>();
        this.daftarJadwalFilm = new HashMap<>();
        this.daftarTransaksi = new ArrayList<>();
    }

    public void tambahPenonton(String id, String nama) {
        if (daftarPenonton.containsKey(id)) {
            throw new IllegalArgumentException("ID penonton sudah terdaftar: " + id);
        }
        Penonton penonton = new Penonton(id, nama);
        daftarPenonton.put(penonton.getId(), penonton);
    }

    public void tambahJadwalFilm(String kode, String nama, int kuota) {
        if (daftarJadwalFilm.containsKey(kode)) {
            throw new IllegalArgumentException("Kode jadwal sudah terdaftar: " + kode);
        }
        JadwalFilm jadwal = new JadwalFilm(kode, nama, kuota);
        daftarJadwalFilm.put(jadwal.getKode(), jadwal);
    }

    public void prosesTransaksi(String idPenonton, String kodeFilm, int jumlah)
            throws TransaksiException {
        Aktor aktor = daftarPenonton.get(idPenonton);
        if (aktor == null) {
            throw new TransaksiException("ID penonton tidak ditemukan: " + idPenonton);
        }

        ObjekBioskop objek = daftarJadwalFilm.get(kodeFilm);
        if (objek == null) {
            throw new TransaksiException("Kode jadwal film tidak ditemukan: " + kodeFilm);
        }

        if (!(aktor instanceof Penonton) || !(objek instanceof JadwalFilm)) {
            throw new TransaksiException("Jenis data transaksi tidak sesuai.");
        }

        JadwalFilm jadwal = (JadwalFilm) objek;
        jadwal.kurangiKuota(jumlah);

        Transaksi transaksi = new Transaksi((Penonton) aktor, jadwal, jumlah);
        daftarTransaksi.add(transaksi);

        System.out.println("[TRANSAKSI BERHASIL] " + transaksi);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        System.out.println("Bioskop: " + namaBioskop);

        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            StringBuilder isiFile = new StringBuilder();
            String baris;

            while ((baris = reader.readLine()) != null) {
                isiFile.append(baris);
            }

            String data = isiFile.toString().trim();
            if (data.isEmpty()) {
                throw new IOException("File transaksi kosong.");
            }

            // Digit kedua = 1: setiap transaksi dipisahkan tanda pipe (|).
            String[] recordTransaksi = data.split("\\|");

            for (int i = 0; i < recordTransaksi.length; i++) {
                String record = recordTransaksi[i].trim();
                if (record.isEmpty()) {
                    continue;
                }

                try {
                    String[] bagian = record.split("/");
                    String id = null;
                    String kode = null;
                    String jumlahTeks = null;

                    for (String item : bagian) {
                        String nilai = item.trim();
                        int pemisah = nilai.indexOf('-');
                        if (pemisah <= 0 || pemisah == nilai.length() - 1) {
                            throw new IllegalArgumentException("Format bagian data salah: " + nilai);
                        }

                        String kunci = nilai.substring(0, pemisah).trim();
                        String isi = nilai.substring(pemisah + 1).trim();

                        switch (kunci) {
                            case "ID":
                                id = isi;
                                break;
                            case "KD":
                                kode = isi;
                                break;
                            case "JML":
                                jumlahTeks = isi;
                                break;
                            case "":
                                break;
                            default:
                                throw new IllegalArgumentException("Penanda tidak dikenal: " + kunci);
                        }
                    }

                    if (id == null || kode == null || jumlahTeks == null) {
                        throw new IllegalArgumentException("Data ID, KD, atau JML belum lengkap.");
                    }

                    int jumlah = Integer.parseInt(jumlahTeks);
                    prosesTransaksi(id, kode, jumlah);

                } catch (TransaksiException | IllegalArgumentException e) {
                    System.out.println("[TRANSAKSI DITOLAK] " + e.getMessage());
                }
            }
        }
    }

    public void simpanLaporanAkhir() {
        // Object Serialization: simpan seluruh keadaan sistem ke file biner.
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream("sistem_state.ser"))) {
            output.writeObject(this);
            System.out.println("[SERIALIZATION BERHASIL] sistem_state.ser");
        } catch (IOException e) {
            System.out.println("[GAGAL SERIALIZATION] " + e.getMessage());
        }

        // Digit ketiga = 7: iterasi HashMap menggunakan values().
        try (FileWriter writer = new FileWriter("laporan.txt")) {
            writer.write("DAFTAR AKTOR AKTIF - " + namaBioskop + System.lineSeparator());
            writer.write("========================================" + System.lineSeparator());

            for (Aktor aktor : daftarPenonton.values()) {
                writer.write(aktor.tampilkanInfo() + System.lineSeparator());
            }

            System.out.println("[LAPORAN BERHASIL] laporan.txt");
        } catch (IOException e) {
            System.out.println("[GAGAL MENULIS LAPORAN] " + e.getMessage());
        }
    }
}
