package com.demo.dao.userDao;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.demo.beans.User;
import com.demo.dbutil.DBUtil;
import com.demo.exceptions.userExceptions.EmailAlreadyExistsException;
import com.demo.exceptions.userExceptions.UserNotFoundException;

public class UserDaoImpl implements UserDao {
   // apart from the table given in pdf loginDate and loginTime has been added and telephone is made varchar(10)
	@Override
	public int registerUser(User per) throws EmailAlreadyExistsException {
		try {
			Connection conn = DBUtil.getConnConnection();

			PreparedStatement pst = conn.prepareStatement(
					"insert into user(userName, userRole, userTelephone, userEmail, userPassword,LoginDate,LoginTime) values(?,?,?,?,?,?,?)");
			pst.setString(1, per.getUserName());
			pst.setString(2, per.getUserRole());
			pst.setString(3, per.getUserTelephone());
			pst.setString(4, per.getUserEmail());
			pst.setString(5, per.getUserPassword());
			LocalDateTime localDateTime = LocalDateTime.now();
			System.out.println(localDateTime.toString());
			Date date = java.sql.Date.valueOf(localDateTime.toLocalDate());
			Time time = java.sql.Time.valueOf(localDateTime.toLocalTime());
			pst.setDate(6, date);
			pst.setTime(7, time);
			int count = pst.executeUpdate();

			if (count == 1) {
				return 1;
			}
		} catch (SQLException e) {
			throw new EmailAlreadyExistsException("Cannot add! User with email " + per.getUserEmail() + " already exists !");
		//	e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int loginUser(String email, String pswd) throws UserNotFoundException {
		User user = null;
		try {
			Connection conn = DBUtil.getConnConnection();

			PreparedStatement pst = conn.prepareStatement("select * from user where userEmail=? and userPassword=?");

			pst.setString(1, email);
			pst.setString(2, pswd);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				user = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userRole"),
						rs.getString("userTelephone"), rs.getString("userEmail"), rs.getString("userPassword"));
				if (user.getUserEmail().equals(email) && user.getUserPassword().equals(pswd)) {
					updateUserLoginDateTime(user);
				}
			}
			return 1;
		} catch (SQLException e) {
			throw new UserNotFoundException("No such user found !");
		}
		
	}

	@Override
	public int importUser(String path) throws Exception {
		int status = 0;
		try {
			List<User> users = getJSONtoList(path);
			try {
				for (User u : users) {
					registerUser(u);
				}
			} catch (EmailAlreadyExistsException e) {
				throw new EmailAlreadyExistsException(
						"Cannot import ! Some/All users with same email already exists !");
			}
			status = 1;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return status;
	}

	private List<User> getJSONtoList(String path) throws Exception {
		List<User> userList = new ArrayList<User>();
		try {
			JSONParser jsonParser = new JSONParser();
			JSONArray arr = (JSONArray) jsonParser.parse(new FileReader(path));

			for (Object o : arr) {

				JSONObject user = (JSONObject) o;
				String name = (String) user.get("userName");
				String role = (String) user.get("userRole");
				String phone = (String) user.get("userTelephone");
				String email = (String) user.get("userEmail");
				String pswd = (String) user.get("userPassword");

				userList.add(new User(name, role, phone, email, pswd));
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return userList;
	}

	@Override
	public List<User> displayAllUser() {
		List<User> list = new ArrayList<>();
		User per = null;
		try {
			Connection conn = DBUtil.getConnConnection();
			PreparedStatement pst = conn.prepareStatement("select * from user");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				per = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userRole"),
						rs.getString("userTelephone"), rs.getString("userEmail"), rs.getString("userPassword"));
				list.add(per);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public User displayUserWithId(int id) throws UserNotFoundException {

		User per = null;
		try {
			Connection conn = DBUtil.getConnConnection();
			PreparedStatement pst = conn.prepareStatement("select * from user where userId = ?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				per = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userRole"),
						rs.getString("userTelephone"), rs.getString("userEmail"), rs.getString("userPassword"));
			} else
				throw new UserNotFoundException("No Person found !");
		} catch (SQLException e) {
			throw new UserNotFoundException("No Person found !");
		}
		return per;
	}

	private void updateUserLoginDateTime(User per) throws UserNotFoundException {

		try {
			Connection conn = DBUtil.getConnConnection();

			String getCount = "select * from user where userId = " + per.getUserId();
			PreparedStatement pst_temp = conn.prepareStatement(getCount);
			ResultSet rs = pst_temp.executeQuery();

			if (!rs.next())
				throw new UserNotFoundException("User not present !");
			else {
				LocalDateTime localDateTime = LocalDateTime.now();
				System.out.println(localDateTime.toString());
				Date date = java.sql.Date.valueOf(localDateTime.toLocalDate());
				Time time = java.sql.Time.valueOf(localDateTime.toLocalTime());
				PreparedStatement pst = conn
						.prepareStatement("update user set logindate = ?, logintime = ? where userId = ?");
				pst.setDate(1, date);
				pst.setObject(2, time);
				pst.setInt(3, per.getUserId());

				int count = pst.executeUpdate();

				if (count == 1) {
					return;
				}
			}
		} catch (SQLException e) {
			throw new UserNotFoundException("No such User found !");
		}
	}
}
