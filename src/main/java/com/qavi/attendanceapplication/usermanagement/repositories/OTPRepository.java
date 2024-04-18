package com.qavi.attendanceapplication.usermanagement.repositories;

import com.qavi.attendanceapplication.usermanagement.entities.OTP.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OTPRepository extends JpaRepository<OTP,Long> {


    @Query(value = "SELECT email FROM otp WHERE otp = :otp AND generated_time > now() - interval '10 minutes'", nativeQuery = true)
    String findEmailByOtpAndGeneratedTime(String otp);


    @Modifying
    @Query(value="delete from otp where email=:email",nativeQuery = true)
    void deleteByEmail(String email);
}
