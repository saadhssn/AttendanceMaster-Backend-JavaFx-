package com.qavi.attendanceapplication.usermanagement.services.otp;

import com.qavi.attendanceapplication.usermanagement.entities.OTP.OTP;
import com.qavi.attendanceapplication.usermanagement.entities.OTP.PasswordUpdate;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.repositories.OTPRepository;
import com.qavi.attendanceapplication.usermanagement.repositories.UserRepository;
import com.qavi.attendanceapplication.usermanagement.services.user.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    OTPRepository otpRepository;

    @Autowired
    EmailService emailService;

    public String sendEmailOTP(OTP otpUser) {
        Optional<User> userOptional = userRepository.findByEmail(otpUser.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String userEmail = user.getEmail();
            String otp = generateOTP();
            String subject = "OTP Code";
            String message = "Hi " + user.getFirstName() + ",\nYour OTP Code is . \n" + otp +"\n" + user.getEmail();
            emailService.sendEmail(userEmail, subject, message);

            OTP otp1 = new OTP();
            otp1.setOTP(otp);
            otp1.setEmail(userEmail);
            otp1.setGeneratedTime(LocalDateTime.now());
            otpRepository.save(otp1);
            return "Email sent successfully";
        } else {
            return "User Doesn't Exist";
        }
    }

    public static String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000;
        return String.format("%06d", otp);
    }


    @Transactional
    public String resetPassword(PasswordUpdate passwordUpdate) {
        String email = otpRepository.findEmailByOtpAndGeneratedTime(passwordUpdate.getOtp());
        if (StringUtils.hasText(email)) {

            userRepository.updatePassword(email, passwordEncoder.encode(passwordUpdate.getNewPassword()));
            otpRepository.deleteByEmail(email);
            return "Password Updated";
        } else {
            return "No record found";
        }
    }
}
