package com.example.hkmessenger;

public class msgModle
{
    String message;
    String senderId;
    long timeStamp;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    String imageUri;
    public msgModle() {
    }

    public msgModle(String message, String senderId, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
