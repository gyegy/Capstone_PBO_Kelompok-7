package dao;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Akses tabel DETAIL_PENUGASAN: siapa saja Petugas yang terlibat
 * pada sebuah penugasan (dipakai tampilan Petugas).
 */
public class DetailPenugasanDAO {

    // ID penugasan yang melibatkan petugas tertentu.
    // hanyaAktif = true -> hanya status_keterlibatan 'Aktif'
    public List<Integer> getIdPenugasanByPetugas(int idPetugas, boolean hanyaAktif) {
        List<Integer> daftar = new ArrayList<>();

        String sql = "SELECT DISTINCT PENUGASAN_id_penugasan "
                + "FROM detail_penugasan "
                + "WHERE PETUGAS_id_pengguna = ? "
                + (hanyaAktif ? "AND status_keterlibatan = 'Aktif' " : "")
                + "ORDER BY PENUGASAN_id_penugasan DESC";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                return daftar;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPetugas);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        daftar.add(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil detail penugasan: " + e.getMessage());
        }

        return daftar;
    }

    // Nama Petugas berstatus Aktif untuk setiap penugasan
    public Map<Integer, String> getNamaPetugasPerPenugasan(boolean par) {
        Map<Integer, String> peta = new LinkedHashMap<>();

        String sql = "SELECT dp.PENUGASAN_id_penugasan, p.nama_lengkap "
                + "FROM detail_penugasan dp "
                + "JOIN pengguna p ON dp.PETUGAS_id_pengguna = p.id_pengguna "
                + "WHERE dp.status_keterlibatan = 'Aktif' "
                + "ORDER BY dp.PENUGASAN_id_penugasan, p.nama_lengkap";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                return peta;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt(1);
                    String nama = rs.getString(2);
                    peta.merge(id, nama, (a, b) -> a + ", " + b);
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil nama petugas: " + e.getMessage());
        }

        return peta;
    }

    public Iterable<int[]> getKeterlibatan() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
