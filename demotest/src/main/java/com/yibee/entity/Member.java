package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Calendar;
import java.util.Date;


/**
 * The persistent class for the members database table.
 * 
 */
@Entity
@Table(name="members")
@NamedQuery(name="Member.findAll", query="SELECT m FROM Member m")
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false)
	private int actived;

	@Temporal(TemporalType.DATE)
	private Calendar birthday;

	@Column(nullable=false)
	private int disabled;

	@Column(nullable=false, length=100)
	private String email;

	@Column(name="first_name", length=32)
	private String firstName;

	private int gender;

	@Column(name="last_name", length=32)
	private String lastName;
	
	@JsonIgnore
	@Column(name="pass_word", nullable=false, length=32)
	private String passWord;

	@Column(name="phone_number", length=32)
	private String phoneNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="register_date")
	private Date registerDate;

	@Column(name="school_code", length=32)
	private String schoolCode;

	@Column(name="sell_disabled", nullable=false)
	private int sellDisabled;

	@Column(name="user_name", nullable=false, length=32)
	private String userName;

	public Member() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getActived() {
		return this.actived;
	}

	public void setActived(int actived) {
		this.actived = actived;
	}

	public Calendar getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public int getDisabled() {
		return this.disabled;
	}

	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getGender() {
		return this.gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getSchoolCode() {
		return this.schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public int getSellDisabled() {
		return this.sellDisabled;
	}

	public void setSellDisabled(int sellDisabled) {
		this.sellDisabled = sellDisabled;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}