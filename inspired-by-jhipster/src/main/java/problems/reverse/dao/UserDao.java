package problems.reverse.dao;

import problems.reverse.domain.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDao {
	public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";

	public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";

	public Optional<User> findOneByActivationKey(String activationKey) {
		return Optional.empty();
	}

	public List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
		return Collections.emptyList();
	}

	public Optional<User> findOneByResetKey(String resetKey) {
		return Optional.empty();
	}

	public Optional<User> findOneByEmailIgnoreCase(String email) {
		return Optional.empty();
	}

	public Optional<User> findOneByLogin(String login){
		return Optional.empty();
	}

//	@EntityGraph(attributePaths = "authorities")
//	@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
	public Optional<User> findOneWithAuthoritiesByLogin(String login) {
		return Optional.empty();
	}

//	@EntityGraph(attributePaths = "authorities")
//	@Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
	public Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
		return Optional.empty();
	}

	public void saveAndFlush(User user) {

	}
}
