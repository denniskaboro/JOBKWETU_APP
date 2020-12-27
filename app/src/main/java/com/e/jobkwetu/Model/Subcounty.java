package com.e.jobkwetu.Model;

public class Subcounty {

    public static final String TABLE_NAME = "subcounty";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SUBCOUNTY = "subcounty";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String subcounty;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_SUBCOUNTY + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Subcounty() {
    }

    public Subcounty(int id, String subcounty, String timestamp) {
        this.id = id;
        this.subcounty = subcounty;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
