package com.example.sih.Atmanirbhar.Atmanirbhar;


public class serviceCardView {
    private String JobName;
    private String Description;
    private String Phone;
    private String Location;

    public serviceCardView() {
    }

    public serviceCardView(String jobname, String phone, String description, String location) {
        JobName = jobname;
        Description = description;
        Phone = phone;
        Location = location;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}