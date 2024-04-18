package com.qavi.attendanceapplication.usermanagement.filters;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qavi.attendanceapplication.usermanagement.entities.permisions.PermissionAssigned;
import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import com.qavi.attendanceapplication.usermanagement.models.LoginResponseModel;
import com.qavi.attendanceapplication.usermanagement.models.PermissionBitsModel;
import com.qavi.attendanceapplication.usermanagement.utils.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.qavi.attendanceapplication.AttendanceApplication.loginExpiryTimeMinutes;
import static com.qavi.attendanceapplication.AttendanceApplication.secretJWT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authenticate = authenticationManager.authenticate(authentication);

        // Check if the user is enabled
        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();
        if (!user.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }

        // Check if the user is a customer, and deny authentication if true
        Collection<Role> roles = user.getRoles();
        boolean isOnlyCustomer = roles.size() == 1 && roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_CUSTOMER"));

        if (isOnlyCustomer) {
            throw new BadCredentialsException("Customers are not allowed to log in.");
        }
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Collection<Role> roles = user.getRoles();
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

        Algorithm algorithm = Algorithm.HMAC256(secretJWT.getBytes());
        String accessToken = com.auth0.jwt.JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(loginExpiryTimeMinutes)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", newRoles)
                .withClaim("permissions", permissionBitsModelList.stream().map(p -> p.getPermission()).collect(Collectors.toList()))
                .withClaim("permissionBits", permissionBitsModelList.stream().map(p -> p.getPermissionBit()).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = com.auth0.jwt.JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(5)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        LoginResponseModel loginResponse = new LoginResponseModel();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        if (user.getId() != null) {
            loginResponse.setUserId(user.getId().toString());
        }

        loginResponse.setEmail(user.getEmail());
        loginResponse.setRoles(newRoles);
        loginResponse.setFirstName(user.getFirstName());

        loginResponse.setPermissions(permissionBitsModelList);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMessage = "Invalid Credentials!";

        if (failed instanceof DisabledException) {
            errorMessage = "User account is disabled";
        } else if (failed instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> error = new HashMap<>();
        error.put("error_message", errorMessage);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

}
