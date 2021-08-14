package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trainer implements Serializable {

    @SerializedName("UserName")
    private String username;

    @SerializedName("Name")
    private String name;

    @SerializedName("Email")
    private String email;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Address")
    private String address;

    @SerializedName("IsActive")
    private boolean isActive;

    @SerializedName("Password")
    private String password;

    @SerializedName("IdSkill")
    private int skill;

    @SerializedName("ActivationCode")
    private String activationCode;

    @SerializedName("ResetPasswordCode")
    private String resetPasswordCode;

    @SerializedName("IsReceiveNotification")
    private boolean isReceiveNotification;

    public Trainer() {
    }

    public Trainer(String username, String name, String email, String phone, String address, boolean isActive, String password, int skill, String activationCode, String resetPasswordCode, boolean isReceiveNotification) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
        this.password = password;
        this.skill = skill;
        this.activationCode = activationCode;
        this.resetPasswordCode = resetPasswordCode;
        this.isReceiveNotification = isReceiveNotification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public boolean isReceiveNotification() {
        return isReceiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        isReceiveNotification = receiveNotification;
    }

    @Override
    public String toString() {
        return username;
    }
}