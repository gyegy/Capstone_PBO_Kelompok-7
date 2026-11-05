package model;

public class Petugas extends Pengguna {

    private String spesialisasi;
    private String shiftKerja;

    public Petugas() {
        super();
        setPeran("Petugas");
    }

    public Petugas(int idPengguna, String namaLengkap,
            String username, String password, String email,
            String noTelepon, String statusAktif,
            String spesialisasi, String shiftKerja) {

        super(idPengguna, namaLengkap, username,
                password, email, noTelepon,
                "Petugas", statusAktif);

        this.spesialisasi = spesialisasi;
        this.shiftKerja = shiftKerja;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public String getShiftKerja() {
        return shiftKerja;
    }

    public void setShiftKerja(String shiftKerja) {
        this.shiftKerja = shiftKerja;
    }

    @Override
    public String getHakAkses() {
        return "Menangani pekerjaan dan memperbarui progres perbaikan";
    }

    @Override
    public void tampilkanInformasi() {
        super.tampilkanInformasi();
        System.out.println("Spesialisasi : " + spesialisasi);
        System.out.println("Shift Kerja  : " + shiftKerja);
    }
}
