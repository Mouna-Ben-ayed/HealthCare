package com.example.healthcare.Models;

public class Doctor {
    private int id=0;
    private String firstName;
    private String lastName;
    private String phone;


    private String address;

    public Doctor(String firstName, String lastName, String phone, String address) {
        this.id ++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id=id ;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

