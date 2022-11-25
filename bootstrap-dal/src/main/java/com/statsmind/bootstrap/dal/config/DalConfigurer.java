package com.statsmind.bootstrap.dal.config;

import com.statsmind.commons.security.SM3PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DalConfigurer {
    @Bean
    SM3PasswordEncoder passwordEncoder() {
        return new SM3PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return super.encode(shadow(rawPassword.toString()));
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return super.matches(shadow(rawPassword.toString()), encodedPassword);
            }

            private String shadow(String rawPassword) {
                return rawPassword + rawPassword.charAt(0) + rawPassword.charAt(rawPassword.length()-1);
            }
        };
    }
}
