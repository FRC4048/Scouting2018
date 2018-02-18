package com.example.dan.matchscoutingmain2018;

public class Option {

	private String name;
	private int value;
	private int itemID;

	public Option(String name, int value, int itemID) {
		this.name = name;
		this.value = value;
		this.itemID = itemID;
	} // End constructor

	public String getName() {
		return name;
	} // End getName

	public int getValue() {
		return value;
	} // End getValue

	public int getItemID() {
		return itemID;
	} // End getItemID

	@Override
	public String toString() {
		return String.valueOf(this.value);
	} // End toString

} // End Option