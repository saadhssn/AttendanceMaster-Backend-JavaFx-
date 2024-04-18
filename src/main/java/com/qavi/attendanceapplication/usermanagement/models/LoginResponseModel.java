package com.qavi.attendanceapplication.usermanagement.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseModel {
    String accessToken;
    String refreshToken;
    String userId;
    String email;
    List<String> roles;
    String firstName;
    String lastName;
    Boolean isEnabled;
    List<PermissionBitsModel> permissions;
}
