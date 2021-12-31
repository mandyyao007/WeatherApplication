package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
