package com.example.sih.PublishJob;

public class vocationalPost {
    private String JobName;
    private String Description;
    private String Days;
    private String Phone;

    public vocationalPost() {
    }

    public vocationalPost(String jobName, String description, String days, String phone) {
        JobName = jobName;
        Description = description;
        Days = days;
        Phone = phone;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
