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


    public String getHakAkses() {
        return hakAkses;
    }

    public void setHakAkses(String hakAkses) {
        this.hakAkses = hakAkses;
    }

    @Override
    public String getInformasiPeran() {
        return "Hak Akses Admin : " + hakAkses;
    }
}
