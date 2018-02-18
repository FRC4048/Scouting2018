package com.example.dan.matchscoutingmain2018;

public final class Record {

    private String value;
    // database item id
    private int itemID;

    public Record(String value, int itemID) {
        this.value = value;
        this.itemID = itemID;
    } // End constructor

    public Record(String value) {
        String[] values = value.split(Form.ID_DELIMITER);
        this.itemID = Integer.valueOf(values[0]);
        this.value = values[1];
    } // End constructor

    public String getValue() {
        return value;
    } // End getValue

    public int getItemID() {
        return itemID;
    } // End getItemID

    public void setValue(String value) {
        this.value = value;
    } // End setValue

    public void setItemID(int itemID) {
        this.itemID = itemID;
    } // End setItemID

    @Override
    public String toString() {
        return itemID + Form.ID_DELIMITER + value;
    } // End toString

} // End Record