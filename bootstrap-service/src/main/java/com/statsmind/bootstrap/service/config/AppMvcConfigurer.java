package com.statsmind.bootstrap.service.config;

import com.statsmind.bootstrap.common.model.RequestContext;
import com.statsmind.bootstrap.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class AppMvcConfigurer implements WebMvcConfigurer {
    @NonNull
    private AuthService authService;

    private HandlerInterceptor apiInterceptor = new HandlerInterceptor() {
        /**
         * 处理API用户认证
         *
         * @param request current HTTP request
         * @param response current HTTP response
         * @param handler chosen handler to execute, for type and/or instance evaluation
         * @return
         * @throws Exception
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            return authService.login(request, response, handler);
        }

        /**
         * 这个在 /README.md 里面有说明，关于 Transaction
         *
         * @param request current HTTP request
         * @param response current HTTP response
         * @param handler the handler that started asynchronous
         * execution, for type and/or instance examination
         * @param ex any exception thrown on handler execution, if any; this does not
         * include exceptions that have been handled through an exception resolver
         * @throws Exception
         */
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            if (ex == null) {
                // nothing now
            }
        }
    };

    private HandlerInterceptor corsInterceptor = new HandlerInterceptor() {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));

            return true;
        }
    };

    /**
     * Session 由内存管理，不依赖其他服务，前期方便，后面再迁移到 Redis
     * @return
     */
    @Bean
    ReactiveSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor)
            .order(1);

        registry.addInterceptor(apiInterceptor)
            .excludePathPatterns("/api/auth/**")
            .addPathPatterns("/api/**")
            .order(2);
    }
}
