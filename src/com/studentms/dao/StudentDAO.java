package com.studentms.dao;

import com.studentms.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDAO {
    private static StudentDAO instance;
    private List<Student> students = new ArrayList<>();
    private int nextId = 1;

    private StudentDAO() {
        loadSampleData();
    }

    public static StudentDAO getInstance() {
        if (instance == null) instance = new StudentDAO();
        return instance;
    }

    private void loadSampleData() {
        addStudent(new Student(0, "Aarav Sharma",    "500123456", "B-Tech CSE 2022", "9876543210", 85.5, "aarav@email.com",   "B.Tech CSE"));
        addStudent(new Student(0, "Priya Mehta",     "500234567", "B-Tech IT 2022",  "9876543211", 91.0, "priya@email.com",   "B.Tech IT"));
        addStudent(new Student(0, "Rahul Gupta",     "500345678", "B-Tech ECE 2021", "9876543212", 73.2, "rahul@email.com",   "B.Tech ECE"));
        addStudent(new Student(0, "Sneha Patel",     "500456789", "B-Tech CSE 2023", "9876543213", 88.0, "sneha@email.com",   "B.Tech CSE"));
        addStudent(new Student(0, "Karan Singh",     "500567890", "MBA 2022",        "9876543214", 60.5, "karan@email.com",   "MBA"));
        addStudent(new Student(0, "Anjali Verma",    "500678901", "B-Tech IT 2021",  "9876543215", 95.0, "anjali@email.com",  "B.Tech IT"));
        addStudent(new Student(0, "Rohit Kumar",     "500789012", "B-Tech CSE 2022", "9876543216", 78.3, "rohit@email.com",   "B.Tech CSE"));
        addStudent(new Student(0, "Deepika Nair",    "500890123", "BCA 2023",        "9876543217", 82.1, "deepika@email.com", "BCA"));
    }

    public Student addStudent(Student s) {
        s.setId(nextId++);
        students.add(s);
        return s;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public boolean updateStudent(Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updated.getId()) {
                students.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    public List<Student> search(String query) {
        String q = query.toLowerCase().trim();
        if (q.isEmpty()) return getAllStudents();
        return students.stream().filter(s ->
            s.getName().toLowerCase().contains(q) ||
            s.getSapId().toLowerCase().contains(q) ||
            s.getBatch().toLowerCase().contains(q) ||
            s.getCourse().toLowerCase().contains(q) ||
            s.getPhoneNo().contains(q)
        ).collect(Collectors.toList());
    }

    public int getTotalCount() { return students.size(); }

    public long getLowAttendanceCount() {
        return students.stream().filter(s -> s.getAttendance() < 75).count();
    }
}
