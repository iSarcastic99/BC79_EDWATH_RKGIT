package com.example.sih.Jobs;

public class vocationalCardView {
    private String JobName;
    private String Description;
    private String Phone;
    private String Location;

    public vocationalCardView() {
    }

    public vocationalCardView(String jobname, String description, String phone, String location) {
        JobName = jobname ;
        Description = description;
        Phone = phone;
        Location = location;
    }

    public String getJobName() {
        return "Job Title: " + JobName;
    }

    public String getDescription() {
        return "Description: " + Description;
    }

    public String getPhone() {
        return "Phone: " + Phone;
    }

    public String getLocation() {
        return "Location: " + Location;
    }

}