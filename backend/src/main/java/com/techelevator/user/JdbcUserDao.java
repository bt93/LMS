package com.techelevator.user;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.authentication.PasswordHasher;

@Component
public class JdbcUserDao implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private PasswordHasher passwordHasher;

	@Autowired
	public JdbcUserDao(DataSource dataSource, PasswordHasher passwordHasher) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.passwordHasher = passwordHasher;
	}

	@Override
	public User saveUser(String email, String password, String permission) { 			// Only creates a login for the user and
		byte[] salt = passwordHasher.generateRandomSalt();										// adds defaults into some columns
		String hashedPassword = passwordHasher.computeHash(password, salt);
		String saltString = new String(Base64.encode(salt));
		String defaultFirst = "TE Firstname";
		String defaultLast = "TE Lastname";
		String defaultRole = "TE Instructor";
		LocalDate defaultDate = LocalDate.of(2016, Month.JANUARY, 1);
		String defaultCampus = "CLE";
		String defaultPic = "https://res.cloudinary.com/goshorn/image/upload/v1596286167/lms_test/TE_bur_z3zvc4.png";

		long newId = jdbcTemplate.queryForObject(
				"INSERT INTO users(email,firstname,lastname,profile_pic, password, salt, permission) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id",
				Long.class, email, defaultFirst, defaultLast, defaultPic, hashedPassword, saltString, permission);
		jdbcTemplate.update("INSERT INTO employee_profile(user_id,role,start_date,end_date,campus_short)VALUES ('"+newId+"','"+defaultRole+"','"+defaultDate+"',null,'"+defaultCampus+"')");

		User newUser = new User();
		newUser.setId(newId);
		newUser.setEmail(email);
		newUser.setPermission(permission);

		return newUser;
	}

//	@Override
//	public long createUser(User aUser, String email) {
//
//		long userId = jdbcTemplate.queryForObject(
//				"UPDATE users SET firstname = ?,lastname = ?,profile_pic = ? WHERE email = '" + email+ "' RETURNING id",
//				Long.class, aUser.getFirstName(), aUser.getLastName(), aUser.getProfilePic());
//
//		return userId;
//	}

	@Override
	public void changePassword(User user, String newPassword) {
		byte[] salt = passwordHasher.generateRandomSalt();
		String hashedPassword = passwordHasher.computeHash(newPassword, salt);
		String saltString = new String(Base64.encode(salt));

		jdbcTemplate.update("UPDATE users SET password=?, salt=? WHERE id=?", hashedPassword, saltString, user.getId());
	}

	@Override
	public User getValidUserWithPassword(String email, String password) {
		String sqlSearchForUser = "SELECT * FROM users WHERE UPPER(email) = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUser, email.toUpperCase());
		if (results.next()) {
			String storedSalt = results.getString("salt");
			String storedPassword = results.getString("password");
			String hashedPassword = passwordHasher.computeHash(password, Base64.decode(storedSalt));
			if (storedPassword.equals(hashedPassword)) {
				return mapResultToUser(results);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		String sqlSelectAllUsers = "SELECT id, email, permission FROM users";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllUsers);

		while (results.next()) {
			User user = mapResultToUser(results);
			users.add(user);
		}

		return users;
	}

	@Override
	public User getUserByEmail(String email) {
		String sqlSelectUserByEmail = "SELECT id, email, permission FROM users WHERE email = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserByEmail, email);

		if (results.next()) {
			return mapResultToUser(results);
		} else {
			return null;
		}
	}

	@Override
	public void deleteUser(int id) {
		String sql = "DELETE FROM employee_profile WHERE user_id = ?; " + "DELETE FROM users WHERE id = ?";
		jdbcTemplate.update(sql, id, id);
	}

	@Override
	public void changePermission(String email, String permission) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("UPDATE users SET permission = ? WHERE email = ?", permission, email);
	}

	private User mapResultToUser(SqlRowSet results) {
		User user = new User();
		user.setId(results.getLong("id"));
		user.setEmail(results.getString("email"));
		user.setPermission(results.getString("permission"));
//		user.setFirstName(results.getString("firstname"));
//		user.setLastName(results.getString("lastname"));
//		user.setProfilePic(results.getString("profile_pic"));
		return user;
	}

}
