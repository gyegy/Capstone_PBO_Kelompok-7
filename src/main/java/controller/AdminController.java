package controller;

import dao.DashboardDAO;
import java.util.Map;

public class AdminController extends AuthController {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    // totalFasilitas, fasilitasRusak, laporanMenunggu, totalPengguna, dst.
    public Map<String, Integer> getRingkasanDashboard() {
        return dashboardDAO.getRingkasanDashboard();
    }
}
