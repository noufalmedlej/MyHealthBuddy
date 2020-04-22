package com.example.myhealthbuddy;


import java.security.PublicKey;

public class item_record  {

    public String date;
    public String file;
    public String rid;
    public String time;
    public String doctorName;
    public boolean ck;
    public String did ;
    public String pid ;
    public String hospital;
    public int type;



    public item_record() {

    }

    public item_record(String date, String file, String rid, String time,String doctorName,boolean ck,String did,String pid ,int type,String hospital) {
        this.date = date;
        this.file = file;
        this.rid = rid;
        this.time = time;
        this.doctorName=doctorName;
        this.ck=ck;
        this.type=type;
        this.hospital=hospital;
        this.pid=pid;
        this.did=did;

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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hname) {
        this.hospital = hname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String puid) {
        this.pid = puid;
    }
}
