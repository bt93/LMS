package com.techelevator.userProfile;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserProfileDao implements UserProfileDao {
	
	private JdbcTemplate myJdbcTemplate;
	
	@Autowired
	public JdbcUserProfileDao(DataSource myDataSource) {
		this.myJdbcTemplate = new JdbcTemplate(myDataSource);
	}
	
	public void createUserProfile(UserProfile newProfile, String email) {
		LocalDate start = newProfile.makeDate(newProfile.getStartDate());
		System.out.println("inside dao to create profile  "+email+"  "+start);
		String insertSql = "INSERT INTO user_profile "
						 + "(profile_id, firstname, lastname, role, start_date,end_date, profile_pic) "
						 + "VALUES ((SELECT id from users where email = '"+ email +"'),?,?,?,?,null,?) ";
		myJdbcTemplate.update(insertSql,newProfile.getFirstName(),newProfile.getLastName(),
				   newProfile.getRole(),start,newProfile.getProfilePic());
		
		
	}

}
