
package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;

public class PrioritasRenderer extends JLabel implements TableCellRenderer {

    public PrioritasRenderer() {
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        setText(value == null ? "" : value.toString());

        switch (getText()) {
            case "Tinggi":
                setForeground(new Color(220, 38, 38));
                break;
            case "Sedang":
                setForeground(new Color(180, 83, 9));
                break;
            case "Rendah":
                setForeground(new Color(21, 128, 61));
                break;
            default:
                setForeground(Color.BLACK);
                break;
        }

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        Color warnaBadge;

        switch (getText()) {
            case "Tinggi":
                warnaBadge = new Color(254, 226, 226);
                break;
            case "Sedang":
                warnaBadge = new Color(254, 243, 199);
                break;
            case "Rendah":
                warnaBadge = new Color(220, 252, 231);
                break;
            default:
                warnaBadge = Color.WHITE;
                break;
        }

        g2.setColor(warnaBadge);
        g2.fillRoundRect(
                5, 5, getWidth() - 10, getHeight() - 10, 20, 20
        );

        g2.dispose();

        super.paintComponent(g);
    }
}
