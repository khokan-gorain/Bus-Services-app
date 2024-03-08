package com.khokan_gorain_nsubusservices_app.ModelClass;

public class TeacherRouthInchargeList {
    String teacherName,areaName,profileImg,phoneNumber;
    int status,id;


    public TeacherRouthInchargeList(String teacherName, String areaName, String profileImg, String phoneNumber, int status, int id) {
        this.teacherName = teacherName;
        this.areaName = areaName;
        this.profileImg = profileImg;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
