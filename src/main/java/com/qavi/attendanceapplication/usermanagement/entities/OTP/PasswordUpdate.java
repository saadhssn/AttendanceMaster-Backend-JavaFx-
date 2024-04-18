package com.qavi.attendanceapplication.usermanagement.entities.OTP;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordUpdate {

    private String newPassword;
    private String otp;
}
