package dev.nullzwo.user.app;

import dev.nullzwo.user.domain.api.ChangeService;
import dev.nullzwo.user.domain.api.UserService;
import dev.nullzwo.user.domain.service.ChangeLogger;
import dev.nullzwo.user.domain.spi.ChangePort;
import dev.nullzwo.user.domain.spi.UserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AppConfiguration {

    @Bean
    public ChangeLogger changeLogger(ChangePort changePort) {
        return new ChangeLogger(Clock.systemUTC(), changePort);
    }

    @Bean
    public UserService userService(UserPort userPort) {
        return new UserService(userPort, changeLogger(null));
    }

    @Bean
    public ChangeService changeService(UserPort userPort, ChangePort changePort) {
        return new ChangeService(changePort, userPort);
    }
}
