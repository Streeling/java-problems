package problems.reverse.service;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import problems.reverse.domain.User;
import problems.reverse.repository.UserRepository;

@Log4j2
@Transactional
public class UserService {

	private UserRepository userRepository;

	private final CacheManager cacheManager;

	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, CacheManager cacheManager, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.cacheManager = cacheManager;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> requestPasswordReset(String mail) {
		return Optional.empty(); // Should be implemented
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);
		return Optional.empty(); // Should be implemented
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		// Should be implemented
	}

	private void clearUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		if (user.getEmail() != null) {
			Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
		}
	}
}