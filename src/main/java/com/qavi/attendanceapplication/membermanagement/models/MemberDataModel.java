package com.qavi.attendanceapplication.membermanagement.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDataModel {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String Designation;
    private String country;
    private String RFID;
    private LocalDateTime dateOfBirth;
    private LocalDateTime registeredAt;
    private Boolean active;
}
