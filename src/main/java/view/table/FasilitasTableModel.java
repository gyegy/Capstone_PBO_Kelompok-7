package view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Fasilitas;
import view.util.FormatUi;

/**
 * Model tabel daftar fasilitas bandara.
 */
public class FasilitasTableModel extends AbstractTableModel {

    private static final String[] KOLOM = {
        "ID", "Nama Fasilitas", "Jenis", "Lokasi", "Kondisi", "Keterangan", "Aksi"
    };

    private final List<Fasilitas> data = new ArrayList<>();

    public void setData(List<Fasilitas> daftar) {
        data.clear();
        if (daftar != null) {
            data.addAll(daftar);
        }
        fireTableDataChanged();
    }

    public Fasilitas get(int baris) {
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
        Fasilitas f = data.get(baris);
        switch (kolom) {
            case 0:
                return FormatUi.idFasilitas(f.getIdFasilitas());
            case 1:
                return f.getNamaFasilitas();
            case 2:
                return f.getJenisFasilitas();
            case 3:
                return f.getLokasi();
            case 4:
                return f.getKondisi();
            case 5:
                return f.getKeterangan() == null ? "-" : f.getKeterangan();
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
