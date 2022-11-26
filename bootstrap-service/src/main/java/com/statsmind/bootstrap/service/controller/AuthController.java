package com.statsmind.bootstrap.service.controller;

import com.statsmind.bootstrap.common.model.UserLoginRequest;
import com.statsmind.bootstrap.common.model.UserLoginResponse;
import com.statsmind.bootstrap.service.AuthService;
import com.statsmind.commons.web.RestResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @NonNull
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<UserLoginResponse> userLogin(
        @RequestBody UserLoginRequest request, HttpServletRequest servletRequest) {
        return RestResponse.ok(authService.login(request, servletRequest));
    }
}
