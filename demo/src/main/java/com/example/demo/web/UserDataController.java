package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.MediaType;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserDataController {
    @Autowired
    private UserDataRepository userdataRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserData>> getAllUsers(@RequestParam(required = false) String keycloakid) {
        try {
            List<UserData> users = new ArrayList<UserData>();

            if (keycloakid == null)
                userdataRepository.findAll().forEach(users::add);
            // else
            // userdataRepository.findBykeycloakid(keycloakid).forEach(users::add);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserData> getUserDataById(@PathVariable("id") long id) {
        Optional<UserData> tutorialData = userdataRepository.findById(id);

        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<UserData> createUserData(@RequestBody UserData userData) {
        try {
            UserData _userdata = userdataRepository
                    .save(new UserData(userData.getUserName(), userData.getCustomRoles(), userData.getKeycloakid()));
            return new ResponseEntity<>(_userdata, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserData> updateTutorial(@PathVariable("id") long id, @RequestBody UserData userdata) {
        Optional<UserData> tutorialData = userdataRepository.findById(id);

        if (tutorialData.isPresent()) {
            UserData _userdata = tutorialData.get();
            _userdata.setUserName(userdata.getUserName());
            _userdata.setCustomRoles(userdata.getCustomRoles());
            _userdata.setKeycloakid(userdata.getKeycloakid());
            return new ResponseEntity<>(userdataRepository.save(_userdata), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserData(@PathVariable("id") long id) {
        try {
            userdataRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUserData() {
        try {
            userdataRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserData handleUserInfoSave(Principal principal) {

        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();

        UserData user = new UserData();
        if(accessToken != null){
        user.setKeycloakid(accessToken.getSubject());
        user.setUserName(accessToken.getPreferredUsername());
        if (accessToken.getResourceAccess().get("demo-springboot") != null) {
            Set<String> otherClaims = accessToken.getResourceAccess().get("demo-springboot").getRoles();

            String otherRoles = String.join(",", otherClaims);
            user.setCustomRoles(otherRoles);
        }

        Optional<UserData> tutorialData = userdataRepository.findBykeycloakid(user.getKeycloakid());

        if (tutorialData.isPresent()) {
            return tutorialData.get();
        } else {
            userdataRepository.save(user);
            return user;
        }

    }else{
        return user;
    }

}
}