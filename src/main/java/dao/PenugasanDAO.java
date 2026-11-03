
package com.mycompany.aerocare.dao;

import config.Koneksi;
import model.Penugasan;
import model.DetailPenugasan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PenugasanDAO {

    // READ: Mengambil seluruh data penugasan
    public List<Penugasan> getAllPenugasan() {

        List<Penugasan> daftarPenugasan = new ArrayList<>();

        String sql = "SELECT * FROM penugasan";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Penugasan penugasan = new Penugasan();

                penugasan.setIdPenugasan(
                    rs.getInt("id_penugasan")
                );

                penugasan.setWaktuPenugasan(
                    rs.getDate("waktu_penugasan")
                );

                penugasan.setCatatanPenugasan(
                    rs.getString("catatan_penugasan")
                );

                penugasan.setSupervisorIdPengguna(
                    rs.getInt("SUPERVISOR_id_pengguna")
                );

                penugasan.setLaporanIdLaporan(
                    rs.getInt("LAPORAN_KERUSAKAN_id_laporan")
                );

                penugasan.setIdPengguna(
                    rs.getInt("id_pengguna")
                );

                daftarPenugasan.add(penugasan);
            }

        } catch (SQLException e) {
            System.out.println(
                "Gagal mengambil data penugasan: " + e.getMessage()
            );
        }

        return daftarPenugasan;
    }

    // CREATE: Menambahkan penugasan beserta Petugas
    public boolean tambahPenugasan(
            Penugasan penugasan,
            List<Integer> daftarIdPetugas) {

        String sqlPenugasan = "INSERT INTO penugasan "
                + "(waktu_penugasan, catatan_penugasan, "
                + "SUPERVISOR_id_pengguna, "
                + "LAPORAN_KERUSAKAN_id_laporan, id_pengguna) "
                + "VALUES (?, ?, ?, ?, ?)";

        String sqlDetail = "INSERT INTO detail_penugasan "
                + "(status_keterlibatan, PETUGAS_id_pengguna, "
                + "PENUGASAN_id_penugasan) "
                + "VALUES (?, ?, ?)";

        Connection conn = null;

        try {
            conn = Koneksi.getConnection();

            if (conn == null) {
                System.out.println("Koneksi database gagal.");
                return false;
            }

            conn.setAutoCommit(false);

            int idPenugasan;

            try (PreparedStatement ps = conn.prepareStatement(
                    sqlPenugasan, Statement.RETURN_GENERATED_KEYS)) {

                ps.setDate(1, penugasan.getWaktuPenugasan());
                ps.setString(2, penugasan.getCatatanPenugasan());
                ps.setInt(3, penugasan.getSupervisorIdPengguna());
                ps.setInt(4, penugasan.getLaporanIdLaporan());
                ps.setInt(5, penugasan.getIdPengguna());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException(
                            "ID penugasan gagal diperoleh."
                        );
                    }

                    idPenugasan = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {

                for (Integer idPetugas : daftarIdPetugas) {

                    ps.setString(1, "Aktif");
                    ps.setInt(2, idPetugas);
                    ps.setInt(3, idPenugasan);

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            conn.commit();

            return true;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println(
                        "Gagal membatalkan transaksi: " + ex.getMessage()
                    );
                }
            }

            System.out.println(
                "Gagal menambahkan penugasan: " + e.getMessage()
            );

            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(
                        "Gagal menutup koneksi: " + e.getMessage()
                    );
                }
            }
        }
    }
    
    public boolean updatePenugasan(
            Penugasan penugasan,
            List<Integer> daftarIdPetugas) {

        String sqlPenugasan = "UPDATE penugasan SET "
                + "waktu_penugasan = ?, "
                + "catatan_penugasan = ? "
                + "WHERE id_penugasan = ?";

        String sqlHapusDetail = "DELETE FROM detail_penugasan "
                + "WHERE PENUGASAN_id_penugasan = ?";

        String sqlDetail = "INSERT INTO detail_penugasan "
                + "(status_keterlibatan, PETUGAS_id_pengguna, "
                + "PENUGASAN_id_penugasan) "
                + "VALUES (?, ?, ?)";

        Connection conn = null;

        try {
            if (daftarIdPetugas == null || daftarIdPetugas.isEmpty()) {
                System.out.println("Pilih minimal satu Petugas.");
                return false;
            }

            conn = Koneksi.getConnection();

            if (conn == null) {
                System.out.println("Koneksi database gagal.");
                return false;
            }

            conn.setAutoCommit(false);

            int idPenugasan = penugasan.getIdPenugasan();

            try (PreparedStatement ps = conn.prepareStatement(sqlPenugasan)) {

                ps.setDate(1, penugasan.getWaktuPenugasan());
                ps.setString(2, penugasan.getCatatanPenugasan());
                ps.setInt(3, idPenugasan);

                int hasil = ps.executeUpdate();

                if (hasil == 0) {
                    throw new SQLException(
                        "Data penugasan tidak ditemukan."
                    );
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlHapusDetail)) {
                ps.setInt(1, idPenugasan);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {

                for (Integer idPetugas : daftarIdPetugas) {

                    ps.setString(1, "Aktif");
                    ps.setInt(2, idPetugas);
                    ps.setInt(3, idPenugasan);

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            conn.commit();

            return true;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println(
                        "Gagal membatalkan transaksi: " + ex.getMessage()
                    );
                }
            }

            System.out.println(
                "Gagal memperbarui penugasan: " + e.getMessage()
            );

            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(
                        "Gagal menutup koneksi: " + e.getMessage()
                    );
                }
            }
        }
    }
    
    public boolean hapusPenugasan(int idPenugasan) {

        String sqlProgres = "SELECT COUNT(*) "
                + "FROM progres_penanganan "
                + "WHERE PENUGASAN_id_penugasan = ?";

        String sqlHapusDetail = "DELETE FROM detail_penugasan "
                + "WHERE PENUGASAN_id_penugasan = ?";

        String sqlHapusPenugasan = "DELETE FROM penugasan "
                + "WHERE id_penugasan = ?";

        Connection conn = null;

        try {
            conn = Koneksi.getConnection();

            if (conn == null) {
                System.out.println("Koneksi database gagal.");
                return false;
            }

            conn.setAutoCommit(false);

            // Memeriksa apakah penugasan sudah memiliki progres
            try (PreparedStatement ps = conn.prepareStatement(sqlProgres)) {

                ps.setInt(1, idPenugasan);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println(
                            "Penugasan tidak dapat dihapus karena "
                            + "sudah memiliki riwayat progres."
                        );

                        conn.rollback();
                        return false;
                    }
                }
            }

            // Menghapus detail penugasan terlebih dahulu
            try (PreparedStatement ps = conn.prepareStatement(sqlHapusDetail)) {

                ps.setInt(1, idPenugasan);
                ps.executeUpdate();
            }

            // Menghapus data penugasan
            try (PreparedStatement ps = conn.prepareStatement(sqlHapusPenugasan)) {

                ps.setInt(1, idPenugasan);

                int hasil = ps.executeUpdate();

                if (hasil == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();

            return true;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println(
                        "Gagal membatalkan transaksi: " + ex.getMessage()
                    );
                }
            }

            System.out.println(
                "Gagal menghapus penugasan: " + e.getMessage()
            );

            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(
                        "Gagal menutup koneksi: " + e.getMessage()
                    );
                }
            }
        }
    }
    
    // ini method cari penugasan
    public List<Penugasan> searchPenugasan(String keyword) {

        List<Penugasan> daftarPenugasan = new ArrayList<>();

        String sql = "SELECT DISTINCT p.* "
                + "FROM penugasan p "
                + "LEFT JOIN pengguna s "
                + "ON p.SUPERVISOR_id_pengguna = s.id_pengguna "
                + "LEFT JOIN detail_penugasan dp "
                + "ON p.id_penugasan = dp.PENUGASAN_id_penugasan "
                + "LEFT JOIN pengguna pt "
                + "ON dp.PETUGAS_id_pengguna = pt.id_pengguna "
                + "WHERE CAST(p.id_penugasan AS CHAR) LIKE ? "
                + "OR CAST(p.LAPORAN_KERUSAKAN_id_laporan AS CHAR) LIKE ? "
                + "OR p.catatan_penugasan LIKE ? "
                + "OR s.nama_lengkap LIKE ? "
                + "OR pt.nama_lengkap LIKE ? "
                + "ORDER BY p.id_penugasan ASC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String parameter = "%" + keyword + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, parameter);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Penugasan penugasan = new Penugasan();

                    penugasan.setIdPenugasan(
                            rs.getInt("id_penugasan")
                    );

                    penugasan.setWaktuPenugasan(
                            rs.getDate("waktu_penugasan")
                    );

                    penugasan.setCatatanPenugasan(
                            rs.getString("catatan_penugasan")
                    );

                    penugasan.setSupervisorIdPengguna(
                            rs.getInt("SUPERVISOR_id_pengguna")
                    );

                    penugasan.setLaporanIdLaporan(
                            rs.getInt("LAPORAN_KERUSAKAN_id_laporan")
                    );

                    penugasan.setIdPengguna(
                            rs.getInt("id_pengguna")
                    );

                    daftarPenugasan.add(penugasan);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Gagal mencari penugasan: " + e.getMessage()
            );
        }

        return daftarPenugasan;
    }
}
