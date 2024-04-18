package com.qavi.attendanceapplication.attendancemanagement.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequest {
    private List<AttendanceData> attendanceDataList;
    private AttendanceData attendanceData;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttendanceData {
        private Long id;
        private Long attendanceId;
        private String attendanceMarkedAt;
        private String RFID;
        private String flag;
    }
}
