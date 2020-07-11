package com.techelevator.userProfile;

import java.time.LocalDate;
import java.util.Date;

	public class UserProfile {
	
	private int profileId;
	private String firstName;
	private String lastName;
	private String role;
	private Date startDate;
	private Date endDate;
	private String profilePic;
	
	public UserProfile() {
		
	}
	public UserProfile( String firstName, String lastName, String role, Date startDate, Date endDate, String profilePic) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.startDate =  startDate; 
		this.endDate =  endDate;
		this.profilePic = profilePic;
	}
	
	@Override
	public String toString() {
		return "UserProfile [firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + ", startDate="
				+ startDate + ", endDate=" + endDate + ", profilePic=" + profilePic + "]";
	}
	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
			return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	
}
