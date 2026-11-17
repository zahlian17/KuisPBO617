package com.mycompany.sistembioskop;

public class TransaksiException extends Exception {
    private static final long serialVersionUID = 1L;

    public TransaksiException(String pesan) {
        super(pesan);
    }
}
