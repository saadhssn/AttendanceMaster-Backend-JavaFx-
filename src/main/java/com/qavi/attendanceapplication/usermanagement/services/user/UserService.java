package com.qavi.attendanceapplication.usermanagement.services.user;
import com.qavi.attendanceapplication.usermanagement.entities.OTP.OTP;
import org.springframework.dao.DataAccessException;
import javax.persistence.PersistenceException;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import com.qavi.attendanceapplication.usermanagement.constants.UserConstants;
import com.qavi.attendanceapplication.usermanagement.entities.permisions.PermissionAssigned;
import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import com.qavi.attendanceapplication.usermanagement.entities.user.PasswordUpdate;
import com.qavi.attendanceapplication.usermanagement.entities.user.ProfileImages;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.models.UserDataModel;
import com.qavi.attendanceapplication.usermanagement.repositories.ProfileImageRepository;
import com.qavi.attendanceapplication.usermanagement.repositories.RoleRepository;
import com.qavi.attendanceapplication.usermanagement.repositories.UserRepository;
import com.qavi.attendanceapplication.usermanagement.utils.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messages;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        } else if (user.isEnabled()) {
            Collection<Role> roles = user.getRole();
            Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>() {
            };
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getPermissionAssigned().toString()));
            }
            CustomUserDetails customUserDetail = new CustomUserDetails(user);
            customUserDetail.setUser(user);
            customUserDetail.setAuthorities(authorities);
            return customUserDetail;
        }else{
            throw new UsernameNotFoundException("User not found with email: " + email);

        }

    }

    public Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        return getGrantedAuthorities(getPermissions(roles));
    }

    public List<String> getPermissions(Collection<Role> roles) {

        List<String> permissions = new ArrayList<>();
        for (Role role : roles) {
            Collection<PermissionAssigned> permissionsAssignedToRole = role.getPermissionAssigned();
            for (PermissionAssigned permissionAssigned : permissionsAssignedToRole) {
                if (!permissions.contains(permissionAssigned.getPermission().getName())) {
                    permissions.add(permissionAssigned.getPermission().getName() + " " + permissionAssigned.getPermissionBits());
                }
            }
        }
        return permissions;
    }

    public List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    //GET ONE USER
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    //GET ALL USER
    public List<User> getAllUsers(Long OrgId) {
        return userRepository.findAllUserByOrganizationId(OrgId);
    }

    public List<User> getRecentRegistration(Long OrgId) {
        return userRepository.findRecentUsersByOrganizationId(OrgId);
    }

    //Create User

    public Long createUser(User user, String OrgId) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRegisteredAt(LocalDateTime.now());
            user.setAuthType(UserConstants.LOCAL);
            user.setEnabled(true);
            user.setRole(Set.of(roleRepository.searchByName("ROLE_EMPLOYEE")));
            user.setEmailNotificationEnabled(true);
            User savedUser = userRepository.save(user);
            Organization organization = organizationRepository.findById(Long.valueOf(OrgId)).orElseThrow();
            List<User> organizationUsers = new ArrayList<>(organization.getUsers());

            organizationUsers.add(savedUser);
            organization.setUsers(organizationUsers);

            organizationRepository.save(organization);
            return savedUser.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long createAdmin(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRegisteredAt(LocalDateTime.now());
            user.setAuthType(UserConstants.LOCAL);
            user.setEnabled(true);
            user.setRole(Set.of(roleRepository.searchByName("ROLE_ADMIN")));
            user.setEmailNotificationEnabled(true);
            User savedUser = userRepository.save(user);

            return savedUser.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create User Employee

    //UpdateUser
    public Boolean updateUser(UserDataModel userDataModel, Long id) {
        try {
            User user = userRepository.findById(id).get();
            user.setFirstName(userDataModel.getFirstName());
            user.setLastName(userDataModel.getLastName());
            user.setEmail(userDataModel.getEmail());
            user.setCountryCode(userDataModel.getCountryCode());
            user.setCnicNumber(userDataModel.getCnicNumber());
            user.setPhoneNumber(userDataModel.getPhone_number());
            user.setAddress(userDataModel.getAddress());
            user.setCountry(userDataModel.getCountry());
            user.setCity(userDataModel.getCity());

            userRepository.save(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public Optional<User> findUserByEmailAndType(String email, String authType) {
        return userRepository.findByEmail(email, authType);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveProfileImage(Long profileImgId, Long appUserId) {
        ProfileImages savedImg = profileImageRepository.findById(profileImgId).get();
        User user = getUser(appUserId);
        user.setProfileImage(savedImg);
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean updatePassword(PasswordUpdate passwordUpdate, Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && passwordEncoder.matches(passwordUpdate.getOldPassword(), user.getPassword())) {
            String newEncodedPassword = passwordEncoder.encode(passwordUpdate.getNewPassword());
            user.setPassword(newEncodedPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public boolean softDeleteUser(Long id) {
        try {
            userRepository.toggleUserStatus(id);
            return true;
        } catch (Exception e) {

            return false;
        }
    }






}
