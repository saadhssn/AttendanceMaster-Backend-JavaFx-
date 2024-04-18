package com.qavi.attendanceapplication.attendancemanagement.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceModel {
    private Long id;
    private LocalDateTime attendanceMarkedAt;
    private Long Organization_id;
    private Long Members_id;
    private String memberName;
    private  String phone_number;
}
