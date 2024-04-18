package com.qavi.attendanceapplication.usermanagement.repositories;

import com.qavi.attendanceapplication.usermanagement.entities.user.ProfileImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImages, Long> {
}
