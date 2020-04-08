package com.example.myhealthbuddy;

public class HCPsResult {


    public String name;
    public String id;
    public String phone;
    public String specialty;
    public String gender;
    public String img;


    public HCPsResult() {

    }
    public HCPsResult(String name, String id, String phone, String specialty, String gender) {
        this.name = name;
        this.id = id;
        this.phone= phone;

        this.specialty= specialty;
        this.gender= gender;

        // add img paramter also
        //this.img=img;

    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}






