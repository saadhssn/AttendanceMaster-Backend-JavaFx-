package com.qavi.attendanceapplication.attendancemanagement.utils;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.models.AttendanceModel;

public class AttendanceConverter {

    public static AttendanceModel covertAtoAM(Attendance attendance) {
        AttendanceModel attendanceModel = new AttendanceModel();
        attendanceModel.setMemberName(attendance.getMember().getName());
        attendanceModel.setPhone_number(attendance.getMember().getPhoneNumber());
        attendanceModel.setId(attendance.getId());
        attendanceModel.setAttendanceMarkedAt(attendance.getAttendanceMarkedAt());
        attendanceModel.setOrganization_id(attendance.getOrganization().getId());
        attendanceModel.setMembers_id(attendance.getMember().getId());
        return attendanceModel;
    }
}

