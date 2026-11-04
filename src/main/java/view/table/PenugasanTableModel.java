package view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Penugasan;
import view.util.FormatUi;

/**
 * Model tabel penugasan. Karena relasi laporan disimpan sebagai ID, nama
 * fasilitas dan nama petugas diisi melalui daftar pendamping.
 */
public class PenugasanTableModel extends AbstractTableModel {

    public enum Mode {
        TUGAS_SAYA, PANTAU
    }

    private final Mode mode;
    private final List<Penugasan> data = new ArrayList<>();
    private final List<String> fasilitas = new ArrayList<>();
    private final List<String> lokasi = new ArrayList<>();
    private final List<String> petugas = new ArrayList<>();
    private final List<String> prioritas = new ArrayList<>();
    private final List<String> statusLaporan = new ArrayList<>();
    private final List<String> progresTerakhir = new ArrayList<>();

    public PenugasanTableModel(Mode mode) {
        this.mode = mode;
    }

    public void setData(List<Penugasan> daftar) {
        data.clear();
        if (daftar != null) {
            data.addAll(daftar);
        }
        fireTableDataChanged();
    }

    public void setPendamping(List<String> fasilitas, List<String> lokasi,
            List<String> petugas, List<String> prioritas) {
        this.fasilitas.clear();
        this.lokasi.clear();
        this.petugas.clear();
        this.prioritas.clear();
        if (fasilitas != null) {
            this.fasilitas.addAll(fasilitas);
        }
        if (lokasi != null) {
            this.lokasi.addAll(lokasi);
        }
        if (petugas != null) {
            this.petugas.addAll(petugas);
        }
        if (prioritas != null) {
            this.prioritas.addAll(prioritas);
        }
        fireTableDataChanged();
    }

    public void setStatusPendamping(List<String> statusLaporan,
            List<String> progresTerakhir) {
        this.statusLaporan.clear();
        this.progresTerakhir.clear();
        if (statusLaporan != null) {
            this.statusLaporan.addAll(statusLaporan);
        }
        if (progresTerakhir != null) {
            this.progresTerakhir.addAll(progresTerakhir);
        }
        fireTableDataChanged();
    }

    public Penugasan get(int baris) {
        return data.get(baris);
    }

    public String fasilitas(int baris) {
        return ambil(fasilitas, baris);
    }

    public String petugas(int baris) {
        return ambil(petugas, baris);
    }

    public String lokasi(int baris) {
        return ambil(lokasi, baris);
    }

    public String prioritas(int baris) {
        return ambil(prioritas, baris);
    }

    private String ambil(List<String> daftar, int baris) {
        return baris < daftar.size() ? daftar.get(baris) : "-";
    }

    private String[] kolom() {
        if (mode == Mode.TUGAS_SAYA) {
            return new String[]{"ID Tugas", "ID Laporan", "Fasilitas", "Lokasi",
                "Prioritas", "Catatan Penugasan", "Aksi"};
        }
        return new String[]{"ID Tugas", "Waktu", "Petugas", "Fasilitas",
            "Status Laporan", "Progres Terakhir", "Aksi"};
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return kolom().length;
    }

    @Override
    public String getColumnName(int kolom) {
        return kolom()[kolom];
    }

    @Override
    public Object getValueAt(int baris, int kolom) {
        Penugasan p = data.get(baris);
        String nama = kolom()[kolom];

        switch (nama) {
            case "ID Tugas":
                return FormatUi.idTugas(p.getIdPenugasan());
            case "ID Laporan":
                return FormatUi.idLaporan(p.getLaporanIdLaporan());
            case "Fasilitas":
                return fasilitas(baris);
            case "Lokasi":
                return lokasi(baris);
            case "Prioritas":
                return prioritas(baris);
            case "Catatan Penugasan":
                return p.getCatatanPenugasan() == null
                        || p.getCatatanPenugasan().trim().isEmpty()
                                ? "-" : p.getCatatanPenugasan();
            case "Waktu":
                return FormatUi.tanggal(p.getWaktuPenugasan());
            case "Petugas":
                return petugas(baris);
            case "Status Laporan":
                return ambil(statusLaporan, baris);
            case "Progres Terakhir":
                return ambil(progresTerakhir, baris);
            case "Aksi":
                return mode == Mode.TUGAS_SAYA ? "Perbarui" : "Lihat Riwayat";
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int baris, int kolom) {
        return false;
    }
}
