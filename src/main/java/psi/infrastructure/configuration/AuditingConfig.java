package psi.infrastructure.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import psi.domain.user.entity.User;
import psi.domain.user.control.UserService;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.UserInfoProvider;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuditingConfig {

    private final UserService userService;

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

    class SpringSecurityAuditAwareImpl implements AuditorAware<User> {

        @Override
        public Optional<User> getCurrentAuditor() {
            return UserInfoProvider.getAuthenticatedUser()
                    .map(UserInfo::getId)
                    .map(userService::getExistingUser);
        }
    }

}
