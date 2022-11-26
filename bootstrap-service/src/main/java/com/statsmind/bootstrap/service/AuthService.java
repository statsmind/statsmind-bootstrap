package com.statsmind.bootstrap.service;

import com.statsmind.bootstrap.common.entity.UserEntity;
import com.statsmind.bootstrap.common.model.*;
import com.statsmind.bootstrap.dal.DalUserService;
import com.statsmind.commons.Status;
import com.statsmind.commons.security.CryptoProvider;
import com.statsmind.commons.web.RestException;
import com.statsmind.commons.web.RestResponseCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private static final String SESSION_AUTH_TOKEN_KEY = "AUTH_TOKEN";
    @NonNull
    private DalUserService dalUserService;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private CryptoProvider cryptoProvider;

    @SneakyThrows
    public UserLoginResponse login(UserLoginRequest request, HttpServletRequest servletRequest) {
        HttpSession httpSession = servletRequest.getSession(false);

        UserEntity user = dalUserService.getUserByMobile(request.getMobile());
        if (user == null) {
            /**
             * 测试环境
             */
            user = new UserEntity().setMobile(request.getMobile())
                .setPasswordHash(passwordEncoder.encode(request.getPassword()));
            dalUserService.saveUser(user);

            if (httpSession != null) {
                httpSession.removeAttribute(SESSION_AUTH_TOKEN_KEY);
            }

            this.setLoginFailed(httpSession, true);
            throw new RestException(RestResponseCode.INVALID_AUTH);
        }

        UserLoginRequisite loginRequisite = getUserLoginRequisite(httpSession);
        if (loginRequisite != null) {
            if (!StringUtils.equalsAnyIgnoreCase(request.getCaptcha(), loginRequisite.getCaptcha())) {
                this.setLoginFailed(httpSession, true);
                throw new RestException.ValidationErrorBuilder()
                    .append("captcha", "验证码错误")
                    .build();
            }
        }
        /**
         * 用户的密码是经过 SM3 杂凑算法加密的
         */
        String encodedPassword = request.getPassword();
        if (!passwordEncoder.matches(encodedPassword, user.getPasswordHash())) {
            if (httpSession != null) {
                httpSession.removeAttribute(SESSION_AUTH_TOKEN_KEY);
            }

            this.setLoginFailed(httpSession, true);
            throw new RestException(RestResponseCode.INVALID_AUTH);
        }

        UserLoginResponse response = new UserLoginResponse();
        BeanUtils.copyProperties(user, response);
        response.setToken(encodeToken(user));

        if (httpSession == null) {
            servletRequest.getSession(true);
        }

        if (httpSession != null) {
            httpSession.setAttribute(SESSION_AUTH_TOKEN_KEY, response.getToken());
        }

        this.setLoginFailed(httpSession, false);
        return response;
    }

    protected void setLoginFailed(HttpSession httpSession, boolean failed) {
        if (httpSession == null) {
            return;
        }

        if (failed) {
            httpSession.setAttribute("LOGIN_FAILED", "true");
        } else {
            httpSession.removeAttribute("LOGIN_FAILED");
        }
    }

    public UserGetLoginRequisiteResponse userGetLoginRequisite(UserGetLoginRequisiteRequest request, HttpSession httpSession) {
        UserLoginRequisite userLoginRequisite = getUserLoginRequisite(httpSession);
        return new UserGetLoginRequisiteResponse(request).setCaptchaImage(null);
    }

    protected UserLoginRequisite getUserLoginRequisite(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        String captcha = (String)httpSession.getAttribute("LOGIN_CAPTCHA");

        UserLoginRequisite userLoginRequisite = new UserLoginRequisite();
        userLoginRequisite.setCaptcha(captcha);

        return userLoginRequisite;
    }

    public boolean login(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession(false);

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token) && httpSession != null) {
            token = (String) httpSession.getAttribute(SESSION_AUTH_TOKEN_KEY);
        }

        if (StringUtils.isBlank(token)) {
            throw new RestException(RestResponseCode.NO_AUTH, "没有用户认证信息");
        }

        UserEntity user = decodeToken(token);
        if (user == null || user.getStatusId() != Status.AVAILABLE) {
            throw new RestException(RestResponseCode.NO_AUTH, "用户认证信息错误");
        }

        RequestContext requestContext = new RequestContext();
        requestContext.setUser(user);
        requestContext.setServletRequest(request);

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                requestContext.getUser().getId(), requestContext, new ArrayList()));

        return true;
    }

    @SneakyThrows
    protected String encodeToken(UserEntity user) {
        return cryptoProvider.encryptId(user.getId());
    }

    @SneakyThrows
    protected UserEntity decodeToken(String token) {
        Long userId = cryptoProvider.decryptId(token).longValue();
        return dalUserService.getUserById(userId);
    }
}
