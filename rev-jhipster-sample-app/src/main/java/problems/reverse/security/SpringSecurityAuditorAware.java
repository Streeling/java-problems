package problems.reverse.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

import problems.reverse.config.Constants;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM));
  }
}