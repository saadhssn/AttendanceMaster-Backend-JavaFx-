package com.qavi.attendanceapplication.usermanagement.models;

import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDataModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cnicNumber;
    private String countryCode;
    private String phone_number;
    private String country;
    private String city;
    private String Address;
    private Set<Role> roles;
    private String authType;
    private boolean enabled;
    private LocalDateTime lastLoginAt;
}

