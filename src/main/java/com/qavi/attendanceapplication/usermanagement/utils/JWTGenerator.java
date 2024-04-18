package com.qavi.attendanceapplication.usermanagement.utils;

import com.qavi.attendanceapplication.usermanagement.entities.permisions.PermissionAssigned;
import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.PermissionBitsModel;
import com.qavi.attendanceapplication.usermanagement.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.qavi.attendanceapplication.AttendanceApplication.loginExpiryTimeMinutes;
import static com.qavi.attendanceapplication.AttendanceApplication.secretJWT;

@Component
public class JWTGenerator {
    @Autowired
    UserService userService;
    private final HttpServletRequest request;


    public JWTGenerator(HttpServletRequest request) {
        this.request = request;
    }

    public String generateJWTToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretJWT.getBytes());

        CustomUserDetails customUserDetails = userService.loadUserByUsername(user.getEmail());
        Collection<Role> roles = customUserDetails.getRoles();
        List<PermissionBitsModel> permissionBitsModelList = new ArrayList<>();
        List<String> newRoles = new ArrayList<>();
        for (Role role : roles) {
            newRoles.add(role.getName());
            Collection<PermissionAssigned> fetchedPermission = role.getPermissionAssigned();
            for (PermissionAssigned permission : fetchedPermission) {
                if (!permissionBitsModelList.contains(permission.getPermission().getName())) {
                    PermissionBitsModel permissionBitsModel = new PermissionBitsModel();
                    permissionBitsModel.setPermission(permission.getPermission().getName());
                    permissionBitsModel.setPermissionBit(permission.getPermissionBits());
                    permissionBitsModelList.add(permissionBitsModel);
                }
            }
        }
        String accessToken = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(loginExpiryTimeMinutes)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", newRoles)
                .withClaim("permissions", permissionBitsModelList.stream().map(p -> p.getPermission()).collect(Collectors.toList()))
                .withClaim("permissionBits", permissionBitsModelList.stream().map(p -> p.getPermissionBit()).collect(Collectors.toList()))
                .sign(algorithm);

        return accessToken;
    }
}
