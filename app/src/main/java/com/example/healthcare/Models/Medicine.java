package com.example.healthcare.Models;

public class Medicine {
    private long  medicineId;
    private int userId; // Clé étrangère vers User
    private String name;
    private int hour;
    private int minute;

    public Medicine( int userId, String name, int hour, int minute) {

        this.userId = userId;
        this.name = name;
        this.hour = hour;
        this.minute = minute;
    }

    // Getters and Setters
    public long  getId() { return medicineId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }

    public void setMedicineId(long  id) { this.medicineId = id; }
    public void setName(String name) { this.name = name; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
}
