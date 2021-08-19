package com.example.demo.web;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/")
public class UserServiceController {

    @Autowired
    private UserDataRepository userdataRepository;

    @GetMapping(value = "/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
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