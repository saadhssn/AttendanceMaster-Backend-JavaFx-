package com.qavi.attendanceapplication.usermanagement.entities.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdate {

    private String oldPassword;
    private String newPassword;

}
