package controller;

import config.Sesi;
import dao.DetailPenugasanDAO;
import dao.FasilitasDAO;
import dao.LaporanKerusakanDAO;
import dao.PenggunaDAO;
import dao.PenugasanDAO;
import dao.ProgresPenangananDAO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Fasilitas;
import model.LaporanKerusakan;
import model.Penugasan;
import model.Pengguna;
import model.ProgresPenanganan;


public abstract class BaseController {


    public int idLogin() {
        String id = Sesi.getIdPengguna();
        return id == null ? 0 : Integer.parseInt(id);
    }

    public String namaLogin() {
        return Sesi.getNamaLengkap();
    }

    public String peranLogin() {
        return Sesi.getPeran();
    }

    public String labelPengguna() {
        return Sesi.getNamaLengkap() + " (" + Sesi.getPeran() + ")";
    }

    public void hapusSesi() {
        Sesi.hapus();
    }


    public List<Fasilitas> semuaFasilitas() {
        return new FasilitasDAO().getAllFasilitas();
    }

    public List<LaporanKerusakan> semuaLaporan() {
        return new LaporanKerusakanDAO().getAllLaporan();
    }

    public List<Penugasan> semuaPenugasan() {
        return new PenugasanDAO().getAllPenugasan();
    }

    public Map<Integer, Fasilitas> petaFasilitas() {
        Map<Integer, Fasilitas> peta = new HashMap<>();
        for (Fasilitas f : semuaFasilitas()) {
            peta.put(f.getIdFasilitas(), f);
        }
        return peta;
    }

    public Map<Integer, LaporanKerusakan> petaLaporan() {
        Map<Integer, LaporanKerusakan> peta = new HashMap<>();
        for (LaporanKerusakan l : semuaLaporan()) {
            peta.put(l.getIdLaporan(), l);
        }
        return peta;
    }

    public Map<Integer, String> petaNamaPengguna() {
        Map<Integer, String> peta = new HashMap<>();
        for (Pengguna u : new PenggunaDAO().getAllPengguna()) {
            peta.put(u.getIdPengguna(), u.getNamaLengkap());
        }
        return peta;
    }

    public Map<Integer, Penugasan> penugasanPerLaporan() {
        Map<Integer, Penugasan> peta = new HashMap<>();
        for (Penugasan p : semuaPenugasan()) {
            peta.putIfAbsent(p.getLaporanIdLaporan(), p);
        }
        return peta;
    }

    public Penugasan cariPenugasan(int idPenugasan) {
        for (Penugasan p : semuaPenugasan()) {
            if (p.getIdPenugasan() == idPenugasan) {
                return p;
            }
        }
        return null;
    }

    public Map<Integer, List<Integer>> petugasPerPenugasan() {
        Map<Integer, List<Integer>> peta = new HashMap<>();
        for (int[] k : new DetailPenugasanDAO().getKeterlibatan()) {
            peta.computeIfAbsent(k[1], x -> new ArrayList<>()).add(k[0]);
        }
        return peta;
    }

    public String namaPetugas(Penugasan p, Map<Integer, List<Integer>> petugas,
            Map<Integer, String> nama, boolean tandaiUtama) {

        List<Integer> ids = new ArrayList<>(
                petugas.getOrDefault(p.getIdPenugasan(), new ArrayList<>()));

        boolean adaUtama = ids.remove(Integer.valueOf(p.getIdPengguna()));
        if (adaUtama) {
            ids.add(0, p.getIdPengguna());
        }
        if (ids.isEmpty()) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(nama.getOrDefault(ids.get(i), "?"));
            if (tandaiUtama && adaUtama && i == 0) {
                sb.append(" (utama)");
            }
        }
        return sb.toString();
    }

    public Map<Integer, String> namaPetugasPerPenugasan(boolean tandaiUtama) {
        Map<Integer, List<Integer>> petugas = petugasPerPenugasan();
        Map<Integer, String> nama = petaNamaPengguna();
        Map<Integer, String> hasil = new HashMap<>();

        for (Penugasan p : semuaPenugasan()) {
            hasil.put(p.getIdPenugasan(), namaPetugas(p, petugas, nama, tandaiUtama));
        }
        return hasil;
    }

    public List<ProgresPenanganan> riwayatProgres(int idPenugasan) {
        return new ProgresPenangananDAO().getAllProgres(idPenugasan);
    }

    public ProgresPenanganan progresTerakhir(int idPenugasan) {
        List<ProgresPenanganan> daftar = riwayatProgres(idPenugasan);
        return daftar.isEmpty() ? null : daftar.get(daftar.size() - 1);
    }

    public Timestamp waktuSelesai(int idPenugasan) {
        Timestamp hasil = null;
        for (ProgresPenanganan r : riwayatProgres(idPenugasan)) {
            if ("Selesai".equals(r.getStatusProgres())) {
                hasil = r.getWaktuUpdate();
            }
        }
        return hasil;
    }

    public List<LaporanKerusakan> laporanPeriode(Periode periode) {
        List<LaporanKerusakan> hasil = new ArrayList<>();
        for (LaporanKerusakan l : semuaLaporan()) {
            if (periode.memuat(l.getTanggalLaporan())) {
                hasil.add(l);
            }
        }
        return hasil;
    }
}
