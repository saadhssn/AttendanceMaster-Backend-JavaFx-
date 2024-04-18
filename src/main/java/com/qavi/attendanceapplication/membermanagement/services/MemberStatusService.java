package com.qavi.attendanceapplication.membermanagement.services;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.entities.MemberStatus;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberRepository;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberStatusRepository;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.SecondaryTable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberStatusService {
    @Autowired
    MemberServices memberServices;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberStatusRepository memberStatusRepository;
    @Autowired
    OrganizationRepository organizationRepository;

    @Transactional
    public void calculateAndUpdate() {
        List<Member> allMembers = memberRepository.findAll();
        Map<Long, Map<String, Long>> statusMap = new HashMap<>();

        for (Member member : allMembers) {
            List<Long> orgIds = organizationRepository.findOrgIdsByMemberId(member.getId());
            if (orgIds == null || orgIds.isEmpty()) {
                // Handle the case where organizations are not found for the member
                continue;
            }

            for (Long orgId : orgIds) {
                String statusKey = member.getActive() ? "activeUsers" : "inActiveUsers";

                statusMap.computeIfAbsent(orgId, k -> new HashMap<>())
                        .put(statusKey, statusMap.getOrDefault(orgId, new HashMap<>()).getOrDefault(statusKey, 0L) + 1);
            }
        }

        Calendar now = Calendar.getInstance();
        Long year = Long.valueOf(now.get(Calendar.YEAR));
        String month = getShortMonthName(now.get(Calendar.MONTH) + 1);

        for (Map.Entry<Long, Map<String, Long>> entry : statusMap.entrySet()) {
            Long orgId = entry.getKey();
            Map<String, Long> orgStatus = entry.getValue();

            MemberStatus memberStatus = memberStatusRepository.findByYearAndMonthAndOrgId(year, month, orgId)
                    .orElse(new MemberStatus());

            memberStatus.setYear(year);
            memberStatus.setMonth(month);
            memberStatus.setOrgId(orgId);
            memberStatus.setActiveUsers(orgStatus.getOrDefault("activeUsers", 0L));
            memberStatus.setInActiveUsers(orgStatus.getOrDefault("inActiveUsers", 0L));

            memberStatusRepository.save(memberStatus);
            System.out.println(memberStatus);
        }
    }

    private String getShortMonthName(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }

}
