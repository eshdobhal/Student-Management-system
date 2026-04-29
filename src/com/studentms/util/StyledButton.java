package com.studentms.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private boolean hovered = false;

    public StyledButton(String text, Color base, Color hover) {
        super(text);
        this.baseColor = base;
        this.hoverColor = hover;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(Theme.FONT_HEADER);
        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(130, 36));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hovered ? hoverColor : baseColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.ARC, Theme.ARC);
        super.paintComponent(g);
        g2.dispose();
    }
}
