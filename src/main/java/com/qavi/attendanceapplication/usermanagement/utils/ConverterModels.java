package com.qavi.attendanceapplication.usermanagement.utils;

import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.UserDataModel;

public class ConverterModels {
    public static UserDataModel convertUserToUserDataModel(User user) {
        UserDataModel userDataModel = new UserDataModel();
        userDataModel.setFirstName(user.getFirstName());
        userDataModel.setLastName(user.getLastName());
        userDataModel.setEmail(user.getEmail());
        userDataModel.setCnicNumber(user.getCnicNumber());
        userDataModel.setPhone_number(user.getPhoneNumber());
        userDataModel.setCountryCode(user.getCountryCode());
        userDataModel.setId(user.getId());
        userDataModel.setCity(user.getCity());
        userDataModel.setCountry(user.getCountry());
        userDataModel.setAddress(user.getAddress());
        userDataModel.setRoles(user.getRole());
        userDataModel.setAuthType(user.getAuthType());
        userDataModel.setEnabled(user.isEnabled());
        userDataModel.setLastLoginAt(user.getLastLoginAt());
        return userDataModel;
    }
}

