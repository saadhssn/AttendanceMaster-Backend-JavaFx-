package com.qavi.attendanceapplication.attendancemanagement.services;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.entities.AttendanceRequest;
import com.qavi.attendanceapplication.attendancemanagement.repositories.AttendanceRepository;
import com.qavi.attendanceapplication.attendancemanagement.utils.AttendanceMembersDTO;
import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberRepository;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qavi.attendanceapplication.AttendanceApplication.zoneId;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrganizationRepository organizationRepository;


    // list
    public void markAttendance(List<AttendanceRequest.AttendanceData> attendanceDataList, Long OrgId) {
        Organization organization = organizationRepository.findById(OrgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + OrgId));


        for (AttendanceRequest.AttendanceData attendanceData : attendanceDataList) {

            Member member = memberRepository.findById(attendanceData.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + attendanceData.getId()));

            Attendance attendance = new Attendance();
            attendance.setMember(member);
            attendance.setOrganization(organization);
            attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()));
            attendanceRepository.save(attendance);
        }
    }


    public void markAttendanceDesktop(List<AttendanceRequest.AttendanceData> attendanceDataList, Long OrgId) {
        Organization organization = organizationRepository.findById(OrgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + OrgId));

        for (AttendanceRequest.AttendanceData attendanceData : attendanceDataList) {

            Member member = memberRepository.findById(attendanceData.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + attendanceData.getId()));

            if (attendanceData.getFlag().equalsIgnoreCase("add")) {
                Attendance attendance = new Attendance();
                attendance.setMember(member);
                attendance.setOrganization(organization);
                attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()).atZone(zoneId).toLocalDateTime());
                attendanceRepository.save(attendance);
            } else if (attendanceData.getFlag().equalsIgnoreCase("delete")) {
                if (attendanceRepository.findById(attendanceData.getAttendanceId()).isPresent()) {
                    attendanceRepository.deleteById(attendanceData.getAttendanceId());
                }
            } else if (attendanceData.getFlag().equalsIgnoreCase("update")) {
                if (attendanceRepository.findById(attendanceData.getAttendanceId()).isPresent()) {
                    Attendance attendance = attendanceRepository.findById(attendanceData.getAttendanceId()).get();
                    attendance.setMember(member);
                    attendance.setOrganization(organization);
                    attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()).atZone(zoneId).toLocalDateTime());
                    attendanceRepository.save(attendance);
                } else {
                    Attendance attendance = new Attendance();
                    attendance.setMember(member);
                    attendance.setOrganization(organization);
                    attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()));
                    attendanceRepository.save(attendance);
                }
            }
        }
    }

    /// individual
    public void markIndividualAttendance(AttendanceRequest.AttendanceData attendanceData, Long OrgId) {
        Member member = memberRepository.findById(attendanceData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + attendanceData.getId()));

        Organization organization = organizationRepository.findById(OrgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + OrgId));

        List<Long> byOrganizationId = organizationRepository.findOrgIdsByMemberId(member.getId());

        if (byOrganizationId.contains(OrgId)) {
            Attendance attendance = new Attendance();
            attendance.setMember(member);
            attendance.setOrganization(organization);
            attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()));
            attendanceRepository.save(attendance);
        } else {
            throw new EntityNotFoundException("Member is not registered with this Organization: " + OrgId);
        }
    }


    public void markIndividualAttendanceByRFID(AttendanceRequest.AttendanceData attendanceData, Long OrgId) {
        Member member = memberRepository.findByRFID(attendanceData.getRFID())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with RFID: " + attendanceData.getRFID()));


        Organization organization = organizationRepository.findById(OrgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + OrgId));

        List<Long> byOrganizationId = organizationRepository.findOrgIdsByMemberId(member.getId());

        if (byOrganizationId.contains(OrgId)) {
            Attendance attendance = new Attendance();
            attendance.setMember(member);
            attendance.setOrganization(organization);
            attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()));
            attendanceRepository.save(attendance);
        } else {
            throw new EntityNotFoundException("Member is not registered with this Organization: " + OrgId);
        }
    }


    public List<AttendanceMembersDTO> getMembersWithAttendance(Long orgId) {
        List<Tuple> tuples = attendanceRepository.findAllByOrganizationId(orgId);
        return convertTuplesToDTOs(tuples);
    }


    private List<AttendanceMembersDTO> convertTuplesToDTOs(List<Tuple> tuples) {
        List<AttendanceMembersDTO> result = new ArrayList<>();
        for (Tuple tuple : tuples) {
            AttendanceMembersDTO dto = new AttendanceMembersDTO();
            dto.setId(tuple.get("id", BigInteger.class));
            dto.setName(tuple.get("name", String.class));
            dto.setPhoneNumber(tuple.get("phone_number", String.class));
            dto.setAttendance_id(tuple.get("Attendance_id", BigInteger.class));
            dto.setAttendanceMarkedAt(tuple.get("attendance_marked_at", Timestamp.class));


            result.add(dto);
        }
        return result;
    }


    public Attendance getAttendance(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    public void removeAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public void update(AttendanceRequest.AttendanceData attendanceData, Long id) {
        Attendance attendance = attendanceRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Attendance not found with ID: " + id));

//        Member member = memberRepository.findById(attendanceData.getId()).orElse(null);
//        attendance.setMember(member);
        attendance.setAttendanceMarkedAt(LocalDateTime.parse(attendanceData.getAttendanceMarkedAt()));
        attendanceRepository.save(attendance);
    }

    public Long getTotalAttendance(Long orgId, Long month, Long year) {
        LocalDate endDate = LocalDate.of(year.intValue(), month.intValue(), 1).plusMonths(1).minusDays(1);
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return attendanceRepository.getTotalAttendanceForMonth(orgId, formattedEndDate);
    }
}
