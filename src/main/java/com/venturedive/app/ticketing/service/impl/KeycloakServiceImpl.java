package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.service.KeycloakService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "keycloakService")
public class KeycloakServiceImpl implements KeycloakService {

    @Autowired
    private RestTemplate restTemplate;

    private Keycloak getKeycloakObject(){
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(GlobalConstant.AUTH_SERVER_URL)
                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
                .username(GlobalConstant.CONSOLE_USERNAME).password(GlobalConstant.CONSOLE_PASSWORD)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

        keycloak.tokenManager().getAccessToken();

        return keycloak;
    }

    private UserRepresentation getUserRepresentation(UserDto userDto){
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getEmail());
        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastname());
        user.setEmail(userDto.getEmail());

        return user;
    }

    @Override
    public Response createUser(UserDto userDto){
        Keycloak keycloak = getKeycloakObject();
        UserRepresentation user = getUserRepresentation(userDto);
        RealmResource realmResource = keycloak.realm(GlobalConstant.AUTH_SERVER_REALM);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(user);
        return response;
    }

    @Override
    public String getUserId(Response response){
        String userId = CreatedResponseUtil.getCreatedId(response);
        return userId;
    }

    @Override
    public void setPasswordCredentials(UserDto userDto, String userId){
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDto.getPassword());

        Keycloak keycloak = getKeycloakObject();
        keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).users().get(userId).resetPassword(passwordCred);
    }

    @Override
    public void assignRole(UserDto userDto, String userId, String role){
        Keycloak keycloak = getKeycloakObject();
        RoleRepresentation roleRepresentation = keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).roles().get(role).toRepresentation();
        keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
        keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).users().get(userId).roles().clientLevel(GlobalConstant.CLIENT_ID).add(Arrays.asList(roleRepresentation));
    }

    @Override
    public AuthDto getAccessToken(UserDto userDto){
        AuthDto result = new AuthDto();
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", GlobalConstant.CLIENT_SECRET);
        clientCredentials.put("grant_type", "password");

        Configuration configuration =
                new Configuration(GlobalConstant.AUTH_SERVER_URL,
                        GlobalConstant.AUTH_SERVER_REALM,
                        GlobalConstant.CLIENT_ID,
                        clientCredentials, null);

        AuthzClient authzClient = AuthzClient.create(configuration);

        AccessTokenResponse response =
                authzClient.obtainAccessToken(userDto.getUsername(), userDto.getPassword());

        Keycloak keycloak = getKeycloakObject();

        result.setAccessTokenResponse(response);
        result.setRoles(getClientRole(userDto.getUsername(),keycloak));
        return result;
    }

    @Override
   public AccessTokenResponse getAccessTokenByRefreshToken(String refreshToken){
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("grant_type", "refresh_token");
        clientCredentials.put("refresh_token", refreshToken);
        clientCredentials.put("client_id", GlobalConstant.CLIENT_ID);
        clientCredentials.put("client_secret", GlobalConstant.CLIENT_SECRET);

        Configuration configuration =
                new Configuration(GlobalConstant.AUTH_SERVER_URL,
                        GlobalConstant.AUTH_SERVER_REALM,
                        GlobalConstant.CLIENT_ID,
                        clientCredentials, null);

        String url = GlobalConstant.AUTH_SERVER_URL + "/realms/" + GlobalConstant.AUTH_SERVER_REALM + "/protocol/openid-connect/token";

        Http http = new Http(configuration, (params, headers) -> {});

        AccessTokenResponse atResponse = http.<AccessTokenResponse>post(url)
                .authentication()
                .client()
                .form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", refreshToken)
                .param("client_id", GlobalConstant.CLIENT_ID)
                .param("client_secret", GlobalConstant.CLIENT_SECRET)
                .response()
                .json(AccessTokenResponse.class)
                .execute();

        return atResponse;
    }

    @Override
    public void logout(String refreshToken){

            MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
            requestParams.add("client_id", GlobalConstant.CLIENT_ID);
            requestParams.add("client_secret", GlobalConstant.CLIENT_SECRET);
            requestParams.add("refresh_token", refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams, headers);

            String url = GlobalConstant.AUTH_SERVER_URL + "/realms/" + GlobalConstant.AUTH_SERVER_REALM + "/protocol/openid-connect/logout";

            restTemplate.postForEntity(url, request, Object.class);
    }

    private List<String> getClientRole(String username, Keycloak keycloak){

        Optional<UserRepresentation> user = keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();
        if (user.isPresent()) {
            UserRepresentation userRepresentation = user.get();
            UserResource userResource = keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).users().get(userRepresentation.getId());
            ClientRepresentation clientRepresentation = keycloak.realm(GlobalConstant.AUTH_SERVER_REALM).clients().findByClientId(GlobalConstant.CLIENT_ID).get(0);
            return userResource.roles().clientLevel(clientRepresentation.getId()).listAll().stream()
                    .map(r -> String.valueOf(r.getName()))
                    .collect(Collectors.toList());
        }
       else {
            return new ArrayList<>();
        }
    }
}


