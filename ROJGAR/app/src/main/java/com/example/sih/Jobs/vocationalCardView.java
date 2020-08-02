package com.example.sih.Jobs;


public class vocationalCardView {
    private String JobName;
    private String Description;
    private String Phone;
    private String Days;

    public vocationalCardView() {
    }

    public vocationalCardView(String jobname, String description, String phone, String days) {
        JobName = jobname ;
        Description = description;
        Phone = phone;
        Days = days;
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

    public String getDays() {
        return "Number of days: " + Days;
    }

}