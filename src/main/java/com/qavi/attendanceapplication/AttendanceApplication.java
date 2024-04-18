package com.qavi.attendanceapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.ZoneId;

@SpringBootApplication
@CrossOrigin
@EnableScheduling
public class AttendanceApplication {

	public static final String secretJWT = "attendanceapplicationlkajjqieojqiojeksmndvjasfjhasdifjiwef";
	public static final Long loginExpiryTimeMinutes = 120L;
	public static final ZoneId zoneId = ZoneId.of("Asia/Karachi");

	public static void main(String[] args) {
		SpringApplication.run(AttendanceApplication.class, args);
	}

}
