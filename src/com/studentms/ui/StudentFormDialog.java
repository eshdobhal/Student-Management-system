package com.studentms.ui;

import com.studentms.model.Student;
import com.studentms.util.Theme;
import com.studentms.util.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentFormDialog extends JDialog {
    private JTextField tfName, tfSapId, tfBatch, tfPhone, tfEmail, tfAttendance;
    private JComboBox<String> cbCourse;
    private boolean confirmed = false;
    private Student student;

    private static final String[] COURSES = {
        "B.Tech CSE", "B.Tech IT", "B.Tech ECE", "B.Tech EEE",
        "B.Tech Mechanical", "MBA", "MCA", "BCA", "B.Sc CS"
    };

    public StudentFormDialog(Frame parent, Student existing) {
        super(parent, existing == null ? "Add New Student" : "Edit Student", true);
        this.student = existing != null ? existing : new Student();
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        getContentPane().setBackground(Theme.BG_CARD);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG_CARD);
        root.setBorder(new EmptyBorder(24, 30, 20, 30));

        // Title
        JLabel title = new JLabel(student.getId() == 0 ? "Add New Student" : "Edit Student");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        root.add(title, BorderLayout.NORTH);

        // Form grid
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 12);

        tfName       = makeField(student.getName());
        tfSapId      = makeField(student.getSapId());
        tfBatch      = makeField(student.getBatch());
        tfPhone      = makeField(student.getPhoneNo());
        tfEmail      = makeField(student.getEmail());
        tfAttendance = makeField(student.getId() == 0 ? "" : String.valueOf(student.getAttendance()));
        cbCourse     = makeCourseCombo();

        addRow(form, gbc, 0, "Full Name *",     tfName);
        addRow(form, gbc, 1, "SAP ID *",        tfSapId);
        addRow(form, gbc, 2, "Course *",         cbCourse);
        addRow(form, gbc, 3, "Batch *",          tfBatch);
        addRow(form, gbc, 4, "Phone Number *",   tfPhone);
        addRow(form, gbc, 5, "Email",            tfEmail);
        addRow(form, gbc, 6, "Attendance (%) *", tfAttendance);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_CARD);
        btnPanel.setBorder(new EmptyBorder(18, 0, 0, 0));

        StyledButton btnCancel = new StyledButton("Cancel", Theme.BG_ROW_ALT, Theme.BORDER);
        btnCancel.setPreferredSize(new Dimension(100, 36));
        btnCancel.addActionListener(e -> dispose());

        StyledButton btnSave = new StyledButton("Save", Theme.ACCENT, Theme.ACCENT_HOVER);
        btnSave.setPreferredSize(new Dimension(100, 36));
        btnSave.addActionListener(e -> save());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JTextField makeField(String value) {
        JTextField tf = new JTextField(value != null ? value : "", 22);
        tf.setBackground(Theme.BG_DARK);
        tf.setForeground(Theme.TEXT_PRIMARY);
        tf.setCaretColor(Theme.ACCENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setFont(Theme.FONT_BODY);
        return tf;
    }

    private JComboBox<String> makeCourseCombo() {
        JComboBox<String> cb = new JComboBox<>(COURSES);
        cb.setBackground(Theme.BG_DARK);
        cb.setForeground(Theme.TEXT_PRIMARY);
        cb.setFont(Theme.FONT_BODY);
        if (student.getCourse() != null) cb.setSelectedItem(student.getCourse());
        return cb;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setPreferredSize(new Dimension(145, 30));
        form.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(field, gbc);
    }

    private void save() {
        String name  = tfName.getText().trim();
        String sapId = tfSapId.getText().trim();
        String batch = tfBatch.getText().trim();
        String phone = tfPhone.getText().trim();
        String attStr = tfAttendance.getText().trim();

        if (name.isEmpty() || sapId.isEmpty() || batch.isEmpty() || phone.isEmpty() || attStr.isEmpty()) {
            showError("Please fill all required (*) fields.");
            return;
        }
        if (!phone.matches("\\d{10}")) {
            showError("Phone number must be exactly 10 digits.");
            return;
        }
        double att;
        try {
            att = Double.parseDouble(attStr);
            if (att < 0 || att > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Attendance must be a number between 0 and 100.");
            return;
        }

        student.setName(name);
        student.setSapId(sapId);
        student.setBatch(batch);
        student.setPhoneNo(phone);
        student.setEmail(tfEmail.getText().trim());
        student.setAttendance(att);
        student.setCourse((String) cbCourse.getSelectedItem());

        confirmed = true;
        dispose();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() { return confirmed; }
    public Student getStudent()  { return student; }
}
