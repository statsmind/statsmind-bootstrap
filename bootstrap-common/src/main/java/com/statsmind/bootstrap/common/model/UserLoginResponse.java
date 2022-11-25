package com.statsmind.bootstrap.common.model;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String id;
    private String mobile;
    private String displayName;
    private String token;
}
