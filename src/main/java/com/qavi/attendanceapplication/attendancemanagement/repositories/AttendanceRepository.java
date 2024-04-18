package com.qavi.attendanceapplication.attendancemanagement.repositories;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.utils.AttendanceMembersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query(value = "SELECT member.id, member.name, member.phone_number, attendance.attendance_marked_at, attendance.id as attendance_id  \n" +
            "FROM member\n" +
            "LEFT JOIN attendance ON member.id = attendance.member_id AND DATE(attendance.attendance_marked_at) = CURRENT_DATE\n" +
            "JOIN organization_members ON member.id = organization_members.members_id\n" +
            "WHERE organization_members.organization_id = :orgId\n" +
            "ORDER BY member.id;\n", nativeQuery = true)
    List<Tuple> findAllByOrganizationId(@Param("orgId") Long orgId);


    @Query(value = "SELECT COUNT(*) FROM attendance WHERE organization_id = :orgId AND TO_CHAR(attendance_marked_at, 'YYYY-MM') <= :yearMonth", nativeQuery = true)
    Long getTotalAttendanceForMonth(@Param("orgId") Long orgId, @Param("yearMonth") String formattedEndDate);


    List<Attendance> getAllByOrganizationId(Long orgId);
}
