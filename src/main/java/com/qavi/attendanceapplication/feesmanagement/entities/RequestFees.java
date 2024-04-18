package com.qavi.attendanceapplication.feesmanagement.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestFees {
    private String memberId;
    private double amount;
    private LocalDateTime createdAt;
    private String forMonthOf;
    private String forYearOf;
}
