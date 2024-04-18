package com.qavi.attendanceapplication.attendancemanagement.entities;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private LocalDateTime attendanceMarkedAt;
    @ManyToOne
    private Organization organization;
}
