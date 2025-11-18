package com.example.healthcare.Models;

public class HealthRecord {
    private int userId;
    private String medicalHistory;
    private String testResults;
    private String allergies;
    private String vaccinations;
    private String additionalDetails;

    public HealthRecord(int userId, String medicalHistory, String testResults, String allergies, String vaccinations, String additionalDetails) {
        this.userId = userId;
        this.medicalHistory = medicalHistory;
        this.testResults = testResults;
        this.allergies = allergies;
        this.vaccinations = vaccinations;
        this.additionalDetails = "";
    }

    // Getters and setters for the properties

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getTestResults() {
        return testResults;
    }

    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(String vaccinations) {
        this.vaccinations = vaccinations;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}


