package com.qavi.attendanceapplication.feesmanagement.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FeesModel {
    private Long id;
    private double amount;
    private LocalDateTime createdAt;
    private Long memberId;
    private String forMonthOf;
    private String forYearOf;
    private Long organizationId;
}

