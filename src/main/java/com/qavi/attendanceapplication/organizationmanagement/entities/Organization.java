package com.qavi.attendanceapplication.organizationmanagement.entities;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    private String description;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Member> members;
    private Boolean active;
    private LocalDateTime createdAt;
    @ManyToMany
    private List<User> users;


}
