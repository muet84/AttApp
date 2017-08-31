package com.ibrahim.mibrahim.attapp;

/**
 * Created by M.Ibrahim on 3/29/2017.
 */

public class UserDeviceBean {


    String application;
    String imeiNumber;
    String permissions;
    String manufacturer;
    String userName,password;
    String server_ID;
    String device_ID;
    String status,approved,customer_id,user_id;

    public String getDevice_ID() {
        return device_ID;
    }

    public void setDevice_ID(String device_ID) {
        this.device_ID = device_ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserDeviceBean(String application, String imeiNumber, String permissions,
                          String manufacturer, String userName, String password,
                          String server_ID, String device_ID, String brand, String year_build,
                          String OS, String OS_version, String comment, String note,String status ,
                          String approved , String customer_id,String user_id) {

                this.application = application;
                this.imeiNumber = imeiNumber;
                this.permissions = permissions;
                this.manufacturer = manufacturer;
                this.userName = userName;
                this.password = password;
                this.server_ID = server_ID;
                this.device_ID = device_ID;
                this.brand = brand;
                this.year_build = year_build;
                this.OS = OS;
                this.OS_version = OS_version;
                this.comment = comment;
                this.note = note;
                this.status  = status;
                this.approved = approved;
                this.customer_id = customer_id;
                this.user_id = user_id;
    }

    public String getServer_ID() {

        return server_ID;
    }

    public void setServer_ID(String server_ID) {
        this.server_ID = server_ID;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear_build() {
        return year_build;
    }

    public void setYear_build(String year_build) {
        this.year_build = year_build;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getOS_version() {
        return OS_version;
    }

    public void setOS_version(String OS_version) {
        this.OS_version = OS_version;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String brand;
    String year_build;
    String OS;
    String OS_version;
    String comment;
    String note;

}
