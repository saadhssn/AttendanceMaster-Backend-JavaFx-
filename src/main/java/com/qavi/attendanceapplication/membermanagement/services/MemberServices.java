package com.qavi.attendanceapplication.membermanagement.services;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.repositories.AttendanceRepository;
import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.entities.MemberImages;
import com.qavi.attendanceapplication.membermanagement.entities.MemberStatus;
import com.qavi.attendanceapplication.membermanagement.models.MemberDataModel;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.qavi.attendanceapplication.membermanagement.repositories.MemberImageRepository;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberStatusRepository;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MemberServices {

    @Autowired
    MemberImageRepository memberImageRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    public Long registerMember(Member member, String orgId) {
        try {
            member.setActive(true);
            member.setActiveDate(LocalDateTime.now());
            Member savedMember = memberRepository.save(member);

            Optional<Organization> organizationOptional = organizationRepository.findByIdWithMembers(Long.valueOf(orgId));
            Organization organization = organizationOptional.orElseThrow();

            List<Member> organizationMembers = new ArrayList<>(organization.getMembers());
            organizationMembers.add(savedMember);
            organization.setMembers(organizationMembers);

            organizationRepository.save(organization);

            return savedMember.getId();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public void saveMemberImage(Long profileImgId, Long appUserId) {
        MemberImages savedImg = memberImageRepository.findById(profileImgId).get();
        Member member = getUser(appUserId);
        member.setProfileImage(savedImg);
        memberRepository.save(member);
    }

    public Member getUser(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> getAllMembers(Long orgId) {
        return memberRepository.findAllByOrganizationId(orgId);
    }

    public Long getAllMembersCount(Long orgId, Long month, Long year) {
        LocalDate endDate = LocalDate.of(year.intValue(), month.intValue(), 1).plusMonths(1).minusDays(1);
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return memberRepository.countMembersByOrganizationId(orgId, formattedEndDate);
    }

    public List<Map<String, Long>> getMembersOfEachMonth(Long orgId) {
        return memberRepository.getMembersOfEachMonth(orgId);
    }

    public List<Map<String, Long>> getMembersStatus(Long orgId, Long year) {
        return memberRepository.getMembersStatusEachMonth(orgId, year);
    }

    public List<Map<String, Long>> getDropouts(Long orgId, Long year) {
        return memberRepository.getDropOuts(orgId, year);
    }

    public List<Map<Long, Long>> getWeeklyAnalysis(Long orgId) {
        List<Map<Long, Long>> weeklyAnalysis = memberRepository.getWeeklyAnalysis(orgId);
        List<Long> days = new ArrayList<>();

        for (Map<Long, Long> dayAnalysis : weeklyAnalysis) {
            days.addAll(dayAnalysis.keySet());
        }


        return weeklyAnalysis;
    }

    public List<Map<String, Long>> getAttendanceStatus(Long orgId, Long year, Long month) {
        return memberRepository.getAttendanceStatusOfMembers(orgId, year, month);
    }

    public Long getAllMembersActive(Long orgId, Long month, Long year) {
        LocalDate endDate = LocalDate.of(year.intValue(), month.intValue(), 1).plusMonths(1).minusDays(1);
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return memberRepository.countMembersActiveByOrganizationId(orgId, formattedEndDate);
    }

    public Long getAllMembersNonActive(Long orgId, Long month, Long year) {
        LocalDate endDate = LocalDate.of(year.intValue(), month.intValue(), 1).plusMonths(1).minusDays(1);
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return memberRepository.countMembersNonActiveByOrganizationId(orgId, formattedEndDate);
    }

    public List<Map<String, Long>> getAttendanceAnalysis(Long orgId, String yearMonth, LocalDate attendanceDate) {
        String formattedDate = attendanceDate.format(DateTimeFormatter.ISO_DATE);
        System.out.println(orgId);
        System.out.println(yearMonth);
        System.out.println(formattedDate);
        return memberRepository.getAttendanceAnalysis(orgId, yearMonth, formattedDate);
    }

    public List<Map<String, Long>> getAttendanceCount(Long orgId, String yearMonth, LocalDate attendanceDate) {
        String formattedDate = attendanceDate.format(DateTimeFormatter.ISO_DATE);
        System.out.println(orgId);
        System.out.println(yearMonth);
        System.out.println(formattedDate);
        return memberRepository.getAttendanceDetails(orgId, formattedDate);
    }

    public boolean deactivateMember(Long memberId) {
        int rowsUpdated = memberRepository.deactivateMember(memberId);

        Boolean Status = memberRepository.getStatus(memberId);
        if (!Status) {
            memberRepository.setInActiveDate(memberId, LocalDateTime.now());
        } else {
            memberRepository.setActiveDate(memberId, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    //Update Member
    public Boolean updateMember(MemberDataModel memberDataModel, Long id) {
        try {
            Member member = memberRepository.findById(id).get();
            member.setName(memberDataModel.getName());
            member.setEmail(memberDataModel.getEmail());
            member.setPhoneNumber(memberDataModel.getPhoneNumber());
            member.setRFID(memberDataModel.getRFID());
            member.setAddress(memberDataModel.getAddress());
            member.setDesignation(memberDataModel.getDesignation());
            member.setDateOfBirth(memberDataModel.getDateOfBirth());
            memberRepository.save(member);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
