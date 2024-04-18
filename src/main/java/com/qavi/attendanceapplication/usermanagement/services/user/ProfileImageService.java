package com.qavi.attendanceapplication.usermanagement.services.user;


import com.qavi.attendanceapplication.usermanagement.entities.user.ProfileImages;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.repositories.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileImageService {
    @Autowired
    ProfileImageRepository profileRepository;

    @Autowired
    UserService userService;
    @Value("${spring.application.baseUrl}")
    private String baseUrl;

    public Long saveFileKey(String uploadedFileKey) {
        ProfileImages image = ProfileImages.builder().key(uploadedFileKey).build();
        ProfileImages savedImg = profileRepository.save(image);
        return savedImg.getId();
    }


    public Map<String, Object> getProfileImgData(Long userId) {

        Map<String, Object> data = new HashMap<>();
        User user = userService.getUser(userId);

        if (user.getProfileImage() == null) {
            data.put("id", null);
            data.put("url", null);
        } else {
            data.put("id", user.getProfileImage().getId());
            data.put("url", "/api/file/" + user.getProfileImage().getKey());
        }
        return data;
    }
}