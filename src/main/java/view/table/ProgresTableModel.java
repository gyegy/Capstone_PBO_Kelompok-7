package view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ProgresPenanganan;
import view.util.FormatUi;

/**
 * Model tabel riwayat progres penanganan.
 */
public class ProgresTableModel extends AbstractTableModel {

    public enum Mode {
        RIWAYAT_TUGAS, RIWAYAT_SAYA
    }

    private final Mode mode;
    private final List<ProgresPenanganan> data = new ArrayList<>();
    private final List<String> namaTugas = new ArrayList<>();
    private final List<String> namaFasilitas = new ArrayList<>();

    public ProgresTableModel(Mode mode) {
        this.mode = mode;
    }

    public void setData(List<ProgresPenanganan> daftar) {
        data.clear();
        if (daftar != null) {
            data.addAll(daftar);
        }
        fireTableDataChanged();
    }

    public void setPendamping(List<String> tugas, List<String> fasilitas) {
        namaTugas.clear();
        namaFasilitas.clear();
        if (tugas != null) {
            namaTugas.addAll(tugas);
        }
        if (fasilitas != null) {
            namaFasilitas.addAll(fasilitas);
        }
        fireTableDataChanged();
    }

    public ProgresPenanganan get(int baris) {
        return data.get(baris);
    }

    private String[] kolom() {
        if (mode == Mode.RIWAYAT_SAYA) {
            return new String[]{"ID Tugas", "Waktu Update", "Fasilitas",
                "Status Progres", "Tindakan Penanganan"};
        }
        return new String[]{"Waktu Update", "Status Progres",
            "Tindakan Penanganan"};
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
        ProgresPenanganan p = data.get(baris);
        String nama = kolom()[kolom];

        switch (nama) {
            case "Waktu Update":
                return FormatUi.tanggalWaktu(p.getWaktuUpdate());
            case "Status Progres":
                return p.getStatusProgres();
            case "Tindakan Penanganan":
                return p.getTindakanPenanganan() == null
                        || p.getTindakanPenanganan().trim().isEmpty()
                                ? "-" : p.getTindakanPenanganan();
            case "ID Tugas":
                return FormatUi.idTugas(p.getPenugasanIdPenugasan());
            case "Fasilitas":
                return baris < namaFasilitas.size()
                        ? namaFasilitas.get(baris) : "-";
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int baris, int kolom) {
        return false;
    }
}
