package com.java1234.model;

public class Bill {
    private int id;
    private String uuid;
    private String name;
    private String email;
    private String mobileNumber;
    private String date;
    private String total;
    private String createdBy;

    public Bill() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
