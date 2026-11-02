package dao;

import config.Koneksi;
import model.Pengguna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PenggunaDAO {

    // ==========================================
    // 1. LOGIN PENGGUNA
    // ==========================================
    public Pengguna login(String username) {

        String sql = "SELECT * FROM pengguna WHERE username = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Pengguna pengguna = new Pengguna();

                    pengguna.setIdPengguna(rs.getInt("id_pengguna"));
                    pengguna.setNamaLengkap(rs.getString("nama_lengkap"));
                    pengguna.setUsername(rs.getString("username"));
                    pengguna.setPassword(rs.getString("password"));
                    pengguna.setEmail(rs.getString("email"));
                    pengguna.setNoTelepon(rs.getString("no_telepon"));
                    pengguna.setPeran(rs.getString("peran"));
                    pengguna.setStatusAktif(rs.getString("status_aktif"));

                    return pengguna;
                }
            }

        } catch (SQLException e) {
            System.out.println("Gagal melakukan login: " + e.getMessage());
        }

        return null;
    }


    // ==========================================
    // 2. MENAMPILKAN SELURUH DATA PENGGUNA
    // ==========================================
    public List<Pengguna> getAllPengguna() {

        List<Pengguna> daftarPengguna = new ArrayList<>();

        String sql = "SELECT * FROM pengguna";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pengguna pengguna = new Pengguna();

                pengguna.setIdPengguna(rs.getInt("id_pengguna"));
                pengguna.setNamaLengkap(rs.getString("nama_lengkap"));
                pengguna.setUsername(rs.getString("username"));
                pengguna.setPassword(rs.getString("password"));
                pengguna.setEmail(rs.getString("email"));
                pengguna.setNoTelepon(rs.getString("no_telepon"));
                pengguna.setPeran(rs.getString("peran"));
                pengguna.setStatusAktif(rs.getString("status_aktif"));

                daftarPengguna.add(pengguna);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data pengguna: " + e.getMessage());
        }

        return daftarPengguna;
    }


    // ==========================================
    // 3. MENAMBAHKAN PENGGUNA BARU
    // ==========================================
    public boolean tambahPengguna(
            String namaLengkap,
            String username,
            String password,
            String email,
            String noTelepon,
            String peran,
            String statusAktif,
            String dataTambahan1,
            String dataTambahan2
    ) {

        String sqlPengguna = "INSERT INTO pengguna "
                + "(nama_lengkap, username, password, email, no_telepon, peran, status_aktif) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlAdmin = "INSERT INTO admin "
                + "(id_pengguna, hak_akses) VALUES (?, ?)";

        String sqlSupervisor = "INSERT INTO supervisor "
                + "(id_pengguna, area_pengawasan) VALUES (?, ?)";

        String sqlPetugas = "INSERT INTO petugas "
                + "(id_pengguna, spesialisasi, shift_kerja) VALUES (?, ?, ?)";

        Connection conn = null;

        try {

            conn = Koneksi.getConnection();

            if (conn == null) {
                System.out.println("Koneksi database tidak tersedia.");
                return false;
            }

            conn.setAutoCommit(false);

            int idPengguna;

            // Menambahkan data ke tabel pengguna
            try (PreparedStatement ps = conn.prepareStatement(
                    sqlPengguna, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, namaLengkap);
                ps.setString(2, username);
                ps.setString(3, password);

                if (email == null || email.trim().isEmpty()) {
                    ps.setNull(4, Types.VARCHAR);
                } else {
                    ps.setString(4, email);
                }

                ps.setString(5, noTelepon);
                ps.setString(6, peran);
                ps.setString(7, statusAktif);

                ps.executeUpdate();

                // Mengambil ID otomatis dari MySQL
                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }

                    idPengguna = rs.getInt(1);
                }
            }

            // Menambahkan data sesuai peran
            if ("Admin".equals(peran)) {

                try (PreparedStatement ps = conn.prepareStatement(sqlAdmin)) {

                    ps.setInt(1, idPengguna);
                    ps.setString(2, dataTambahan1);

                    ps.executeUpdate();
                }

            } else if ("Supervisor".equals(peran)) {

                try (PreparedStatement ps = conn.prepareStatement(sqlSupervisor)) {

                    ps.setInt(1, idPengguna);
                    ps.setString(2, dataTambahan1);

                    ps.executeUpdate();
                }

            } else if ("Petugas".equals(peran)) {

                if (dataTambahan1 == null || dataTambahan2 == null
                        || dataTambahan1.trim().isEmpty()
                        || dataTambahan2.trim().isEmpty()) {

                    conn.rollback();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(sqlPetugas)) {

                    ps.setInt(1, idPengguna);
                    ps.setString(2, dataTambahan1);
                    ps.setString(3, dataTambahan2);

                    ps.executeUpdate();
                }

            } else {

                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Gagal menambahkan pengguna: " + e.getMessage());
            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // ==========================================
    // 4. MEMPERBARUI DATA PENGGUNA
    // ==========================================
    public boolean updatePengguna(
            int idPengguna,
            String namaLengkap,
            String username,
            String password,
            String email,
            String noTelepon,
            String statusAktif,
            String dataTambahan1,
            String dataTambahan2
    ) {

        String sqlPengguna = "UPDATE pengguna SET "
                + "nama_lengkap = ?, "
                + "username = ?, "
                + "password = COALESCE(NULLIF(?, ''), password), "
                + "email = ?, "
                + "no_telepon = ?, "
                + "status_aktif = ? "
                + "WHERE id_pengguna = ?";

        String sqlPeran = "SELECT peran FROM pengguna WHERE id_pengguna = ?";

        String sqlAdmin = "UPDATE admin SET hak_akses = ? "
                + "WHERE id_pengguna = ?";

        String sqlSupervisor = "UPDATE supervisor SET area_pengawasan = ? "
                + "WHERE id_pengguna = ?";

        String sqlPetugas = "UPDATE petugas SET spesialisasi = ?, shift_kerja = ? "
                + "WHERE id_pengguna = ?";

        Connection conn = null;

        try {

            conn = Koneksi.getConnection();

            if (conn == null) {
                System.out.println("Koneksi database tidak tersedia.");
                return false;
            }

            conn.setAutoCommit(false);

            String peran;

            // Mengambil peran pengguna dari database
            try (PreparedStatement ps = conn.prepareStatement(sqlPeran)) {

                ps.setInt(1, idPengguna);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        conn.rollback();
                        System.out.println("Pengguna tidak ditemukan.");
                        return false;
                    }

                    peran = rs.getString("peran");
                }
            }

            // Memperbarui data utama pengguna
            try (PreparedStatement ps = conn.prepareStatement(sqlPengguna)) {

                ps.setString(1, namaLengkap);
                ps.setString(2, username);
                ps.setString(3, password);

                if (email == null || email.trim().isEmpty()) {
                    ps.setNull(4, Types.VARCHAR);
                } else {
                    ps.setString(4, email);
                }

                ps.setString(5, noTelepon);
                ps.setString(6, statusAktif);
                ps.setInt(7, idPengguna);

                if (ps.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            // Memperbarui data tambahan sesuai peran
            if ("Admin".equals(peran)) {

                try (PreparedStatement ps = conn.prepareStatement(sqlAdmin)) {

                    ps.setString(1, dataTambahan1);
                    ps.setInt(2, idPengguna);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

            } else if ("Supervisor".equals(peran)) {

                try (PreparedStatement ps = conn.prepareStatement(sqlSupervisor)) {

                    ps.setString(1, dataTambahan1);
                    ps.setInt(2, idPengguna);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

            } else if ("Petugas".equals(peran)) {

                if (dataTambahan1 == null || dataTambahan2 == null
                        || dataTambahan1.trim().isEmpty()
                        || dataTambahan2.trim().isEmpty()) {

                    conn.rollback();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(sqlPetugas)) {

                    ps.setString(1, dataTambahan1);
                    ps.setString(2, dataTambahan2);
                    ps.setInt(3, idPengguna);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

            } else {

                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Gagal memperbarui pengguna: " + e.getMessage());
            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // ==========================================
    // 5. MENONAKTIFKAN PENGGUNA
    // ==========================================
    public boolean hapusPengguna(int idPengguna) {

        String sql = "UPDATE pengguna "
                + "SET status_aktif = 'Tidak Aktif' "
                + "WHERE id_pengguna = ? AND status_aktif = 'Aktif'";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.out.println("Koneksi database tidak tersedia.");
                return false;
            }

            ps.setInt(1, idPengguna);

            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                return true;
            } else {
                System.out.println(
                        "Pengguna tidak ditemukan atau sudah tidak aktif."
                );
                return false;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Gagal menonaktifkan pengguna: " + e.getMessage()
            );
            return false;
        }
    }
}
