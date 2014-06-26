package com.drift.core;

import java.sql.Timestamp;

public class User implements Comparable<User> {

	private int uid = 0;
	private String username = null;
	private String nickname = null;
	private String sex = null;
	private String school = null;
	private String department = null;
	private String major = null;
	private String email = null;
	private String birthday = null;
	private String enrollYear = null;
	private Timestamp register_ts = null;
	
	public Timestamp getRegisterTs() {
		return register_ts;
	}

	public void setRegisterTs(Timestamp ts) {
		this.register_ts = ts;
	}
	
	@Override
	public String toString() {
		return uid + ":" + username;
	}

	public User() {
	}
	
	public User(int uid, String username, String nickname, String sex, String school, String department,
			String major, String email, String birthday, String enrollYear, Timestamp ts)
	{
		this.uid = uid;
		this.username = username;
		this.nickname = nickname;
		this.sex = sex;
		this.school = school;
		this.department = department;
		this.major = major;
		this.email = email;
		this.birthday = birthday;
		this.enrollYear = enrollYear;
		this.register_ts = ts;
	}
	
	public int compareTo(User another) {
		return username.compareTo(another.getUsername());
	}
	
	public boolean equals(Object another) {
		if(this == another)
			return true;
		if(another instanceof User) {
			User anotherUser = (User)another;
			return this.username.equals(anotherUser.getUsername());
		} else {
			return false;
		}
	}
	
	public void setUid(int id) {
		this.uid = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public void setSchool(String school) {
		this.school = school;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public void setEnrollYear(String enrollYear) {
		this.enrollYear = enrollYear;
	}
	
	public int getUid() {
		return uid;
	}
	
	public String getUsername() {
		return username;
	}

	public String getNickname() {
		return nickname==null ? "" : nickname;
	}
	
	public String getSex() {
		return sex;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getSchool() {
		return school==null ? "" : school;
	}
	
	public String getDepartment() {
		return department==null ? "" : department;
	}
	
	public String getMajor() {
		return major==null ? "" : major;
	}
	
	public String getEmail() {
		return email;
	}

	public String getEnrollYear() {
		return enrollYear==null ? "" : enrollYear;
	}
}

