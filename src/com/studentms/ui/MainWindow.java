package com.studentms.ui;

import com.studentms.dao.StudentDAO;
import com.studentms.model.Student;
import com.studentms.util.StyledButton;
import com.studentms.util.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainWindow extends JFrame {

    private StudentDAO dao = StudentDAO.getInstance();
    private StudentTableModel tableModel = new StudentTableModel();
    private JTable table;
    private JTextField searchField;
    private JLabel lblTotal, lblLow, lblSelected;

    public MainWindow() {
        super("Student Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        buildUI();
        refreshTable(null);
    }

    // ─── Layout ───────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG_DARK);
        setContentPane(root);

        root.add(buildSidebar(),   BorderLayout.WEST);
        root.add(buildMainArea(),  BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.BG_CARD);
        side.setPreferredSize(new Dimension(220, 0));
        side.setBorder(new EmptyBorder(30, 18, 30, 18));

        // Logo
        JLabel logo = new JLabel("SMS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 36));
        logo.setForeground(Theme.ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);

        JLabel subtitle = new JLabel("Student Management");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(subtitle);
        side.add(Box.createVerticalStrut(32));

        // Stat cards
        side.add(buildStatCard("Total Students", null, true));
        side.add(Box.createVerticalStrut(12));
        side.add(buildStatCard("Low Attendance", null, false));
        side.add(Box.createVerticalStrut(32));

        // Nav hint
        JLabel hint = new JLabel("ACTIONS");
        hint.setFont(Theme.FONT_BADGE);
        hint.setForeground(Theme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(hint);
        side.add(Box.createVerticalStrut(10));

        side.add(makeSideBtn("➕  Add Student",   this::onAdd));
        side.add(Box.createVerticalStrut(8));
        side.add(makeSideBtn("✏️  Edit Selected", this::onEdit));
        side.add(Box.createVerticalStrut(8));
        side.add(makeSideBtn("🗑  Delete",         this::onDelete));
        side.add(Box.createVerticalStrut(8));
        side.add(makeSideBtn("🔄  Refresh",        e -> refreshTable(null)));

        side.add(Box.createVerticalGlue());
        JLabel copy = new JLabel("© 2025 SMS System");
        copy.setFont(Theme.FONT_SMALL);
        copy.setForeground(Theme.BORDER);
        copy.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(copy);
        return side;
    }

    private JPanel buildStatCard(String label, String val, boolean isTotal) {
        JPanel card = new JPanel(new BorderLayout(0, 2));
        card.setBackground(Theme.BG_DARK);
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(isTotal ? "0" : "0");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(isTotal ? Theme.ACCENT : Theme.DANGER);
        card.add(lbl, BorderLayout.CENTER);

        JLabel desc = new JLabel(label);
        desc.setFont(Theme.FONT_SMALL);
        desc.setForeground(Theme.TEXT_MUTED);
        card.add(desc, BorderLayout.SOUTH);

        if (isTotal) lblTotal = lbl; else lblLow = lbl;
        return card;
    }

    private JButton makeSideBtn(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_PRIMARY);
        btn.setBackground(Theme.BG_DARK);
        btn.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(9, 14, 9, 14)
        ));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(Theme.BG_ROW_ALT); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(Theme.BG_DARK); }
        });
        btn.addActionListener(al);
        return btn;
    }

    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(Theme.BG_DARK);
        main.setBorder(new EmptyBorder(24, 24, 24, 24));

        main.add(buildTopBar(),     BorderLayout.NORTH);
        main.add(buildTablePanel(), BorderLayout.CENTER);
        main.add(buildStatusBar(),  BorderLayout.SOUTH);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel heading = new JLabel("All Students");
        heading.setFont(Theme.FONT_TITLE);
        heading.setForeground(Theme.TEXT_PRIMARY);
        bar.add(heading, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setBackground(Theme.BG_DARK);

        searchField = new JTextField(20);
        searchField.setBackground(Theme.BG_CARD);
        searchField.setForeground(Theme.TEXT_PRIMARY);
        searchField.setCaretColor(Theme.ACCENT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        searchField.setFont(Theme.FONT_BODY);
        searchField.putClientProperty("JTextField.placeholderText", "Search name, SAP ID, batch…");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { refreshTable(searchField.getText()); }
        });

        StyledButton btnAdd = new StyledButton("+ Add Student", Theme.ACCENT, Theme.ACCENT_HOVER);
        btnAdd.addActionListener(this::onAdd);

        right.add(searchField);
        right.add(btnAdd);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JScrollPane buildTablePanel() {
        table = new JTable(tableModel);
        table.setBackground(Theme.BG_CARD);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(42);
        table.setGridColor(Theme.BORDER);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(Theme.SELECTION);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.TABLE_HEADER);
        header.setForeground(Theme.TEXT_MUTED);
        header.setFont(Theme.FONT_HEADER);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.ACCENT));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        // Column widths
        int[] widths = {40, 170, 110, 140, 120, 110, 180, 100};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Attendance column renderer (color-coded)
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                                                            boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String s = val != null ? val.toString() : "0%";
                double pct = 0;
                try { pct = Double.parseDouble(s.replace("%", "")); } catch (Exception ignored) {}
                lbl.setForeground(pct >= 75 ? Theme.SUCCESS : Theme.DANGER);
                lbl.setFont(Theme.FONT_HEADER);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBackground(sel ? Theme.SELECTION : Theme.BG_CARD);
                lbl.setOpaque(true);
                return lbl;
            }
        });

        // Alternating row renderer
        DefaultTableCellRenderer altRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                                                            boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? Theme.SELECTION : (row % 2 == 0 ? Theme.BG_CARD : Theme.BG_ROW_ALT));
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setOpaque(true);
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(altRenderer);
        }

        // Double-click to edit
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) onEdit(null);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int sel = table.getSelectedRow();
            if (lblSelected != null)
                lblSelected.setText(sel >= 0 ? "Selected: Row " + (sel + 1) : "No selection");
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(Theme.BG_DARK);
        scroll.getViewport().setBackground(Theme.BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.getVerticalScrollBar().setBackground(Theme.BG_CARD);
        return scroll;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG_CARD);
        bar.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(6, 14, 6, 14)
        ));

        lblSelected = new JLabel("No selection");
        lblSelected.setFont(Theme.FONT_SMALL);
        lblSelected.setForeground(Theme.TEXT_MUTED);
        bar.add(lblSelected, BorderLayout.WEST);

        JLabel hint = new JLabel("Double-click a row to edit  •  Red attendance = below 75%");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.BORDER);
        bar.add(hint, BorderLayout.EAST);
        return bar;
    }

    // ─── Data & Actions ───────────────────────────────────────────────────────

    private void refreshTable(String query) {
        List<Student> list = (query == null || query.isEmpty())
            ? dao.getAllStudents()
            : dao.search(query);
        tableModel.setData(list);

        lblTotal.setText(String.valueOf(dao.getTotalCount()));
        lblLow.setText(String.valueOf(dao.getLowAttendanceCount()));
    }

    private void onAdd(ActionEvent e) {
        StudentFormDialog dlg = new StudentFormDialog(this, null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            dao.addStudent(dlg.getStudent());
            refreshTable(searchField.getText());
        }
    }

    private void onEdit(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Student selected = tableModel.getStudentAt(row);
        Student copy = cloneStudent(selected);

        StudentFormDialog dlg = new StudentFormDialog(this, copy);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            dao.updateStudent(dlg.getStudent());
            refreshTable(searchField.getText());
        }
    }

    private void onDelete(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Student s = tableModel.getStudentAt(row);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete student: " + s.getName() + " (SAP: " + s.getSapId() + ")?\n\nThis action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteStudent(s.getId());
            refreshTable(searchField.getText());
        }
    }

    private Student cloneStudent(Student s) {
        return new Student(s.getId(), s.getName(), s.getSapId(), s.getBatch(),
            s.getPhoneNo(), s.getAttendance(), s.getEmail(), s.getCourse());
    }
}
