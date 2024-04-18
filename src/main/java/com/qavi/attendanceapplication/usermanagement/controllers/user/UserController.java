package com.qavi.attendanceapplication.usermanagement.controllers.user;

import com.qavi.attendanceapplication.usermanagement.entities.user.PasswordUpdate;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import com.qavi.attendanceapplication.usermanagement.models.UserDataModel;
import com.qavi.attendanceapplication.usermanagement.services.user.GoogleSignInService;
import com.qavi.attendanceapplication.usermanagement.services.user.ProfileImageService;
import com.qavi.attendanceapplication.usermanagement.services.user.UserService;
import com.qavi.attendanceapplication.usermanagement.utils.ConverterModels;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private GoogleSignInService googleSignInService;

    @Autowired
    private ProfileImageService profileImageService;

    //Get All Users
    @GetMapping("/getAll/{OrgId}")
    public ResponseEntity<List<UserDataModel>> getAllUser(@PathVariable Long OrgId) {
        List<User> users = userService.getAllUsers(OrgId);
        List<UserDataModel> convertedList = new ArrayList<>();
        for (User user : users) {
            convertedList.add(ConverterModels.convertUserToUserDataModel(user));
        }
        return new ResponseEntity<List<UserDataModel>>(convertedList, HttpStatus.OK);
    }

    //Get User By Id
    @GetMapping("/{id}")
    public ResponseEntity<UserDataModel> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserDataModel userDataModel = ConverterModels.convertUserToUserDataModel(user);
        return new ResponseEntity<UserDataModel>(userDataModel, HttpStatus.OK);
    }

    // get total employees count
    @PostMapping("signIn/")
    public ResponseEntity<ResponseModel> googleSignIn(@RequestBody UserDataModel userDataModel) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User created successfully")
                .data(new Object())
                .build();

        String accessToken = googleSignInService.SignIn(userDataModel);
        responseModel.setData(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //Create Employee
    @PostMapping("/register/{OrgId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel> createUser(@RequestBody User user, @PathVariable String OrgId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User created successfully")
                .data(new Object())
                .build();

        if (userService.existsByEmail(user.getEmail())) {
            responseModel.setStatus(HttpStatus.BAD_REQUEST);
            responseModel.setMessage("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        Long userId = userService.createUser(user, OrgId);
        if (userId == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed to create user");
        } else {
            responseModel.setData(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //create Admin
    @PostMapping("/registerAdmin")
    public ResponseEntity<ResponseModel> createAdmin(@RequestBody User user) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Admin registered successfully")
                .data(new Object())
                .build();

        if (userService.existsByEmail(user.getEmail())) {
            responseModel.setStatus(HttpStatus.BAD_REQUEST);
            responseModel.setMessage("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        Long userId = userService.createAdmin(user);
        if (userId == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed to Register Admin");
        } else {
            responseModel.setData(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //Update User
    @PutMapping("/update-user/{id}")
    public ResponseEntity<ResponseModel> updateUser(@RequestBody UserDataModel userDataModel, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User updated successfully")
                .data(new Object())
                .build();
        if (!userService.updateUser(userDataModel, id)) {
            responseModel.setMessage("Failed to update user");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    //Soft-delete user
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<ResponseModel> softDeleteUser(@PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Id Soft Deleted")
                .data(new Object())
                .build();
        if (!userService.softDeleteUser(id)) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Soft-Delete Id");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        Boolean deletedUser = userService.deleteUser(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    @GetMapping("/recentRegistration/{OrgId}")
    public  ResponseEntity<List<UserDataModel>> RecentRegistrations(@PathVariable Long OrgId){

        List<User> users = userService.getRecentRegistration(OrgId);
        List<UserDataModel> convertedList = new ArrayList<>();
        for (User user : users) {
            convertedList.add(ConverterModels.convertUserToUserDataModel(user));
        }
        return new ResponseEntity<List<UserDataModel>>(convertedList, HttpStatus.OK);
    }

    // Change Password
    @PutMapping("/update-password/{id}")
    public ResponseEntity<ResponseModel> editPassword(@RequestBody PasswordUpdate passwordUpdate, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Password updated successfully")
                .data(new Object())
                .build();
        if (!userService.updatePassword(passwordUpdate, id)) {
            responseModel.setMessage("Failed to update password");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Map<String, Object>> getProfileImageData(@PathVariable Long userId) {
        Map<String, Object> profileImageData = profileImageService.getProfileImgData(userId);
        return ResponseEntity.ok(profileImageData);
    }



}