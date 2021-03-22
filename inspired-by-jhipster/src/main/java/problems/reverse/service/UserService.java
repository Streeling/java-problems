package problems.reverse.service;

import problems.reverse.dao.UserDao;

public class UserService {

	private UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
}
