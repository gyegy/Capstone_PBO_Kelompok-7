package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.JTableHeader;

/**
 * Kumpulan helper tampilan: menyiapkan combo box, tabel, dan scroll pane
 * agar seragam di semua form AEROCARE.
 */
public final class UiKit {

    private UiKit() {
    }

    // ---------- Combo box ----------
    public static void siapkanCombo(JComboBox<?> combo) {
        combo.setOpaque(false);
        combo.setBackground(Theme.SURFACE);
        combo.setForeground(Theme.TEXT);
        combo.setFont(Theme.regular(13f));
        combo.setBorder(new EmptyBorder(2, 8, 2, 4));
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        combo.setUI(new ComboKotakUI());
        combo.setRenderer(new ComboRenderer());
    }

    // ---------- Tabel ----------
    public static void siapkanTabel(JTable tabel) {
        rapikanWadahTabel(tabel);
        tabel.setBackground(Theme.SURFACE);
        tabel.setForeground(Theme.TEXT);
        tabel.setFont(Theme.regular(12.5f));
        tabel.setRowHeight(48);
        tabel.setShowGrid(false);
        tabel.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tabel.setSelectionBackground(Theme.SLATE_SOFT);
        tabel.setSelectionForeground(Theme.TEXT);
        tabel.setFillsViewportHeight(true);
        tabel.setFocusable(false);
        tabel.setRowSelectionAllowed(true);
        tabel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JTableHeader header = tabel.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new java.awt.Dimension(0, 40));
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.TEXT_MUTED);
        header.setFont(Theme.bold(11.5f));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBorder(null);
    }

    /**
     * Membuat latar scroll pane dan viewport-nya sewarna tabel sehingga tidak
     * ada kotak abu bawaan Look and Feel pada area tabel yang masih kosong.
     */
    private static void rapikanWadahTabel(JTable tabel) {
        javax.swing.JViewport viewport = (javax.swing.JViewport) tabel
                .getParent();
        if (viewport == null) {
            return;
        }
        viewport.setBackground(Theme.SURFACE);
        viewport.setOpaque(true);
        if (viewport.getParent() instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) viewport.getParent();
            scroll.setBackground(Theme.SURFACE);
            scroll.setOpaque(true);
            scroll.getViewport().setOpaque(true);
            scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
            scroll.setViewportBorder(null);
        }
    }

    public static JScrollPane bungkusTabel(JTable tabel) {
        JScrollPane scroll = new JScrollPane(tabel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(Theme.SURFACE);
        scroll.setBackground(Theme.SURFACE);
        scroll.setViewportBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getVerticalScrollBar().setPreferredSize(
                new java.awt.Dimension(8, 0));
        scroll.getVerticalScrollBar().setUI(new ScrollBarTipis());
        scroll.getVerticalScrollBar().setOpaque(false);
        return scroll;
    }

    // ---------- Label ----------
    public static void labelTebal(javax.swing.JLabel label, float ukuran) {
        label.setFont(Theme.bold(ukuran));
        label.setForeground(Theme.TEXT);
    }

    /**
     * Membuat label membungkus teksnya mengikuti lebar yang diberikan. Dipakai
     * untuk teks panjang di dalam layout absolut, karena Swing tidak
     * membungkus teks JLabel sendiri. Pembungkusan diulang otomatis setiap
     * kali teks label diganti.
     */
    public static void labelBungkus(final javax.swing.JLabel label, int lebar) {
        label.putClientProperty("lebarBungkus",
                Integer.valueOf(Math.max(40, lebar - 4)));
        if (label.getClientProperty("pantauBungkus") == null) {
            label.putClientProperty("pantauBungkus", Boolean.TRUE);
            label.addPropertyChangeListener("text", e -> {
                Object teks = e.getNewValue();
                if (teks instanceof String
                        && !((String) teks).startsWith("<html>")) {
                    label.putClientProperty("teksMentah", teks);
                    terapkanBungkus(label);
                }
            });
        }
        Object teks = label.getText();
        if (teks instanceof String && !((String) teks).startsWith("<html>")) {
            label.putClientProperty("teksMentah", teks);
        }
        terapkanBungkus(label);
    }

    private static void terapkanBungkus(javax.swing.JLabel label) {
        Object lebar = label.getClientProperty("lebarBungkus");
        Object teks = label.getClientProperty("teksMentah");
        if (lebar instanceof Integer && teks instanceof String) {
            label.setText("<html><body style=\"width:" + lebar + "\">" + teks
                    + "</body></html>");
        }
    }

    // ---------- Helper teks ----------
    public static String atau(String nilai, String cadangan) {
        if (nilai == null || nilai.trim().isEmpty()) {
            return cadangan;
        }
        return nilai;
    }

    public static String inisial(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            return "?";
        }
        String bersih = nama.trim();
        String[] bagian = bersih.split("\\s+");
        if (bagian.length == 1) {
            return bagian[0].substring(0, 1).toUpperCase();
        }
        return (bagian[0].charAt(0) + "" + bagian[1].charAt(0)).toUpperCase();
    }

    // ---------- Kelas dalam ----------
    /** Combo box bersudut tumpul dengan panah sendiri. */
    private static class ComboKotakUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton panah = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.TEXT_MUTED);
                    int cx = getWidth() / 2 - 3;
                    int cy = getHeight() / 2 - 1;
                    g2.setStroke(new BasicStroke(1.6f,
                            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx, cy, cx + 3, cy + 3);
                    g2.drawLine(cx + 3, cy + 3, cx + 6, cy);
                    g2.dispose();
                }
            };
            panah.setBorder(null);
            panah.setContentAreaFilled(false);
            panah.setFocusPainted(false);
            panah.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return panah;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g,
                java.awt.Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.SURFACE);
            g2.fill(new RoundRectangle2D.Float(bounds.x, bounds.y, bounds.width,
                    bounds.height, 9, 9));
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(bounds.x + 0.5f, bounds.y + 0.5f,
                    bounds.width - 1f, bounds.height - 1f, 9, 9));
            g2.dispose();
        }
    }

    /** Renderer item combo box agar teks rata kiri dan berjarak. */
    private static class ComboRenderer
            extends javax.swing.DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                javax.swing.JList<?> list, Object value, int index,
                boolean terpilih, boolean fokus) {
            super.getListCellRendererComponent(list, value, index, terpilih,
                    fokus);
            setFont(Theme.regular(13f));
            setBorder(new EmptyBorder(4, 10, 4, 10));
            if (terpilih) {
                setBackground(Theme.ADMIN_SOFT);
                setForeground(Theme.TEXT);
            } else {
                setBackground(Theme.SURFACE);
                setForeground(Theme.TEXT);
            }
            return this;
        }
    }

    /** Header tabel bergaya Figma: latar terang, teks abu kecil. */
    public static class HeaderRenderer
            extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            JComponent komponen = (JComponent) super.getTableCellRendererComponent(
                    tabel, nilai, terpilih, fokus, baris, kolom);
            setOpaque(true);
            setBackground(Theme.HEADER_TABEL);
            setForeground(Theme.TEXT_MUTED);
            setFont(Theme.bold(11.5f));
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            return komponen;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Theme.BORDER);
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    }

    /** Renderer dasar dengan garis pemisah tipis di bawah setiap baris. */
    public static abstract class SelBerbaris
            extends javax.swing.table.DefaultTableCellRenderer {

        protected SelBerbaris() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Theme.BORDER_SOFT);
            g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
        }
    }

    /** Scrollbar ramping tanpa tombol panah. */
    public static class ScrollBarTipis extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(0xCBD5E1);
            trackColor = Theme.SURFACE;
        }

        @Override
        protected JButton createDecreaseButton(int orientasi) {
            return tombolKosong();
        }

        @Override
        protected JButton createIncreaseButton(int orientasi) {
            return tombolKosong();
        }

        private JButton tombolKosong() {
            JButton b = new JButton();
            b.setPreferredSize(new java.awt.Dimension(0, 0));
            b.setMinimumSize(new java.awt.Dimension(0, 0));
            b.setMaximumSize(new java.awt.Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c,
                java.awt.Rectangle kotakThumb) {
            if (kotakThumb.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0xCBD5E1));
            g2.fill(new RoundRectangle2D.Float(kotakThumb.x + 1,
                    kotakThumb.y, kotakThumb.width - 2, kotakThumb.height, 6, 6));
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c,
                java.awt.Rectangle kotakTrack) {
            g.setColor(Theme.SURFACE);
            g.fillRect(kotakTrack.x, kotakTrack.y, kotakTrack.width,
                    kotakTrack.height);
        }
    }

    /** Field kata sandi dengan tampilan serupa RoundedTextField. */
    public static class RoundedPasswordField extends JPasswordField {

        private String placeholder = "";
        private Color garis = Theme.BORDER;
        private Color garisFokus = Theme.ADMIN;

        public RoundedPasswordField() {
            setOpaque(false);
            setBorder(new EmptyBorder(7, 13, 7, 13));
            setFont(Theme.regular(13f));
            setForeground(Theme.TEXT);
            setCaretColor(Theme.TEXT);
            setEchoChar('•');
        }

        public void setPlaceholder(String teks) {
            this.placeholder = teks == null ? "" : teks;
            repaint();
        }

        public void setWarnaGarisFokus(Color warna) {
            this.garisFokus = warna;
            repaint();
        }

        @Override
        public Insets getInsets() {
            return new Insets(7, 13, 7, 13);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(Theme.SURFACE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 9, 9));
            g2.setColor(hasFocus() ? garisFokus : garis);
            g2.setStroke(new BasicStroke(hasFocus() ? 1.6f : 1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f,
                    9, 9));
            g2.dispose();

            super.paintComponent(g);

            if (getPassword().length == 0 && !placeholder.isEmpty()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setColor(Theme.TEXT_FAINT);
                g3.setFont(getFont());
                int y = (getHeight() + g3.getFontMetrics().getAscent()) / 2 - 2;
                g3.drawString(placeholder, 13, y);
                g3.dispose();
            }
        }
    }

    /** Area teks bersudut tumpul untuk deskripsi dan catatan. */
    public static class RoundedTextArea extends javax.swing.JTextArea {

        private String placeholder = "";

        public RoundedTextArea() {
            setOpaque(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setBorder(new EmptyBorder(9, 13, 9, 13));
            setFont(Theme.regular(13f));
            setForeground(Theme.TEXT);
            setCaretColor(Theme.TEXT);
            setSelectionColor(Theme.ADMIN_SOFT);
        }

        public void setPlaceholder(String teks) {
            this.placeholder = teks == null ? "" : teks;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.SURFACE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                    9, 9));
            g2.setColor(hasFocus() ? Theme.ADMIN : Theme.BORDER);
            g2.setStroke(new BasicStroke(hasFocus() ? 1.6f : 1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f,
                    getHeight() - 1f, 9, 9));
            g2.dispose();

            super.paintComponent(g);

            if (getText().isEmpty() && !placeholder.isEmpty()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setColor(Theme.TEXT_FAINT);
                g3.setFont(getFont());
                g3.drawString(placeholder, 13, 26);
                g3.dispose();
            }
        }
    }
}
