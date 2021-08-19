package com.example.demo.web;

// import java.util.HashSet;
// import java.util.Set;

import javax.persistence.*;
import lombok.Data;

@Entity(name = "UserData")
@Table(name = "userdata", uniqueConstraints = { @UniqueConstraint(name = "user_id_unique", columnNames = "id") })
@Data
public class UserData {
    @Id
    // @SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence",
    // allocationSize = 1)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
    // "id_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "keycloakid", nullable = true, unique = true

    )
    String keycloakid;

    @Column(name = "userName", nullable = true, columnDefinition = "TEXT")
    String userName;

    @Column(name = "customRoles", nullable = true)
    // Set<String> customRoles = new HashSet<String>();
    String CustomRoles;

    public UserData(String userName, String CustomRoles, String keycloakid) {
        this.userName = userName;
        this.CustomRoles = CustomRoles;
        this.keycloakid = keycloakid;
    }

    public UserData() {

    }
}
