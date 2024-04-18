package com.qavi.attendanceapplication.attendancemanagement.controllers;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.entities.AttendanceRequest;
import com.qavi.attendanceapplication.attendancemanagement.models.AttendanceModel;
import com.qavi.attendanceapplication.attendancemanagement.services.AttendanceService;
import com.qavi.attendanceapplication.attendancemanagement.utils.AttendanceConverter;
import com.qavi.attendanceapplication.attendancemanagement.utils.AttendanceMembersDTO;
import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import com.qavi.attendanceapplication.usermanagement.models.UserDataModel;
import com.qavi.attendanceapplication.usermanagement.utils.ConverterModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    //Attendance for list
    @PostMapping("/list/{OrgId}")
    ResponseEntity<ResponseModel> createAttendance(@RequestBody AttendanceRequest attendanceRequest,@PathVariable Long OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance Marked successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.markAttendance(attendanceRequest.getAttendanceDataList(),OrgId);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to Mark Attendance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseModel);
        }
    }

    @PostMapping("/list/desktop/{OrgId}")
    ResponseEntity<ResponseModel> createAttendanceDesktop(@RequestBody AttendanceRequest attendanceRequest,@PathVariable Long OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance Marked successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.markAttendanceDesktop(attendanceRequest.getAttendanceDataList(),OrgId);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to Mark Attendance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseModel);
        }
    }

    //Attendance for Individual
    @PostMapping("/individual/{OrgId}")
    ResponseEntity<ResponseModel> markAttendance(@RequestBody AttendanceRequest request, @PathVariable Long OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance Marked successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.markIndividualAttendance(request.getAttendanceData(),OrgId);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to Mark Attendance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseModel);
        }
    }


    @PostMapping("/rfid/{OrgId}")
    ResponseEntity<ResponseModel> markAttendanceByRFID(@RequestBody AttendanceRequest request, @PathVariable Long OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance Marked successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.markIndividualAttendanceByRFID(request.getAttendanceData(),OrgId);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to Mark Attendance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseModel);
        }
    }



    @GetMapping("/getAll/{OrgId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<List<AttendanceMembersDTO>> getAttendance(@PathVariable Long OrgId) {
        try {
            List<AttendanceMembersDTO> attendance = attendanceService.getMembersWithAttendance(OrgId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(attendance);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }




    @GetMapping("/getSingle/{id}")
    public ResponseEntity<ResponseModel> getSingleAttendance(@PathVariable Long id) {
        try {
            Attendance attendance = attendanceService.getAttendance(id);
            List<AttendanceModel> convertedList = new ArrayList<>();
            convertedList.add(AttendanceConverter.covertAtoAM(attendance));

            ResponseModel responseModel = ResponseModel.builder()
                    .status(HttpStatus.OK)
                    .message("Attendance Fetched successfully")
                    .data(convertedList)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            ResponseModel responseModel = ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Failed to Fetch Attendance")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }




    // remove attendance
    @DeleteMapping("/remove/{id}")
    ResponseEntity<ResponseModel> removeAttendance(@PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance removed successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.removeAttendance(id);
            responseModel.setData(id);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to remove Attendance");
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }
    }

    //Update Attendance
    @PutMapping("/update/{id}")
    ResponseEntity<ResponseModel> updateAttendance(@RequestBody AttendanceRequest attendanceRequest, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Attendance Updated successfully")
                .data(new Object())
                .build();
        try {
            attendanceService.update(attendanceRequest.getAttendanceData(), id);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to Update Attendance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseModel);
        }
    }

    @GetMapping("/getTotalAttendance/{month}/{year}/{OrgId}")
    public ResponseEntity<ResponseModel> getTotalAttendance(@PathVariable Long OrgId,@PathVariable Long month,@PathVariable Long year) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Fees Fetched Successfully")
                .data(attendanceService.getTotalAttendance(OrgId,month,year))
                .build();

        if (responseModel.getData() == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Fetch Fees record");
        }

        return new ResponseEntity<>(responseModel, responseModel.getStatus());
    }


}
