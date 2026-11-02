package com.mycompany.aerocare.dao;

import config.Koneksi;
import model.Fasilitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FasilitasDAO {

    // READ: Mengambil seluruh data fasilitas
    public List<Fasilitas> getAllFasilitas() {

        List<Fasilitas> daftarFasilitas = new ArrayList<>();

        String sql = "SELECT * FROM fasilitas";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Fasilitas fasilitas = new Fasilitas();

                fasilitas.setIdFasilitas(
                    rs.getInt("id_fasilitas")
                );

                fasilitas.setNamaFasilitas(
                    rs.getString("nama_fasilitas")
                );

                fasilitas.setJenisFasilitas(
                    rs.getString("jenis_fasilitas")
                );

                fasilitas.setLokasi(
                    rs.getString("lokasi")
                );

                fasilitas.setKondisi(
                    rs.getString("kondisi")
                );

                fasilitas.setKeterangan(
                    rs.getString("keterangan")
                );

                fasilitas.setAdminIdPengguna(
                    rs.getInt("ADMIN_id_pengguna")
                );

                daftarFasilitas.add(fasilitas);
            }

        } catch (SQLException e) {
            System.out.println(
                "Gagal mengambil data fasilitas: " + e.getMessage()
            );
        }

        return daftarFasilitas;
    }

    // CREATE: Menambahkan data fasilitas
    public boolean tambahFasilitas(Fasilitas fasilitas) {

        String sql = "INSERT INTO fasilitas "
                + "(nama_fasilitas, jenis_fasilitas, lokasi, kondisi, "
                + "keterangan, ADMIN_id_pengguna) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fasilitas.getNamaFasilitas());
            ps.setString(2, fasilitas.getJenisFasilitas());
            ps.setString(3, fasilitas.getLokasi());
            ps.setString(4, fasilitas.getKondisi());
            ps.setString(5, fasilitas.getKeterangan());
            ps.setInt(6, fasilitas.getAdminIdPengguna());

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menambahkan fasilitas: " + e.getMessage()
            );
            return false;
        }
    }

    // UPDATE: Memperbarui data fasilitas
    public boolean updateFasilitas(Fasilitas fasilitas) {

        String sql = "UPDATE fasilitas SET "
                + "nama_fasilitas = ?, "
                + "jenis_fasilitas = ?, "
                + "lokasi = ?, "
                + "kondisi = ?, "
                + "keterangan = ? "
                + "WHERE id_fasilitas = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fasilitas.getNamaFasilitas());
            ps.setString(2, fasilitas.getJenisFasilitas());
            ps.setString(3, fasilitas.getLokasi());
            ps.setString(4, fasilitas.getKondisi());
            ps.setString(5, fasilitas.getKeterangan());
            ps.setInt(6, fasilitas.getIdFasilitas());

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal memperbarui fasilitas: " + e.getMessage()
            );
            return false;
        }
    }

    // DELETE: Menghapus data fasilitas
    public boolean hapusFasilitas(int idFasilitas) {

        String sql = "DELETE FROM fasilitas "
                + "WHERE id_fasilitas = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFasilitas);

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menghapus fasilitas. "
                + "Pastikan fasilitas tidak memiliki riwayat laporan. "
                + e.getMessage()
            );
            return false;
        }
    }
}
