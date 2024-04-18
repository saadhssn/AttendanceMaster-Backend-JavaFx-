package com.qavi.attendanceapplication.feesmanagement.entities;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private double amount;
    private LocalDateTime createdAt;
    private String forMonthOf;
    private String forYearOf;
    @ManyToOne
    private Organization organization;
}
