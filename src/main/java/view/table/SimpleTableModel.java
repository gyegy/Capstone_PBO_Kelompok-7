package view.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Model tabel serbaguna berbasis baris teks. Dipakai untuk daftar ringkas
 * seperti "Laporan Terbaru" pada dashboard dan daftar "Perlu Ditugaskan".
 */
public class SimpleTableModel extends AbstractTableModel {

    private final String[] kolom;
    private final List<Object[]> baris = new ArrayList<>();

    public SimpleTableModel(String... kolom) {
        this.kolom = kolom;
    }

    public void setBaris(List<Object[]> data) {
        baris.clear();
        if (data != null) {
            baris.addAll(data);
        }
        fireTableDataChanged();
    }

    public void tambah(Object... nilai) {
        baris.add(Arrays.copyOf(nilai, kolom.length));
        fireTableRowsInserted(baris.size() - 1, baris.size() - 1);
    }

    public Object[] get(int index) {
        return baris.get(index);
    }

    public void kosongkan() {
        baris.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return baris.size();
    }

    @Override
    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int index) {
        return kolom[index];
    }

    @Override
    public Object getValueAt(int barisData, int kolomData) {
        Object[] nilai = baris.get(barisData);
        if (kolomData < nilai.length && nilai[kolomData] != null) {
            return nilai[kolomData];
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int barisData, int kolomData) {
        return false;
    }
}
