package problems.reverse.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

/**
 * A user.
 */
@Data
@Entity
@Table(name = "jhi_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(length = 254, unique = true)
	private String email;

	@Column(nullable = false)
	private boolean activated = false;

	@Column(name = "lang_key", length = 10)
	private String langKey;

	@Column(name = "image_url", length = 256)
	private String imageUrl;

	@Column(name = "activation_key", length = 20)
	private String activationKey;

	@Column(name = "reset_key", length = 20)
	private String resetKey;

	@Column(name = "reset_date")
	private Instant resetDate = null;

}
