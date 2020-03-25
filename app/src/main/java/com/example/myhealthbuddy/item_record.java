package com.example.myhealthbuddy;

public class item_record {

    public String date;
    public String did;
    public String file;
    public String pid;
    public String rid;
    public String time;

    public item_record() {

    }

    public item_record(String date, String did, String file, String pid, String rid, String time) {
        this.date = date;
        this.did = did;
        this.file = file;
        this.pid = pid;
        this.rid = rid;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
}
