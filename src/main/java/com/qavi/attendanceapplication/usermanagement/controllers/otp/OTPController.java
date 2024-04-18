package com.qavi.attendanceapplication.usermanagement.controllers.otp;

import com.qavi.attendanceapplication.usermanagement.entities.OTP.OTP;
import com.qavi.attendanceapplication.usermanagement.entities.OTP.PasswordUpdate;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import com.qavi.attendanceapplication.usermanagement.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/otp")
public class OTPController {

    @Autowired
    OTPService otpService;

    @PostMapping("/sendOTP")
    public ResponseEntity<ResponseModel> sendOTP(@RequestBody OTP otp) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("OTP send successfully")
                .data(otpService.sendEmailOTP(otp))
                .build();

        if (responseModel.getData() == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Send OTP");
        }
        return new ResponseEntity<>(responseModel, responseModel.getStatus());
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseModel> resetPassword(@RequestBody PasswordUpdate passwordUpdate) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Password updated successfully")
                .data(otpService.resetPassword(passwordUpdate))
                .build();

        if (responseModel.getData() == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Send OTP");
        }
        return new ResponseEntity<>(responseModel, responseModel.getStatus());
    }

}
