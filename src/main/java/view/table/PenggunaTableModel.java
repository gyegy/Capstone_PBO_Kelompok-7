package view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Pengguna;
import view.util.FormatUi;

/**
 * Model tabel daftar pengguna. Kolom "Data Khusus Peran" berisi hak akses
 * untuk Admin, area pengawasan untuk Supervisor, dan spesialisasi untuk
 * Petugas.
 */
public class PenggunaTableModel extends AbstractTableModel {

    private static final String[] KOLOM = {
        "ID", "Nama Lengkap", "Username", "Peran", "Status",
        "Data Khusus Peran", "Aksi"
    };

    private final List<Pengguna> data = new ArrayList<>();
    private final List<String> dataKhusus = new ArrayList<>();

    public void setData(List<Pengguna> daftar, List<String> khusus) {
        data.clear();
        dataKhusus.clear();
        if (daftar != null) {
            data.addAll(daftar);
        }
        if (khusus != null) {
            dataKhusus.addAll(khusus);
        }
        fireTableDataChanged();
    }

    public Pengguna get(int baris) {
        return data.get(baris);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return KOLOM.length;
    }

    @Override
    public String getColumnName(int kolom) {
        return KOLOM[kolom];
    }

    @Override
    public Object getValueAt(int baris, int kolom) {
        Pengguna p = data.get(baris);
        switch (kolom) {
            case 0:
                return FormatUi.idPengguna(p.getIdPengguna());
            case 1:
                return p.getNamaLengkap();
            case 2:
                return p.getUsername();
            case 3:
                return p.getPeran();
            case 4:
                return p.getStatusAktif();
            case 5:
                return baris < dataKhusus.size() ? dataKhusus.get(baris) : "-";
            case 6:
                return "";
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int baris, int kolom) {
        return false;
    }
}
