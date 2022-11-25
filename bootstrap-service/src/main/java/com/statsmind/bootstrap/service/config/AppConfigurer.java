package com.statsmind.bootstrap.service.config;

import com.statsmind.bootstrap.common.model.RequestContext;
import com.statsmind.commons.security.CryptoAlgorithm;
import com.statsmind.commons.security.CryptoProvider;
import com.statsmind.commons.security.SM4CryptoAlgorithm;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AppConfigurer {
    @SneakyThrows
    @Bean
    CryptoProvider cryptoProvider() {
        return new CryptoProvider("834A87BC123FE", new SM4CryptoAlgorithm());
    }

    /**
     * 填充 created_by, updated_by
     *
     * @return
     */
    @Bean
    AuditorAware<Long> entityAuditorAware() {
        return () -> Optional.ofNullable(RequestContext.currentUserId(false));
    }
}
