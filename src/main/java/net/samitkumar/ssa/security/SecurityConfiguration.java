package net.samitkumar.ssa.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

@Configuration
@Slf4j
public class SecurityConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService inmemoryUsers() {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
        var bob = new User("bob", passwordEncoder().encode("1234"), Collections.emptyList());
        var bil = User.builder()
                .username("bil")
                .password(passwordEncoder().encode("321"))
                //roles - are like ADMIN, USER, STUDENT, TEACHER
                .roles("USER")
                //authorities - are like read, write, delete, update
                .authorities("read")
                .build();
        users.createUser(bob);
        users.createUser(bil);

        return users;
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> print() {
        return event -> {
            log.info("success {}", event.getAuthentication());
        };
    }
}
