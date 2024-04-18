package com.qavi.attendanceapplication.attendancemanagement.utils;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
public class AttendanceMembersDTO {

    private BigInteger id;
    private BigInteger Attendance_id;
    private String name;
    private String phoneNumber;
    private Timestamp attendanceMarkedAt;
}
