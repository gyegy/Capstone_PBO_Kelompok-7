
package com.mycompany.aerocare.dao;

import config.Koneksi;
import model.LaporanKerusakan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaporanKerusakanDAO {

    // READ: Mengambil seluruh data laporan
    public List<LaporanKerusakan> getAllLaporan() {

        List<LaporanKerusakan> daftarLaporan = new ArrayList<>();

        String sql = "SELECT * FROM laporan_kerusakan";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                LaporanKerusakan laporan = new LaporanKerusakan();

                laporan.setIdLaporan(
                    rs.getInt("id_laporan")
                );

                laporan.setTanggalLaporan(
                    rs.getDate("tanggal_laporan")
                );

                laporan.setDeskripsiKerusakan(
                    rs.getString("deskripsi_kerusakan")
                );

                laporan.setPrioritas(
                    rs.getString("prioritas")
                );

                laporan.setStatus(
                    rs.getString("status")
                );

                laporan.setFasilitasIdFasilitas(
                    rs.getInt("FASILITAS_id_fasilitas")
                );

                laporan.setAdminIdPengguna(
                    rs.getInt("ADMIN_id_pengguna")
                );

                daftarLaporan.add(laporan);
            }

        } catch (SQLException e) {
            System.out.println(
                "Gagal mengambil data laporan: " + e.getMessage()
            );
        }

        return daftarLaporan;
    }

    // CREATE: Menambahkan laporan baru
    public boolean tambahLaporan(LaporanKerusakan laporan) {

        String sql = "INSERT INTO laporan_kerusakan "
                + "(tanggal_laporan, deskripsi_kerusakan, "
                + "prioritas, status, FASILITAS_id_fasilitas, "
                + "ADMIN_id_pengguna) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, laporan.getTanggalLaporan());
            ps.setString(2, laporan.getDeskripsiKerusakan());
            ps.setString(3, laporan.getPrioritas());
            ps.setString(4, "Menunggu");
            ps.setInt(5, laporan.getFasilitasIdFasilitas());
            ps.setInt(6, laporan.getAdminIdPengguna());

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menambahkan laporan: " + e.getMessage()
            );
            return false;
        }
    }

    // UPDATE: Memperbarui laporan yang masih menunggu
    public boolean updateLaporan(LaporanKerusakan laporan) {

        String sql = "UPDATE laporan_kerusakan SET "
                + "tanggal_laporan = ?, "
                + "deskripsi_kerusakan = ?, "
                + "prioritas = ?, "
                + "FASILITAS_id_fasilitas = ? "
                + "WHERE id_laporan = ? "
                + "AND status = 'Menunggu'";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, laporan.getTanggalLaporan());
            ps.setString(2, laporan.getDeskripsiKerusakan());
            ps.setString(3, laporan.getPrioritas());
            ps.setInt(4, laporan.getFasilitasIdFasilitas());
            ps.setInt(5, laporan.getIdLaporan());

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal memperbarui laporan: " + e.getMessage()
            );
            return false;
        }
    }

    // DELETE: Menghapus laporan yang masih menunggu
    public boolean hapusLaporan(int idLaporan) {

        String sql = "DELETE FROM laporan_kerusakan "
                + "WHERE id_laporan = ? "
                + "AND status = 'Menunggu'";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLaporan);

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menghapus laporan: " + e.getMessage()
            );
            return false;
        }
    }
    
    //ini method searcch laporan
    public List<LaporanKerusakan> searchLaporan(String keyword) {

        List<LaporanKerusakan> daftarLaporan = new ArrayList<>();

        String sql = "SELECT lr.* "
                + "FROM laporan_kerusakan lr "
                + "JOIN fasilitas f "
                + "ON lr.FASILITAS_id_fasilitas = f.id_fasilitas "
                + "WHERE CAST(lr.id_laporan AS CHAR) LIKE ? "
                + "OR f.nama_fasilitas LIKE ? "
                + "OR lr.deskripsi_kerusakan LIKE ? "
                + "OR lr.prioritas LIKE ? "
                + "OR lr.status LIKE ? "
                + "OR f.lokasi LIKE ? "
                + "ORDER BY lr.id_laporan ASC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String parameter = "%" + keyword + "%";

            ps.setString(1, parameter);
            ps.setString(2, parameter);
            ps.setString(3, parameter);
            ps.setString(4, parameter);
            ps.setString(5, parameter);
            ps.setString(6, parameter);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    LaporanKerusakan laporan = new LaporanKerusakan();

                    laporan.setIdLaporan(
                            rs.getInt("id_laporan")
                    );

                    laporan.setTanggalLaporan(
                            rs.getDate("tanggal_laporan")
                    );

                    laporan.setDeskripsiKerusakan(
                            rs.getString("deskripsi_kerusakan")
                    );

                    laporan.setPrioritas(
                            rs.getString("prioritas")
                    );

                    laporan.setStatus(
                            rs.getString("status")
                    );

                    laporan.setFasilitasIdFasilitas(
                            rs.getInt("FASILITAS_id_fasilitas")
                    );

                    laporan.setAdminIdPengguna(
                            rs.getInt("ADMIN_id_pengguna")
                    );

                    daftarLaporan.add(laporan);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Gagal mencari laporan: " + e.getMessage()
            );
        }

        return daftarLaporan;
    }
}
