package problems.reverse.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import problems.reverse.dao.UserDao;
import problems.reverse.domain.User;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
	private UserDao userDao;

	@Autowired
	private UserService userService;

//	@Autowired
//	private AuditingHandler auditingHandler;
//
//	@MockBean
//	private DateTimeProvider dateTimeProvider;

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
//
//		when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
//		auditingHandler.setDateTimeProvider(dateTimeProvider);
	}

	@Test
	@Transactional
	void assertThatUserMustExistToResetPassword() {
		userDao.saveAndFlush(user);
		Optional<User> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
		assertThat(maybeUser, not(isPresent()));

		maybeUser = userService.requestPasswordReset(user.getEmail());
		assertThat(maybeUser, isPresent());
		assertThat(maybeUser.orElse(null).getEmail(), equalTo(user.getEmail()));
		assertThat(maybeUser.orElse(null).getResetDate(), not(nullValue()));
		assertThat(maybeUser.orElse(null).getResetKey(), not(nullValue()));
	}}
