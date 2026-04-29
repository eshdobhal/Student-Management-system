package com.studentms;

import com.studentms.ui.MainWindow;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set Flat Dark Look & Feel or fall back to system
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Global UI defaults
        UIManager.put("OptionPane.background",        new java.awt.Color(22, 28, 56));
        UIManager.put("Panel.background",             new java.awt.Color(22, 28, 56));
        UIManager.put("OptionPane.messageForeground", new java.awt.Color(230, 235, 255));

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
