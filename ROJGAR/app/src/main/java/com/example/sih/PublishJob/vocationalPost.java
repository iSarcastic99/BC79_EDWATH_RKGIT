package com.example.sih.PublishJob;

public class vocationalPost {
    private String JobName;
    private String Description;
    private String Days;

    public vocationalPost(){

    }

    public vocationalPost(String JobName, String Description, String Days) {
        this.JobName = JobName;
        this.Description = Description;
        this.Days = Days;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String JobName) {
        this.JobName = JobName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String Days) {
        this.Days = Days;
    }

}
