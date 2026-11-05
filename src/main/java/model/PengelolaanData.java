package model;

public interface PengelolaanData<T> {

    void tambah(T data);

    void ubah(T data);

    void hapus(int id);

    T cari(int id);
}