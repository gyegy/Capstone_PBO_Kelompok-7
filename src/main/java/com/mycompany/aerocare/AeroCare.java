
package com.mycompany.aerocare;

import dao.DashboardDAO;

import java.util.Map;

public class AeroCare {

    public static void main(String[] args) {

        DashboardDAO dao = new DashboardDAO();

        Map<String, Integer> data =
                dao.getRingkasanDashboard();

        System.out.println("===== DASHBOARD AEROCARE =====");

        System.out.println(
                "Total Pengguna Aktif       : "
                + data.get("totalPengguna")
        );

        System.out.println(
                "Total Fasilitas            : "
                + data.get("totalFasilitas")
        );

        System.out.println(
                "Fasilitas Baik              : "
                + data.get("fasilitasBaik")
        );

        System.out.println(
                "Fasilitas Rusak             : "
                + data.get("fasilitasRusak")
        );

        System.out.println(
                "Fasilitas Perlu Perbaikan   : "
                + data.get("fasilitasPerluPerbaikan")
        );

        System.out.println(
                "Total Laporan Kerusakan     : "
                + data.get("totalLaporan")
        );

        System.out.println(
                "Laporan Menunggu            : "
                + data.get("laporanMenunggu")
        );

        System.out.println(
                "Laporan Diproses            : "
                + data.get("laporanDiproses")
        );

        System.out.println(
                "Laporan Selesai             : "
                + data.get("laporanSelesai")
        );

        System.out.println(
                "Total Penugasan             : "
                + data.get("totalPenugasan")
        );

        System.out.println(
                "Progres Selesai             : "
                + data.get("totalProgresSelesai")
        );

        System.out.println("==============================");
    }
}
