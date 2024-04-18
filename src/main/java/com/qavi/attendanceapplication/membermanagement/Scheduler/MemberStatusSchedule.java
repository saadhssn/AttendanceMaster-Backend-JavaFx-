package com.qavi.attendanceapplication.membermanagement.Scheduler;

import com.qavi.attendanceapplication.membermanagement.services.MemberStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemberStatusSchedule {
    @Autowired
    private MemberStatusService memberStatusService;

//    @Scheduled(cron = "0 54 16 * * ?")
    @Scheduled(cron = "0 0 23 L * ?")
    public void runMonthlyTask() {
        memberStatusService.calculateAndUpdate();
    }

}
