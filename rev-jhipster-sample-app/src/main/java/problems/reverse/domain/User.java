package problems.reverse.domain;

import java.time.Instant;

import lombok.Data;

/**
 * A user.
 */
@Data
public class User extends AbstractAuditingEntity {

	private Long id;

	private String login;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private boolean activated = false;

	private String langKey;

	private String imageUrl;

	private String activationKey;

	private String resetKey;

	private Instant resetDate = null;
}