package com.ibrahim.mibrahim.attapp;

/**
 * Created by M.Ibrahim on 3/25/2017.
 */

public class QrDataBean {


    private String id;
    private String qrdata;
    private String type;
    private String recorded_id;
    private String timestamp;
    private String device_id;
    private String user_id;
    private String comment;
    private String note;

    public QrDataBean(String id, String qrdata, String type, String recorded_id, String timestamp,
                      String device_id, String user_id, String comment, String note, String status) {
        this.id = id;
        this.qrdata = qrdata;
        this.type = type;
        this.recorded_id = recorded_id;
        this.timestamp = timestamp;
        this.device_id = device_id;
        this.user_id = user_id;
        this.comment = comment;
        this.note = note;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrdata() {
        return qrdata;
    }

    public void setQrdata(String qrdata) {
        this.qrdata = qrdata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecorded_id() {
        return recorded_id;
    }

    public void setRecorded_id(String recorded_id) {
        this.recorded_id = recorded_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public QrDataBean(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
