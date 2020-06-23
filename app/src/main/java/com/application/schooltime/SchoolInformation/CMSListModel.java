package com.application.schooltime.SchoolInformation;

public class CMSListModel {
    private String schoolName ;
    private String schoolUrl;

    public CMSListModel(String schoolName, String schoolUrl) {
        this.schoolName = schoolName;
        this.schoolUrl = schoolUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolUrl() {
        return schoolUrl;
    }
}
