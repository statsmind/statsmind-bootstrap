package com.statsmind.bootstrap.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserGetLoginRequisiteResponse {
    private String captchaImage;

    public UserGetLoginRequisiteResponse(UserGetLoginRequisiteRequest request) {

    }
}
