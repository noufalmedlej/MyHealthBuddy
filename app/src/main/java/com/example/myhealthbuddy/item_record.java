package com.example.myhealthbuddy;


import java.security.PublicKey;

public class item_record  {

    public String date;
    public String file;
    public String rid;
    public String time;
    public String doctorName;
    public String patientName;
    public boolean ck;
    public String did ;
    public String hname;



    public item_record() {

    }

    public item_record(String date, String file, String rid, String time,String doctorName,String patientName,boolean ck) {
        this.date = date;
        this.file = file;
        this.rid = rid;
        this.time = time;
        this.doctorName=doctorName;
        this.patientName=patientName;
        this.ck=ck;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public boolean isCk() {
        return ck;
    }

    public void setCk(boolean ck) {
        this.ck = ck;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }
}
