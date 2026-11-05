package model;

public class Supervisor extends Pengguna {

    private String areaPengawasan;

    public Supervisor() {
        super();
        setPeran("Supervisor");
    }

    public Supervisor(int idPengguna, String namaLengkap,
            String username, String password, String email,
            String noTelepon, String statusAktif,
            String areaPengawasan) {

        super(idPengguna, namaLengkap, username,
                password, email, noTelepon,
                "Supervisor", statusAktif);

        this.areaPengawasan = areaPengawasan;
    }


    public String getAreaPengawasan() {
        return areaPengawasan;
    }

    public void setAreaPengawasan(String areaPengawasan) {
        this.areaPengawasan = areaPengawasan;
    }


    @Override
    public String getInformasiPeran() {
        return "Area Pengawasan : " + areaPengawasan;
    }
}
