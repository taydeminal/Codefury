package com.demo.beans;

public class User {
	private int userId;
	private String userName;
	private String userRole;
	private String userTelephone;
	private String userEmail;
	private String userPassword;

	public User(String userName, String userRole, String userTelephone, String userEmail, String userPassword) {
		this.userName = userName;
		this.userRole = userRole;
		this.userTelephone = userTelephone;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public User(int id, String userName, String userRole, String userTelephone, String userEmail, String userPassword) {
		this.userId = id;
		this.userName = userName;
		this.userRole = userRole;
		this.userTelephone = userTelephone;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserTelephone() {
		return userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userRole=" + userRole + ", userTelephone="
				+ userTelephone + ", userEmail=" + userEmail + ", userPassword=" + userPassword + "]";
	}

}
