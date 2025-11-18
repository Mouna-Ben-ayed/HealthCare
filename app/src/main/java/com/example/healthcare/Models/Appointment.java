package com.example.healthcare.Models;

public class Appointment {
    private int id;
    private String doctorName;
    private String appointmentDateTime;

    public Appointment(int id, String doctorName, String appointmentDateTime) {
        this.id = id;
        this.doctorName = doctorName;
        this.appointmentDateTime = appointmentDateTime;
    }

    public int getId() {
        return id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    @Override
    public String toString() {
        return doctorName + " - " + appointmentDateTime;
    }
}
