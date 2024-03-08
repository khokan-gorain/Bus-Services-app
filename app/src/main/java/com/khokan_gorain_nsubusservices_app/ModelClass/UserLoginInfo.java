package com.khokan_gorain_nsubusservices_app.ModelClass;

public class UserLoginInfo {
    int id;
    String sname,username,sphone_no,smsg,semail_id,spassword,sprofile_img;

    public UserLoginInfo(int id, String sname, String username, String sphone_no, String smsg, String semail_id, String spassword, String sprofile_img) {
        this.id = id;
        this.sname = sname;
        this.username = username;
        this.sphone_no = sphone_no;
        this.smsg = smsg;
        this.semail_id = semail_id;
        this.spassword = spassword;
        this.sprofile_img = sprofile_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSphone_no() {
        return sphone_no;
    }

    public void setSphone_no(String sphone_no) {
        this.sphone_no = sphone_no;
    }

    public String getSmsg() {
        return smsg;
    }

    public void setSmsg(String smsg) {
        this.smsg = smsg;
    }

    public String getSemail_id() {
        return semail_id;
    }

    public void setSemail_id(String semail_id) {
        this.semail_id = semail_id;
    }

    public String getSpassword() {
        return spassword;
    }

    public void setSpassword(String spassword) {
        this.spassword = spassword;
    }

    public String getSprofile_img() {
        return sprofile_img;
    }

    public void setSprofile_img(String sprofile_img) {
        this.sprofile_img = sprofile_img;
    }
}
