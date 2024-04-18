package com.qavi.attendanceapplication.feesmanagement.controllers;

import com.qavi.attendanceapplication.attendancemanagement.entities.Attendance;
import com.qavi.attendanceapplication.attendancemanagement.models.AttendanceModel;
import com.qavi.attendanceapplication.attendancemanagement.utils.AttendanceConverter;
import com.qavi.attendanceapplication.feesmanagement.entities.Fees;
import com.qavi.attendanceapplication.feesmanagement.entities.RequestFees;
import com.qavi.attendanceapplication.feesmanagement.models.FeesModel;
import com.qavi.attendanceapplication.feesmanagement.services.FeesServices;
import com.qavi.attendanceapplication.feesmanagement.utils.FeesConveter;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/fees")
public class FeesController {
    @Autowired
    FeesServices feesServices;

    //Create Fees

    @PostMapping("/register/{OrgId}")
    public ResponseEntity<ResponseModel> createFees(@RequestBody FeesModel feesModel,@PathVariable Long OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Fees Created Successfully")
                .data(new Object())
                .build();
        Long feesId = feesServices.registerFees(feesModel,OrgId);
        if (feesId == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Create Fees");
        } else {
            responseModel.setData(feesId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //Delete Fees
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteFees(@PathVariable Long id) {
        Boolean deletedFees = feesServices.deleteFees(id);
        return new ResponseEntity<>(deletedFees, HttpStatus.OK);
    }

    //Get All Fees
    @GetMapping("/getAll/{OrgId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<List<FeesModel>> getAllFess(@PathVariable Long OrgId) {
        List<Fees> fees = feesServices.getAllFees(OrgId);
        List<FeesModel> convertedList = new ArrayList<>();
        for (Fees fees1 : fees) {
            convertedList.add(FeesConveter.ConvertFeesToFeesModel(fees1));
        }
        return new ResponseEntity<List<FeesModel>>(convertedList, HttpStatus.OK);
    }

    @GetMapping("/paid-users/{orgId}/{year}")
    public ResponseEntity<List<Map<String, Long>>> getPaidUsersByMonthAndYear(
            @PathVariable Long orgId,
            @PathVariable String year
    ) {
        List<Map<String, Long>> result = feesServices.getPaidUsersByMonthAndYear(orgId, year);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Get Fees By Id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getFessById(@PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Fees Fetched Successfully")
                .data(feesServices.getFees(id))
                .build();

        if (responseModel.getData() == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Fetch Fees record");
        }

        return new ResponseEntity<>(responseModel, responseModel.getStatus());
    }


    //Update Fees
    @PutMapping("/update-fees/{id}")
    public ResponseEntity<ResponseModel> updateFees(@RequestBody RequestFees requestFees, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Fees Updated Successfully")
                .data(new Object())
                .build();
        if (!feesServices.updateFees(requestFees, id)) {
            responseModel.setMessage("Update Failed");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

}
