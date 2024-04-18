package com.qavi.attendanceapplication.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleModel {
    String name;
    Long roleId;
    List<PermissionBitsModel> privileges;

}

