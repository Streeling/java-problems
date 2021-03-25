package problems.reverse.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import problems.reverse.domain.User;
import problems.reverse.repository.UserRepository;
import problems.reverse.security.RandomUtil;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
public class UserServiceIT {

	private static final String DEFAULT_LOGIN = "johndoe";

	private static final String DEFAULT_EMAIL = "johndoe@localhost";

	private static final String DEFAULT_FIRSTNAME = "john";

	private static final String DEFAULT_LASTNAME = "doe";

	private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

	private static final String DEFAULT_LANGKEY = "dummy";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuditingHandler auditingHandler;

	@Autowired
	private DateTimeProvider dateTimeProvider;

	private User user;

	@BeforeEach
	public void init() {
		user = new User();
		user.setLogin(DEFAULT_LOGIN);
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);
		user.setEmail(DEFAULT_EMAIL);
		user.setFirstName(DEFAULT_FIRSTNAME);
		user.setLastName(DEFAULT_LASTNAME);
		user.setImageUrl(DEFAULT_IMAGEURL);
		user.setLangKey(DEFAULT_LANGKEY);

		when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
		auditingHandler.setDateTimeProvider(dateTimeProvider);
	}

	@Test
	@Transactional
	void assertThatUserMustExistToResetPassword() {
		userRepository.saveAndFlush(user);
		Optional<User> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
		assertThat(maybeUser, not(isPresent()));

		maybeUser = userService.requestPasswordReset(user.getEmail());
		assertThat(maybeUser, isPresent());
		assertThat(maybeUser.orElse(null).getEmail(), equalTo(user.getEmail()));
		assertThat(maybeUser.orElse(null).getResetDate(), not(nullValue()));
		assertThat(maybeUser.orElse(null).getResetKey(), not(nullValue()));
	}

	@Test
	@Transactional
	void assertThatOnlyActivatedUserCanRequestPasswordReset() {
		user.setActivated(false);
		userRepository.saveAndFlush(user);

		Optional<User> maybeUser = userService.requestPasswordReset(user.getLogin());
		assertThat(maybeUser, not(isPresent()));
		userRepository.delete(user);
	}

	@Test
	@Transactional
	void assertThatResetKeyMustNotBeOlderThan24Hours() {
		Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
		String resetKey = RandomUtil.generateResetKey();
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey(resetKey);
		userRepository.saveAndFlush(user);

		Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
		assertThat(maybeUser, not(isPresent()));
		userRepository.delete(user);
	}

	@Test
	@Transactional
	void assertThatResetKeyMustBeValid() {
		Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey("1234");
		userRepository.saveAndFlush(user);

		Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
		assertThat(maybeUser, not(isPresent()));
		userRepository.delete(user);
	}

	@Test
	@Transactional
	void assertThatUserCanResetPassword() {
		String oldPassword = user.getPassword();
		Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
		String resetKey = RandomUtil.generateResetKey();
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey(resetKey);
		userRepository.saveAndFlush(user);

		Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
		assertThat(maybeUser, isPresent());
		assertThat(maybeUser.orElse(null).getResetDate(), nullValue());
		assertThat(maybeUser.orElse(null).getResetKey(), nullValue());
		assertThat(maybeUser.orElse(null).getPassword(), not(equalTo(oldPassword)));

		userRepository.delete(user);
	}

	@Test
	@Transactional
	void assertThatNotActivatedUsersWithNotNullActivationKeyCreatedBefore3DaysAreDeleted() {
		Instant now = Instant.now();
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
		user.setActivated(false);
		user.setActivationKey(RandomStringUtils.random(20));
		User dbUser = userRepository.saveAndFlush(user);
		dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
		userRepository.saveAndFlush(user);
		Instant threeDaysAgo = now.minus(3, ChronoUnit.DAYS);
		List<User> users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
		assertThat(users, not(empty()));
		userService.removeNotActivatedUsers();
		users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
		assertThat(users, empty());
	}

	@Test
	@Transactional
	void assertThatNotActivatedUsersWithNullActivationKeyCreatedBefore3DaysAreNotDeleted() {
		Instant now = Instant.now();
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
		user.setActivated(false);
		User dbUser = userRepository.saveAndFlush(user);
		dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
		userRepository.saveAndFlush(user);
		Instant threeDaysAgo = now.minus(3, ChronoUnit.DAYS);
		List<User> users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
		assertThat(users, empty());
		userService.removeNotActivatedUsers();
		Optional<User> maybeDbUser = userRepository.findById(dbUser.getId());
		assertThat(maybeDbUser, isPresentAndIs(dbUser));
	}
}