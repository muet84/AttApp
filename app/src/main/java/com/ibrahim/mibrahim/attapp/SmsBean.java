package com.ibrahim.mibrahim.attapp;

/**
 * Created by M.Ibrahim on 4/9/2017.
 */

public class SmsBean {

    String id;
    String server_id;
    String school_id;
    String parent_id;
    String parent_type;
    String type;
    String medium;
    String to_number;

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }


    public SmsBean(){}

    public SmsBean(String id, String server_id, String school_id, String parent_id, String parent_type, String type,
                   String medium, String to_number, String from_text, String title, String contents, String status,
                   String created_by, String updated_by, String created_at, String updated_at) {
        this.id = id;
        this.server_id = server_id;
        this.school_id = school_id;
        this.parent_id = parent_id;
        this.parent_type = parent_type;
        this.type = type;
        this.medium = medium;
        this.to_number = to_number;
        this.from_text = from_text;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public String getFrom_text() {

        return from_text;
    }

    public void setFrom_text(String from_text) {
        this.from_text = from_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getTo_number() {
        return to_number;
    }

    public void setTo_number(String to_number) {
        this.to_number = to_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    String from_text;
    String title;
    String contents;
    String status;
    String created_by;
    String updated_by;
    String created_at;
    String updated_at;

}
