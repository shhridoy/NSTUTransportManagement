package com.shhridoy.nstutransportmanagement.myObjects;

public class Profile {

    private String name = "";
    private String designation = "";
    private String gender = "";
    private String mobile = "";
    private String email = "";
    private String password = "";

    public Profile(String name, String designation, String gender, String mobile, String email, String password) {
        this.name = name;
        this.designation = designation;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public Profile() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}