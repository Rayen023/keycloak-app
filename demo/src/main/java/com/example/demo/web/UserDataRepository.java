package com.example.demo.web;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findBykeycloakid(String keycloakid);
}
