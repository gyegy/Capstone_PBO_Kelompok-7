package controller;

import dao.DetailPenugasanDAO;
import dao.LaporanKerusakanDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Fasilitas;
import model.LaporanKerusakan;
import model.Penugasan;
import model.ProgresPenanganan;

/**
 * Controller Cari Laporan (semua peran). Petugas hanya dapat melihat laporan
 * dari penugasan yang melibatkan dirinya. Dipakai CariLaporanFrame.
 */
public class PencarianController extends BaseController {

    private final LaporanKerusakanDAO laporanDAO = new LaporanKerusakanDAO();

    // Petugas dibatasi pada penugasan miliknya
    public boolean isModePetugas() {
        return "Petugas".equals(peranLogin());
    }

    // id_laporan -> nama petugas. Untuk Petugas hanya laporan pada penugasan
    // miliknya (DETAIL_PENUGASAN); peta ini juga menjadi batas akses.
    public Map<Integer, String> petaPetugasPerLaporan() {
        Map<Integer, String> hasil = new HashMap<>();
        Map<Integer, String> namaPerTugas = namaPetugasPerPenugasan(false);
        List<Integer> milikSaya = new ArrayList<>();

        if (isModePetugas()) {
            milikSaya = new DetailPenugasanDAO()
                    .getIdPenugasanByPetugas(idLogin(), false);
        }

        for (Penugasan p : semuaPenugasan()) {
            if (isModePetugas() && !milikSaya.contains(p.getIdPenugasan())) {
                continue;
            }
            String nama = namaPerTugas.get(p.getIdPenugasan());
            hasil.put(p.getLaporanIdLaporan(), nama == null ? "-" : nama);
        }
        return hasil;
    }

    // Jalankan pencarian: kriteria kosong / "Semua" = tidak difilter.
    // Melempar IllegalArgumentException (pesan siap tampil) bila periode salah.
    public List<LaporanKerusakan> cari(String kataKunci, String status,
            String prioritas, String teksDari, String teksSampai,
            String kriteriaPetugas, Map<Integer, String> petugasPerLaporan) {

        Periode periode = Periode.parse(teksDari, teksSampai);

        kataKunci = kataKunci == null ? "" : kataKunci.trim();
        String namaDicari = kriteriaPetugas == null ? ""
                : kriteriaPetugas.trim().toLowerCase();

        List<LaporanKerusakan> sumber = kataKunci.isEmpty()
                ? laporanDAO.getAllLaporan()
                : laporanDAO.searchLaporan(kataKunci);

        List<LaporanKerusakan> hasil = new ArrayList<>();
        for (LaporanKerusakan l : sumber) {
            if (isModePetugas() && !petugasPerLaporan.containsKey(l.getIdLaporan())) {
                continue;
            }
            String namaPetugas = petugasPerLaporan.getOrDefault(l.getIdLaporan(), "-");
            if (!namaDicari.isEmpty() && !namaPetugas.toLowerCase().contains(namaDicari)) {
                continue;
            }
            if (status != null && !"Semua".equals(status) && !status.equals(l.getStatus())) {
                continue;
            }
            if (prioritas != null && !"Semua".equals(prioritas)
                    && !prioritas.equals(l.getPrioritas())) {
                continue;
            }
            if (periode.getDari() != null && l.getTanggalLaporan().before(periode.getDari())) {
                continue;
            }
            if (periode.getSampai() != null && l.getTanggalLaporan().after(periode.getSampai())) {
                continue;
            }
            hasil.add(l);
        }
        return hasil;
    }

    // ---------------------------------------------------------- Detail

    public static class Detail {
        public LaporanKerusakan laporan;
        public Fasilitas fasilitas;
        public Penugasan penugasan;           // null = belum ditugaskan
        public String namaPetugasUtama = "-";
        public String namaSupervisor = "-";
        public List<ProgresPenanganan> riwayat = new ArrayList<>();
    }

    // Detail: laporan + fasilitas + petugas penanggung jawab + riwayat progres
    public Detail getDetail(LaporanKerusakan l) {
        Detail d = new Detail();
        d.laporan = l;
        d.fasilitas = petaFasilitas().get(l.getFasilitasIdFasilitas());
        d.penugasan = penugasanPerLaporan().get(l.getIdLaporan());

        if (d.penugasan != null) {
            Map<Integer, String> nama = petaNamaPengguna();
            d.namaPetugasUtama = nama.getOrDefault(d.penugasan.getIdPengguna(), "-");
            d.namaSupervisor = nama.getOrDefault(d.penugasan.getSupervisorIdPengguna(), "-");
            d.riwayat = riwayatProgres(d.penugasan.getIdPenugasan());
        }
        return d;
    }
}
