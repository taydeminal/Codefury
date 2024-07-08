package com.demo.dao.userDao;

import java.util.List;

import com.demo.beans.User;
import com.demo.exceptions.userExceptions.EmailAlreadyExistsException;
import com.demo.exceptions.userExceptions.UserNotFoundException;

public interface UserDao {

	public int loginUser(String unmae, String pswd) throws UserNotFoundException;

	public int registerUser(User u) throws EmailAlreadyExistsException;

	public int importUser(String path) throws Exception;

	public List<User> displayAllUser();

	public User displayUserWithId(int id) throws UserNotFoundException;

}
