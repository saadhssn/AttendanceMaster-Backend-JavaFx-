package com.qavi.attendanceapplication.membermanagement.controllers;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.entities.MemberStatus;
import com.qavi.attendanceapplication.membermanagement.models.MemberDataModel;
import com.qavi.attendanceapplication.membermanagement.services.MemberServices;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/member")
public class MemberController {

    @Autowired
    MemberServices memberServices;

    // register employee
    @PostMapping("/register/{OrgId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel> registerMembers(@RequestBody Member member,@PathVariable String OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Member registered successfully")
                .data(new Object())
                .build();
        Long memId = memberServices.registerMember(member,OrgId);
        if (memId == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed to register Member");
        } else {
            responseModel.setData(memId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberServices.getUser(id);
        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }

    @GetMapping("/getAll/{OrgId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers(@PathVariable Long OrgId) {
        List<Member> members = memberServices.getAllMembers(OrgId);
        return new ResponseEntity<List<Member>>(members, HttpStatus.OK);
    }


    @GetMapping("/getAllMembersCount/{month}/{year}/{OrgId}")
    public ResponseEntity<Long> getAllMembersCount(@PathVariable Long OrgId,@PathVariable Long month,@PathVariable Long year) {
        Long MembersCount = memberServices.getAllMembersCount(OrgId,month,year);
        return new ResponseEntity<Long>(MembersCount, HttpStatus.OK);
    }

    @GetMapping("/members-by-month/{orgId}")
    public ResponseEntity<List<Map<String, Long>>> getMembersByMonth(@PathVariable Long orgId) {
        List<Map<String, Long>> result = memberServices.getMembersOfEachMonth(orgId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/members-status-by-months/{year}/{orgId}")
    public ResponseEntity<List<Map<String, Long>>> getMembersStatus(@PathVariable Long orgId,@PathVariable Long year) {
        List<Map<String, Long>> result = memberServices.getMembersStatus(orgId,year);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/dropouts-by-months/{year}/{orgId}")
    public ResponseEntity<List<Map<String, Long>>> getDropouts(@PathVariable Long orgId,@PathVariable Long year) {
        List<Map<String, Long>> result = memberServices.getDropouts(orgId,year);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/weekly-registration-analysis/{orgId}")
    public ResponseEntity<List<Map<Long, Long>>> getWeeklyAnalysis(@PathVariable Long orgId) {
        List<Map<Long, Long>> result = memberServices.getWeeklyAnalysis(orgId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Member Analysis Graph
    @GetMapping("/attendance-status-by-month/{year}/{month}/{orgId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<List<Map<String, Long>>> getAttendanceStatus(@PathVariable Long orgId,@PathVariable Long year,@PathVariable Long month) {
        List<Map<String, Long>> result = memberServices.getAttendanceStatus(orgId,year,month);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllMembersActive/{month}/{year}/{OrgId}")
    public ResponseEntity<Long> getAllMembersActive(@PathVariable Long OrgId,@PathVariable Long month,@PathVariable Long year) {
        Long MembersCount = memberServices.getAllMembersActive(OrgId,month,year);
        return new ResponseEntity<Long>(MembersCount, HttpStatus.OK);
    }

    @GetMapping("/getAllMembersNonActive/{month}/{year}/{OrgId}")
    public ResponseEntity<Long> getAllMembersNonActive(@PathVariable Long OrgId,@PathVariable Long month,@PathVariable Long year) {
        Long MembersCount = memberServices.getAllMembersNonActive(OrgId,month,year);
        return new ResponseEntity<Long>(MembersCount, HttpStatus.OK);
    }

    @GetMapping("/getAttendanceAnalysis/{orgId}/{year}/{month}/{day}")
    public ResponseEntity<List<Map<String, Long>>> getAttendanceAnalysis(
            @PathVariable Long orgId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day) {

        // Format yearMonth as "YYYY-MM"
        String yearMonth = String.format("%04d-%02d", year, month);

        LocalDate attendanceDate = LocalDate.of(year, month, day);
        List<Map<String, Long>> analysisResult = memberServices.getAttendanceAnalysis(orgId, yearMonth, attendanceDate);
        return new ResponseEntity<>(analysisResult, HttpStatus.OK);
    }

    @GetMapping("/getAttendanceCount/{orgId}/{year}/{month}/{day}")
    public ResponseEntity<List<Map<String, Long>>> getAttendanceCount(
            @PathVariable Long orgId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day) {
        System.out.println(LocalDateTime.now());
        // Format yearMonth as "YYYY-MM"
        String yearMonth = String.format("%04d-%02d", year, month);

        LocalDate attendanceDate = LocalDate.of(year, month, day);
        List<Map<String, Long>> analysisResult = memberServices.getAttendanceCount(orgId, yearMonth, attendanceDate);
        return new ResponseEntity<>(analysisResult, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> DeactivateStatus(@PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Successful")
                .data(new Object())
                .build();
        if (!memberServices.deactivateMember(id)) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed to perform operation");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //Update Member
    @PutMapping("/update-member/{id}")
    public ResponseEntity<ResponseModel> updateMember(@RequestBody MemberDataModel memberDataModel, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Member updated successfully")
                .data(new Object())
                .build();
        if (!memberServices.updateMember(memberDataModel, id)) {
            responseModel.setMessage("Failed to update member");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

}
