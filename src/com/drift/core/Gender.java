package com.drift.core;

public enum Gender {
	
	MALE(0, "male"), FEMALE(1, "female");
	
	private int index;
	private String gender;

	private Gender(int index, String gender) {
		this.index = index;
		this.gender = gender;
	}
	
	public static Gender makeGender(int index) {
		if(index == 0) {
			return MALE;
		} else if(index == 1) {
			return FEMALE;
		} else {
			System.err.println("Invalid index: " + index);
			return MALE;
		}
	}
		
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return this.gender;
	}
}
