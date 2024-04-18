package com.qavi.attendanceapplication.membermanagement.services;
import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.entities.MemberImages;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class MemberImageService {
    @Autowired
    MemberServices memberServices;
    @Value("${spring.application.baseUrl}")
    private String baseUrl;

    @Autowired
    MemberImageRepository profileImageRepository;

    public Long saveFileKey(String uploadedFileKey) {
        MemberImages image = MemberImages.builder().key(uploadedFileKey).build();
        MemberImages savedImg = profileImageRepository.save(image);
        return savedImg.getId();
    }



    public Map<String, Object> getProfileImgData(Long userId) {

        Map<String, Object> data = new HashMap<>();
        Member user = memberServices.getUser(userId);

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
