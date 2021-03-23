package problems.reverse.service;

import org.springframework.cache.CacheManager;
import problems.reverse.dao.UserDao;
import problems.reverse.domain.User;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class UserService {

	private UserDao userDao;

	private final CacheManager cacheManager;

	public UserService(UserDao userDao, CacheManager cacheManager) {
		this.userDao = userDao;
		this.cacheManager = cacheManager;
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userDao
				.findOneByEmailIgnoreCase(mail)
				.filter(User::isActivated)
				.map(
						user -> {
//							user.setResetKey(RandomUtil.generateResetKey());
							user.setResetDate(Instant.now());
							this.clearUserCaches(user);
							return user;
						}
				);
	}

	private void clearUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserDao.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		if (user.getEmail() != null) {
			Objects.requireNonNull(cacheManager.getCache(UserDao.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
		}
	}
}
