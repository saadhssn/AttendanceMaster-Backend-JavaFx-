package com.qavi.attendanceapplication.usermanagement.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String cnicNumber;
    private String countryCode;
    @Column(unique = true)
    private String email;
    private String password;
    @OneToOne(orphanRemoval = true)
    private ProfileImages profileImage;
    private String phoneNumber;
    private boolean enabled;
    @JsonIgnore

    private String authType;
    private String country;
    private String deviceId;
    private String address;
    private String fcmToken;
    private LocalDateTime lastLoginAt;
    private String appleIdentifier;
    private LocalDateTime registeredAt;
    private String city;
    private boolean emailNotificationEnabled;

    //   @OneToMany
    //TODO
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "Role_Assigned", joinColumns =
    @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id"))

    private Set<Role> role;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}

