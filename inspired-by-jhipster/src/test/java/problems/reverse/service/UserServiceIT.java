package problems.reverse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import problems.reverse.dao.UserDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
public class UserServiceIT {

	@Autowired
	private UserService service;

	@Autowired
	private UserDao userDao;

	@Test
	public void test() {
		userDao.delete(null);
	}
}
