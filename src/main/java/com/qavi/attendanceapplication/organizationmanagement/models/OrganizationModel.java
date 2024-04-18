package com.qavi.attendanceapplication.organizationmanagement.models;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrganizationModel {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
}
