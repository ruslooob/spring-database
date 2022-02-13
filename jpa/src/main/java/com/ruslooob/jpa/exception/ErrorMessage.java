package com.ruslooob.jpa.exception;

import java.time.LocalDateTime;

public class ErrorMessage {
    private int statusCode;
    private LocalDateTime dateTime;
    private String message;
    private String description;

    public ErrorMessage() {
    }

    public ErrorMessage(int statusCode, LocalDateTime dateTime, String message, String description) {
        this.statusCode = statusCode;
        this.dateTime = dateTime;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
