package com.qavi.attendanceapplication.membermanagement.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime registeredAt;
    @OneToOne(orphanRemoval = true)
    private MemberImages profileImage;
    private Boolean active;
    private LocalDateTime dateOfBirth;
    private String designation;
    private String RFID;
    private LocalDateTime inactiveDate;
    private LocalDateTime activeDate;

}
