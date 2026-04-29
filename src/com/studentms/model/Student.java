package com.studentms.model;

public class Student {
    private int id;
    private String name;
    private String sapId;
    private String batch;
    private String phoneNo;
    private double attendance;
    private String email;
    private String course;

    public Student() {}

    public Student(int id, String name, String sapId, String batch, String phoneNo,
                   double attendance, String email, String course) {
        this.id = id;
        this.name = name;
        this.sapId = sapId;
        this.batch = batch;
        this.phoneNo = phoneNo;
        this.attendance = attendance;
        this.email = email;
        this.course = course;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSapId() { return sapId; }
    public void setSapId(String sapId) { this.sapId = sapId; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public double getAttendance() { return attendance; }
    public void setAttendance(double attendance) { this.attendance = attendance; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', sapId='" + sapId + "'}";
    }
}
