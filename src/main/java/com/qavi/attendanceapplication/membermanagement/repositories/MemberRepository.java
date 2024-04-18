package com.qavi.attendanceapplication.membermanagement.repositories;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query(value = "SELECT m.* FROM Member m JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId", nativeQuery = true)
    List<Member> findAllByOrganizationId(@Param("orgId") Long orgId);


    @Query(value = "SELECT COUNT(*) FROM Member m JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId AND TO_CHAR(m.registered_at, 'YYYY-MM') <= :yearMonth", nativeQuery = true)
    Long countMembersByOrganizationId(@Param("orgId") Long orgId, @Param("yearMonth") String formattedEndDate);

    @Query(value = "SELECT COUNT(*) FROM Member m JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId AND TO_CHAR(m.registered_at, 'YYYY-MM') <= :yearMonth AND m.active = true", nativeQuery = true)
    Long countMembersActiveByOrganizationId(@Param("orgId") Long orgId, @Param("yearMonth") String formattedEndDate);

    @Query(value = "SELECT COUNT(*) FROM Member m JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId AND TO_CHAR(m.registered_at, 'YYYY-MM') <= :yearMonth AND m.active = false", nativeQuery = true)
    Long countMembersNonActiveByOrganizationId(@Param("orgId") Long orgId, @Param("yearMonth") String formattedEndDate);

    @Transactional
    @Modifying
    @Query(value = "UPDATE member SET active = NOT active WHERE id = :id", nativeQuery = true)
    int deactivateMember(@Param("id") Long id);

    @Query(value = "SELECT CAST(EXTRACT(MONTH FROM m.registered_at) AS BIGINT) AS registration_month, COUNT(m.id) AS user_count FROM member m INNER JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId GROUP BY registration_month ORDER BY registration_month ASC", nativeQuery = true)
    List<Map<String, Long>> getMembersOfEachMonth(@Param("orgId") Long orgId);

    @Query(value = "SELECT COUNT(CASE WHEN m.inactive_date IS NOT NULL THEN 1 END) AS inActive, " +
            "COUNT(CASE WHEN m.active_date IS NOT NULL THEN 1 END) AS Active, " +
            "EXTRACT(YEAR FROM m.inactive_date) AS year, " +
            "EXTRACT(MONTH FROM m.inactive_date) AS month " +
            "FROM member m INNER JOIN organization_members o ON m.id = o.members_id " +
            "WHERE o.organization_id = :orgId AND EXTRACT(YEAR FROM m.inactive_date) = :year " +
            "GROUP BY year, month", nativeQuery = true)
    List<Map<String, Long>> getMembersStatusEachMonth(@Param("orgId") Long orgId, @Param("year") Long year);


    @Query(value = "SELECT COUNT(*) AS active, EXTRACT(YEAR FROM m.inactive_date) AS year, EXTRACT(MONTH FROM m.inactive_date) AS month " +
            "FROM member m INNER JOIN organization_members o ON m.id = o.members_id " +
            "WHERE o.organization_id = :orgId AND EXTRACT(YEAR FROM m.inactive_date) = :year " +
            "GROUP BY year, month", nativeQuery = true)
    List<Map<String, Long>> getDropOuts(@Param("orgId") Long orgId, @Param("year") Long year);


    @Query(value = "SELECT EXTRACT(YEAR FROM a.attendance_marked_at) AS year, EXTRACT(MONTH FROM a.attendance_marked_at) AS month, CEIL(EXTRACT(DAY FROM a.attendance_marked_at) / 7.0) AS week_of_month, COUNT(a.id) AS attendance_count FROM attendance a INNER JOIN organization_members om ON a.member_id = om.members_id WHERE om.organization_id = :orgId AND EXTRACT(YEAR FROM a.attendance_marked_at) = :year AND EXTRACT(MONTH FROM a.attendance_marked_at) = :month GROUP BY year, month, week_of_month ORDER BY year ASC, month ASC, week_of_month ASC", nativeQuery = true)
    List<Map<String, Long>> getAttendanceStatusOfMembers(@Param("orgId") Long orgId, @Param("year") Long year, @Param("month") Long month);

    @Query(value = "SELECT CAST(EXTRACT(DOW FROM m.registered_at) AS BIGINT) AS day_of_week, COUNT(*) AS registration_count FROM Member m JOIN organization_members o ON m.id = o.members_id WHERE EXTRACT(YEAR FROM m.registered_at) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM m.registered_at) = EXTRACT(MONTH FROM CURRENT_DATE) AND m.registered_at BETWEEN CURRENT_DATE - CAST(EXTRACT(DOW FROM CURRENT_DATE) AS INTEGER) * INTERVAL '1 day' AND CURRENT_DATE + (6 - CAST(EXTRACT(DOW FROM CURRENT_DATE) AS INTEGER)) * INTERVAL '1 day' AND o.organization_id = :orgId GROUP BY day_of_week ORDER BY day_of_week", nativeQuery = true)
    List<Map<Long, Long>> getWeeklyAnalysis(@Param("orgId") Long orgId);

    @Query(value = "WITH ActiveMembers AS (SELECT COUNT(*) AS active_count FROM Member m JOIN organization_members om ON m.id = om.members_id WHERE om.organization_id = :orgId AND TO_CHAR(m.registered_at, 'YYYY-MM') <= :yearMonth AND m.active = true), AttendanceCounts AS (SELECT COUNT(*) AS present_count FROM attendance WHERE organization_id = :orgId AND TO_CHAR(attendance_marked_at, 'YYYY-MM-DD') = :attendanceDate) SELECT present_count, active_count, active_count - present_count AS absent_count FROM AttendanceCounts CROSS JOIN ActiveMembers", nativeQuery = true)
    List<Map<String, Long>> getAttendanceAnalysis(
            @Param("orgId") Long orgId,
            @Param("yearMonth") String yearMonth,
            @Param("attendanceDate") String attendanceDate
    );


    @Query(value = "SELECT\n" +
            "    m.id AS member_id,\n" +
            "    m.name,\n" +
            "    a.id,\n" +
            "    a.attendance_marked_at AT TIME ZONE 'UTC' AT TIME ZONE 'Asia/Karachi' AS attendance_timestamp\n" +
            "FROM\n" +
            "    Member m\n" +
            "INNER JOIN\n" +
            "    attendance a ON m.id = a.member_id\n" +
            "    AND a.organization_id = :orgId\n" +
            "    AND TO_CHAR(a.attendance_marked_at, 'YYYY-MM-DD') = :attendanceDate\n", nativeQuery = true)
    List<Map<String, Long>> getAttendanceDetails(
            @Param("orgId") Long orgId,
            @Param("attendanceDate") String attendanceDate
    );

    Optional<Member> findByRFID(String rfid);

    @Query(value = "select active from member where id=:memberId", nativeQuery = true)
    Boolean getStatus(Long memberId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.inactiveDate = :dateTime WHERE m.id = :memberId")
    void setInActiveDate(@Param("memberId") Long memberId, @Param("dateTime") LocalDateTime dateTime);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.activeDate = :dateTime WHERE m.id = :memberId")
    void setActiveDate(@Param("memberId") Long memberId, @Param("dateTime") LocalDateTime dateTime);

}
