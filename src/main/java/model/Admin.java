package model;

public class Admin extends Pengguna {

    private String hakAkses;

    public Admin() {
        super();
        setPeran("Admin");
    }

    public Admin(int idPengguna, String namaLengkap,
            String username, String password, String email,
            String noTelepon, String statusAktif,
            String hakAkses) {

        super(idPengguna, namaLengkap, username,
                password, email, noTelepon,
                "Admin", statusAktif);

        this.hakAkses = hakAkses;
    }

    public String getHakAksesAdmin() {
        return hakAkses;
    }

    public void setHakAksesAdmin(String hakAkses) {
        this.hakAkses = hakAkses;
    }

    @Override
    public String getHakAkses() {
        return "Mengelola seluruh data sistem AeroCare";
    }

    @Override
    public void tampilkanInformasi() {
        super.tampilkanInformasi();
        System.out.println("Hak Akses Admin : " + hakAkses);
    }
}
