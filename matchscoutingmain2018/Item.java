package com.example.dan.matchscoutingmain2018;

public class Item {

	public enum Datatype {
		INTEGER, BOOLEAN, STRING, OPTIONS
	} // End Datatype

	private int id;
	private String name;
	private Datatype datatype;

	public Item(int id, String name, Datatype datatype) {
		this.id = id;
		this.name = name;
		this.datatype = datatype;
	} // End constructor

	public int getId() {
		return id;
	} // End getId

	public String getName() {
		return name;
	} // End getName

	public Datatype getDatatype() {
		return datatype;
	} // End getDatatype

	@Override
	public String toString() {
		return this.name;
	} // End toString

} // End Item