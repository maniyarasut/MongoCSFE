package com.mongo.demo.entity;

import org.bson.BsonBinary;
import org.springframework.data.annotation.Id;

public class EncryptedPerson {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private BsonBinary ssn;
	private BsonBinary phone;
	private BsonBinary bloodType;

	public EncryptedPerson(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public BsonBinary getSsn() {
		return ssn;
	}

	public void setSsn(BsonBinary ssn) {
		this.ssn = ssn;
	}

	public BsonBinary getPhone() {
		return phone;
	}

	public void setPhone(BsonBinary phone) {
		this.phone = phone;
	}

	public BsonBinary getBloodType() {
		return bloodType;
	}

	public void setBloodType(BsonBinary bloodType) {
		this.bloodType = bloodType;
	}

	@Override
	public String toString() {
		return "Person{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
	}
}
