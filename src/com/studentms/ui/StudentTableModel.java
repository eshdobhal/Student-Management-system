package com.studentms.ui;

import com.studentms.model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private static final String[] COLUMNS = {
        "#", "Name", "SAP ID", "Course", "Batch", "Phone", "Email", "Attendance (%)"
    };

    private List<Student> data = new ArrayList<>();

    public void setData(List<Student> students) {
        this.data = new ArrayList<>(students);
        fireTableDataChanged();
    }

    public Student getStudentAt(int row) {
        return data.get(row);
    }

    @Override public int getRowCount()    { return data.size(); }
    @Override public int getColumnCount() { return COLUMNS.length; }
    @Override public String getColumnName(int col) { return COLUMNS[col]; }
    @Override public boolean isCellEditable(int r, int c) { return false; }

    @Override
    public Object getValueAt(int row, int col) {
        Student s = data.get(row);
        switch (col) {
            case 0: return row + 1;
            case 1: return s.getName();
            case 2: return s.getSapId();
            case 3: return s.getCourse();
            case 4: return s.getBatch();
            case 5: return s.getPhoneNo();
            case 6: return s.getEmail() != null ? s.getEmail() : "—";
            case 7: return String.format("%.1f%%", s.getAttendance());
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return col == 0 ? Integer.class : String.class;
    }
}
