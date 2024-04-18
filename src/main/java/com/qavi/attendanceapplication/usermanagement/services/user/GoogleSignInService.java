package com.qavi.attendanceapplication.usermanagement.services.user;


import com.qavi.attendanceapplication.usermanagement.constants.UserConstants;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.UserDataModel;
import com.qavi.attendanceapplication.usermanagement.repositories.RoleRepository;
import com.qavi.attendanceapplication.usermanagement.repositories.UserRepository;
import com.qavi.attendanceapplication.usermanagement.utils.JWTGenerator;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class GoogleSignInService {

    private final OkHttpClient apiClient;
    private final UserService userService;

    private final RoleRepository roleRepository;
    private final JWTGenerator jwtGenerator;

    private final UserRepository appUserRepository;


    public GoogleSignInService(UserService userService, RoleRepository roleRepository, JWTGenerator jwtGenerator, UserRepository appUserRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
        this.jwtGenerator = jwtGenerator;
        this.appUserRepository = appUserRepository;
        this.apiClient = new OkHttpClient.Builder()
                .build();
    }


    public String SignIn(UserDataModel userDataModel) {


        Optional<User> user = userService.findUserByEmail(userDataModel.getEmail());

        String accessToken = "";

        if (user.isPresent()) {
            var google = user.get().getAuthType().equals("GOOGLE");

            if (google) {
                accessToken = jwtGenerator.generateJWTToken(user.get());
            } else {
                accessToken = "The Provided email is not associated with Google sign up, Please provide password";
            }
        } else {
            // Create and save a new technician in the database
            User newUser = new User();
            newUser.setFirstName(userDataModel.getFirstName()); // Use values from userDataModel
            newUser.setLastName(userDataModel.getLastName());   // Use values from userDataModel
            newUser.setEnabled(true);
            newUser.setRole(Set.of(roleRepository.searchByName("ROLE_OWNER"), roleRepository.searchByName("ROLE_CUSTOMER")));
            newUser.setEmail(userDataModel.getEmail());        // Use values from userDataModel
            newUser.setAuthType(UserConstants.GOOGLE);
            newUser.setEmailNotificationEnabled(false);
            // Save the new user to the database
            appUserRepository.save(newUser);
            accessToken = jwtGenerator.generateJWTToken(newUser);
        }
        return accessToken;
    }

}

