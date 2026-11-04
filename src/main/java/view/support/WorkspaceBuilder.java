package view.support;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/** Layout controller for the shared AEROCARE workspace shell. */
public final class WorkspaceBuilder {
    private WorkspaceBuilder() {}
    public static void mount(Container target, String role, String page, Color accent) {
        target.removeAll(); target.setLayout(new BorderLayout()); target.setBackground(AeroCareTheme.PAGE);
        JPanel shell=new JPanel(new BorderLayout()); shell.setBackground(AeroCareTheme.PAGE);
        shell.add(sidebar(role,page,accent),BorderLayout.WEST);
        JPanel main=new JPanel(new BorderLayout(0,18)); main.setBackground(AeroCareTheme.PAGE);
        main.setBorder(new EmptyBorder(20,24,24,24));
        JPanel top=new JPanel(new BorderLayout()); top.setOpaque(false);
        JLabel brand=new JLabel("AEROCARE  /  "+role.toUpperCase()); brand.setFont(new Font("Segoe UI",Font.BOLD,11)); brand.setForeground(AeroCareTheme.MUTED);
        JLabel user=new JLabel("●  Pengguna aktif     ▾"); user.setFont(new Font("Segoe UI",Font.PLAIN,12)); user.setForeground(AeroCareTheme.INK);
        top.add(brand,BorderLayout.WEST); top.add(user,BorderLayout.EAST); main.add(top,BorderLayout.NORTH);
        main.add(pageContent(page,accent),BorderLayout.CENTER); shell.add(main,BorderLayout.CENTER); target.add(shell,BorderLayout.CENTER); target.revalidate(); target.repaint();
    }
    private static JPanel sidebar(String role,String page,Color accent) {
        JPanel side=new JPanel(); side.setBackground(AeroCareTheme.NAVY); side.setPreferredSize(new Dimension(205,700)); side.setBorder(new EmptyBorder(22,14,18,14)); side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));
        JLabel logo=new JLabel("✦  AEROCARE"); logo.setForeground(Color.WHITE); logo.setFont(new Font("Segoe UI",Font.BOLD,19)); logo.setBorder(new EmptyBorder(4,8,26,4)); side.add(logo);
        side.add(nav("Dashboard",page,accent));
        if(role.equalsIgnoreCase("Admin")) { side.add(nav("Data Fasilitas",page,accent)); side.add(nav("Laporan Kerusakan",page,accent)); side.add(nav("Data Pengguna",page,accent)); side.add(nav("Rekapitulasi",page,accent)); }
        else if(role.equalsIgnoreCase("Supervisor")) { side.add(nav("Penugasan",page,accent)); side.add(nav("Progres Penanganan",page,accent)); side.add(nav("Rekap Laporan",page,accent)); }
        else { side.add(nav("Daftar Tugas",page,accent)); side.add(nav("Input Progres",page,accent)); side.add(nav("Riwayat Progres",page,accent)); }
        side.add(Box.createVerticalGlue()); JLabel foot=new JLabel("AEROCARE  •  v1.0"); foot.setForeground(new Color(150,163,184)); foot.setFont(new Font("Segoe UI",Font.PLAIN,10)); side.add(foot); return side;
    }
    private static JButton nav(String label,String current,Color accent) {
        JButton b=new JButton((label.equals(current)?"▰  ":"   ")+label); b.setAlignmentX(Component.LEFT_ALIGNMENT); b.setMaximumSize(new Dimension(180,38)); b.setHorizontalAlignment(SwingConstants.LEFT); b.setFont(new Font("Segoe UI",label.equals(current)?Font.BOLD:Font.PLAIN,12)); b.setForeground(Color.WHITE); b.setBackground(label.equals(current)?accent:AeroCareTheme.NAVY); b.setBorder(new EmptyBorder(9,10,9,8)); b.setFocusPainted(false); b.setOpaque(true); b.setBorderPainted(false); return b;
    }
    private static JPanel pageContent(String page,Color accent) {
        JPanel p=new JPanel(new BorderLayout(0,16)); p.setOpaque(false);
        JPanel intro=new JPanel(); intro.setOpaque(false); intro.setLayout(new BoxLayout(intro,BoxLayout.Y_AXIS));
        intro.add(AeroCareTheme.heading(page)); intro.add(Box.createVerticalStrut(5)); intro.add(AeroCareTheme.muted("Kelola data dan aktivitas pemeliharaan fasilitas dalam satu tempat.")); p.add(intro,BorderLayout.NORTH);
        JPanel body=new JPanel(new BorderLayout(0,16)); body.setOpaque(false);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); actions.setOpaque(false); JTextField search=new JTextField("Cari data...",20); search.setPreferredSize(new Dimension(220,36)); actions.add(search);
        JButton filter=new JButton("Filter"); AeroCareTheme.styleButton(filter,new Color(71,85,105)); actions.add(filter); JButton add=new JButton("+ Tambah Data"); AeroCareTheme.styleButton(add,accent); actions.add(add); body.add(actions,BorderLayout.NORTH);
        JPanel cards=new JPanel(new GridLayout(1,4,12,0)); cards.setOpaque(false); String[] labels={"Total Data","Menunggu","Diproses","Selesai"}; String[] vals={"128","12","36","80"};
        for(int i=0;i<labels.length;i++){ JPanel c=new JPanel(new BorderLayout(0,8)); c.setBackground(Color.WHITE); c.setBorder(new CompoundBorder(new LineBorder(AeroCareTheme.LINE),new EmptyBorder(14,16,14,16))); JLabel v=new JLabel(vals[i]);v.setFont(new Font("Segoe UI",Font.BOLD,23));v.setForeground(i==1?accent:AeroCareTheme.INK);c.add(AeroCareTheme.muted(labels[i]),BorderLayout.NORTH);c.add(v,BorderLayout.CENTER);cards.add(c); }
        JPanel center=new JPanel(new BorderLayout(0,12)); center.setOpaque(false); center.add(cards,BorderLayout.NORTH);
        String[] cols={"ID","Nama / Deskripsi","Kategori","Tanggal","Status","Aksi"}; Object[][] rows={{"AC-001","Pemeriksaan unit pendingin","HVAC","04 Nov 2026","Diproses","Detail"},{"AC-002","Perbaikan lampu koridor","Elektrikal","03 Nov 2026","Menunggu","Detail"},{"AC-003","Pengecekan pompa air","Plumbing","02 Nov 2026","Selesai","Detail"},{"AC-004","Pemeliharaan ruang rapat","Bangunan","01 Nov 2026","Diproses","Detail"}};
        JTable table=new JTable(new DefaultTableModel(rows,cols){@Override public boolean isCellEditable(int r,int c){return false;}}); table.setRowHeight(38);table.setFont(new Font("Segoe UI",Font.PLAIN,12));table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));table.getTableHeader().setBackground(new Color(247,249,252));table.setGridColor(AeroCareTheme.LINE);table.setFillsViewportHeight(true);
        JPanel tableCard=new JPanel(new BorderLayout());tableCard.setBackground(Color.WHITE);tableCard.setBorder(new CompoundBorder(new LineBorder(AeroCareTheme.LINE),new EmptyBorder(14,14,14,14)));JLabel section=new JLabel("Daftar "+page);section.setFont(new Font("Segoe UI",Font.BOLD,14));section.setBorder(new EmptyBorder(0,0,12,0));tableCard.add(section,BorderLayout.NORTH);tableCard.add(new JScrollPane(table),BorderLayout.CENTER);center.add(tableCard,BorderLayout.CENTER);body.add(center,BorderLayout.CENTER);p.add(body,BorderLayout.CENTER);return p;
    }
}
