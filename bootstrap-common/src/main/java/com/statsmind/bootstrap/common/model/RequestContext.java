package com.statsmind.bootstrap.common.model;

import com.statsmind.bootstrap.common.entity.UserEntity;
import com.statsmind.commons.web.RestException;
import com.statsmind.commons.web.RestResponseCode;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@Data
public class RequestContext {
    private UserEntity user;
    private HttpServletRequest servletRequest;

    /**
     * 当前请求的用户，Authorization 认证
     *
     * @return
     */
    public static Long currentUserId() {
        return currentUserId(true);
    }

    /**
     * 当前请求的用户，Authorization 认证
     *
     * @param throwException
     * @return
     */
    public static Long currentUserId(boolean throwException) {
        RequestContext requestContext = getRequestContext();

        if (requestContext.getUser() != null) {
            return requestContext.getUser().getId();
        }

        if (throwException) {
            throw new RestException(RestResponseCode.NO_AUTH);
        } else {
            return null;
        }
    }

    protected static RequestContext getRequestContext() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            RequestContext requestContext = new RequestContext();

            Authentication authentication = new UsernamePasswordAuthenticationToken(null, requestContext, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return requestContext;
        }

        return (RequestContext) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
