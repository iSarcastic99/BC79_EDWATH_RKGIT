package com.example.sih.PublishJob;

import com.example.sih.Registration.Login;

public class vocationalPost {
    private String JobName;
    private String Description;
    private String Location;
    private String Phone;

    public vocationalPost() {
    }

    public vocationalPost(String jobName, String description, String location, String phone) {
        JobName = jobName;
        Description = description;
        Location = location;
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
        return Location;
    }

    public void setDays(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
