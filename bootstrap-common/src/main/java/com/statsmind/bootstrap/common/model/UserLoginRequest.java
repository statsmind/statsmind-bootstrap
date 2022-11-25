package com.statsmind.bootstrap.common.model;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String mobile;
    private String password;
    private String captcha;
}
