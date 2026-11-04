package view.table;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import view.util.BadgeLabel;
import view.util.Theme;
import view.util.UiKit;

/**
 * Renderer sel tabel AEROCARE: teks biasa, lencana status, tombol aksi,
 * dan teks tebal untuk kolom utama. Latar sel ikut baris terpilih dan
 * setiap baris diberi garis pemisah tipis.
 */
public final class RendererTabel {

    private RendererTabel() {
    }

    /** Sel teks abu-abu gelap biasa. */
    public static class Teks extends UiKit.SelBerbaris {

        private final boolean tebal;
        private final int rata;

        public Teks() {
            this(false, SwingConstants.LEFT);
        }

        public Teks(boolean tebal, int rata) {
            this.tebal = tebal;
            this.rata = rata;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            super.getTableCellRendererComponent(tabel, nilai, terpilih, fokus,
                    baris, kolom);
            setFont(tebal ? Theme.bold(12.5f) : Theme.regular(12.5f));
            setForeground(Theme.TEXT);
            setHorizontalAlignment(rata);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setBackground(terpilih ? Theme.SLATE_SOFT : Theme.SURFACE);
            return this;
        }
    }

    /** Sel teks redup untuk kolom sekunder. */
    public static class TeksRedup extends UiKit.SelBerbaris {

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            super.getTableCellRendererComponent(tabel, nilai, terpilih, fokus,
                    baris, kolom);
            setFont(Theme.regular(12.5f));
            setForeground(Theme.TEXT_MUTED);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setBackground(terpilih ? Theme.SLATE_SOFT : Theme.SURFACE);
            return this;
        }
    }

    /** Sel lencana status berwarna. */
    public static class Lencana implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            String teks = nilai == null ? "" : nilai.toString();
            JPanel pembungkus = new PanelBaris(terpilih);
            BadgeLabel badge = new BadgeLabel(teks);
            pembungkus.add(badge);
            return pembungkus;
        }
    }

    /** Sel berisi kumpulan tautan aksi (Ubah, Hapus, Detail, ...). */
    public static class Aksi implements TableCellRenderer {

        private final String[] label;

        public Aksi(String... label) {
            this.label = label;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            JPanel pembungkus = new PanelBaris(terpilih);
            for (String teks : label) {
                pembungkus.add(new BadgeLabel.TautanAksi(teks, warnaUntuk(teks)));
            }
            return pembungkus;
        }

        private java.awt.Color warnaUntuk(String teks) {
            if ("Hapus".equals(teks) || "Nonaktifkan".equals(teks)) {
                return Theme.RED;
            }
            return Theme.ADMIN;
        }
    }

    /**
     * Sel aksi yang isinya dibaca dari model: satu atau beberapa tautan
     * dipisah koma. Nilai kosong atau "-" ditampilkan sebagai tanda hubung
     * redup, dipakai ketika sebuah baris tidak boleh diubah.
     */
    public static class AksiDinamis implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            String isi = nilai == null ? "" : nilai.toString().trim();
            JPanel pembungkus = new PanelBaris(terpilih);
            if (isi.isEmpty() || "-".equals(isi)) {
                javax.swing.JLabel kosong = new javax.swing.JLabel("-");
                kosong.setFont(Theme.regular(12.5f));
                kosong.setForeground(Theme.TEXT_FAINT);
                pembungkus.add(kosong);
                return pembungkus;
            }
            for (String teks : isi.split(",")) {
                String label = teks.trim();
                if (!label.isEmpty()) {
                    pembungkus.add(new BadgeLabel.TautanAksi(label,
                            warnaUntuk(label)));
                }
            }
            return pembungkus;
        }

        private java.awt.Color warnaUntuk(String teks) {
            if ("Hapus".equals(teks) || "Nonaktifkan".equals(teks)) {
                return Theme.RED;
            }
            if ("Selesai".equals(teks)) {
                return Theme.GREEN;
            }
            return Theme.ADMIN;
        }
    }

    /**
     * Kolom pilih pada tabel: tombol radio (satu baris) atau kotak centang
     * (banyak baris). Baris yang dianggap terpilih ditentukan lewat
     * {@link #setTerpilih(java.util.function.IntPredicate)} sehingga renderer
     * bisa dipakai ulang oleh beberapa layar.
     */
    public static class Pilih implements TableCellRenderer {

        private final boolean kotak;
        private final java.awt.Color aksen;
        private java.util.function.IntPredicate terpilih = b -> false;

        public Pilih(boolean kotak, java.awt.Color aksen) {
            this.kotak = kotak;
            this.aksen = aksen == null ? Theme.ADMIN : aksen;
        }

        public void setTerpilih(java.util.function.IntPredicate terpilih) {
            this.terpilih = terpilih == null ? b -> false : terpilih;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean dipilih, boolean fokus, int baris,
                int kolom) {
            boolean aktif = terpilih.test(baris);
            JPanel pembungkus = new PanelBaris(dipilih);
            if (kotak) {
                view.util.TombolRadio.KotakCentang centang
                        = new view.util.TombolRadio.KotakCentang(aksen);
                centang.setTerpilih(aktif);
                pembungkus.add(centang);
            } else {
                view.util.TombolRadio radio = new view.util.TombolRadio();
                radio.setAksen(aksen);
                radio.setTerpilih(aktif);
                pembungkus.add(radio);
            }
            return pembungkus;
        }
    }

    /** Kolom ID kecil berwarna abu. */
    public static class Id extends UiKit.SelBerbaris {

        @Override
        public Component getTableCellRendererComponent(JTable tabel,
                Object nilai, boolean terpilih, boolean fokus, int baris,
                int kolom) {
            super.getTableCellRendererComponent(tabel, nilai, terpilih, fokus,
                    baris, kolom);
            setFont(Theme.regular(12.5f));
            setForeground(Theme.TEXT_MUTED);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setBackground(terpilih ? Theme.SLATE_SOFT : Theme.SURFACE);
            return this;
        }
    }

    /** Sel dengan teks tebal pada kolom pertama. */
    public static class Utama extends Teks {

        public Utama() {
            super(true, SwingConstants.LEFT);
        }
    }

    /** Pembungkus sel yang menata isi dari kiri dan menggambar garis baris. */
    private static class PanelBaris extends JPanel {

        private final boolean terpilih;

        PanelBaris(boolean terpilih) {
            super(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
            this.terpilih = terpilih;
            setOpaque(true);
            setBackground(terpilih ? Theme.SLATE_SOFT : Theme.SURFACE);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(
                    13, 10, 11, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Theme.BORDER_SOFT);
            g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
        }
    }
}
