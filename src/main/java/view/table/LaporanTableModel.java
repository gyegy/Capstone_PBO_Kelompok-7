package view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.LaporanKerusakan;
import view.util.FormatUi;
import view.util.UiKit;

/**
 * Model tabel laporan kerusakan. Nama fasilitas dan petugas diisi terpisah
 * karena DAO laporan hanya menyimpan ID relasi.
 */
public class LaporanTableModel extends AbstractTableModel {

    public enum Mode {
        RINGKAS, SAYA, CARI, DETAIL
    }

    private final Mode mode;
    private final List<LaporanKerusakan> data = new ArrayList<>();
    private final List<String> namaFasilitas = new ArrayList<>();
    private final List<String> namaPetugas = new ArrayList<>();

    public LaporanTableModel(Mode mode) {
        this.mode = mode;
    }

    public void setData(List<LaporanKerusakan> daftar, List<String> fasilitas,
            List<String> petugas) {
        data.clear();
        namaFasilitas.clear();
        namaPetugas.clear();
        if (daftar != null) {
            data.addAll(daftar);
        }
        if (fasilitas != null) {
            namaFasilitas.addAll(fasilitas);
        }
        if (petugas != null) {
            namaPetugas.addAll(petugas);
        }
        fireTableDataChanged();
    }

    public LaporanKerusakan get(int baris) {
        return data.get(baris);
    }

    public String fasilitas(int baris) {
        return baris < namaFasilitas.size() ? namaFasilitas.get(baris) : "-";
    }

    public String petugas(int baris) {
        return baris < namaPetugas.size() ? namaPetugas.get(baris) : "-";
    }

    private String[] kolom() {
        switch (mode) {
            case SAYA:
                return new String[]{"ID", "Tanggal", "Fasilitas", "Prioritas",
                    "Status", "Aksi"};
            case CARI:
                return new String[]{"ID", "Tanggal", "Fasilitas", "Prioritas",
                    "Status", "Petugas", "Aksi"};
            case DETAIL:
                return new String[]{"ID", "Tanggal", "Deskripsi Kerusakan",
                    "Status", "Tindakan Penanganan"};
            default:
                return new String[]{"ID", "Fasilitas", "Prioritas", "Status",
                    "Tanggal"};
        }
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
        LaporanKerusakan l = data.get(baris);
        String[] namaKolom = kolom();
        String nama = namaKolom[kolom];

        switch (nama) {
            case "ID":
                return FormatUi.idLaporan(l.getIdLaporan());
            case "Tanggal":
                return FormatUi.tanggal(l.getTanggalLaporan());
            case "Fasilitas":
                return fasilitas(baris);
            case "Prioritas":
                return l.getPrioritas();
            case "Status":
                return l.getStatus();
            case "Petugas":
                return petugas(baris);
            case "Deskripsi Kerusakan":
                return UiKit.atau(l.getDeskripsiKerusakan(), "-");
            case "Aksi":
                // Hanya laporan berstatus Menunggu yang masih boleh diubah.
                if (mode == Mode.SAYA) {
                    return "Menunggu".equals(l.getStatus()) ? "Ubah" : "-";
                }
                return "Detail";
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int baris, int kolom) {
        return false;
    }
}
