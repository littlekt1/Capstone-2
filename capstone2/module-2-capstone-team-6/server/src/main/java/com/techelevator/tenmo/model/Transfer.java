package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {
    @JsonProperty("transfer_id")
    private int transferId;
    @JsonProperty("username_from")
    private String usernameFrom;
    @JsonProperty("username_to")
    private String usernameTo;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("status")
    private String status;
    @JsonProperty("request")
    private boolean request;

    public Transfer(int transferId, String usernameFrom, String usernameTo, double amount, String status, boolean request) {
        this.transferId = transferId;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.amount = amount;
        this.status = status;
        this.request = request;
    }

    public Transfer() {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

}
