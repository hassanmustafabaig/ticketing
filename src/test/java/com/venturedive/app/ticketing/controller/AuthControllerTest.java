package com.venturedive.app.ticketing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.service.KeycloakService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ComponentScan(basePackageClasses = { KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class })
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeycloakService mockKeycloakService;

    @Test
    void shouldReturnAccessTokenWith200Status() throws Exception {

        when(mockKeycloakService.getAccessToken(getUserInfo())).thenReturn(getAuthInfo());

        ResultActions result
                = mockMvc.perform(post(GlobalConstant.API_PREFIX_AUTH + GlobalConstant.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidContentForAccessToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotReturnAccessTokenWith401Status() throws Exception {

        when(mockKeycloakService.getAccessToken(getUserInfo())).thenReturn(getAuthInfo());

        ResultActions result
                = mockMvc.perform(post(GlobalConstant.API_PREFIX_AUTH + GlobalConstant.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getInValidContentForAccessToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAccessTokenByRefreshTokenWith200Status() throws Exception {

        when(mockKeycloakService.getAccessTokenByRefreshToken(getAuthInfoForRefreshToken().getRefreshToken())).thenReturn(getAuthInfoForRefreshToken().getAccessTokenResponse());

        ResultActions result
                = mockMvc.perform(post(GlobalConstant.API_PREFIX_AUTH + GlobalConstant.API_AUTH_GET_REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidContentForAccessTokenByRefreshToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotReturnAccessTokenByRefreshTokenWith400Status() throws Exception {

        when(mockKeycloakService.getAccessTokenByRefreshToken(getAuthInfoForRefreshToken().getRefreshToken())).thenReturn(getAuthInfoForRefreshToken().getAccessTokenResponse());

        ResultActions result
                = mockMvc.perform(post(GlobalConstant.API_PREFIX_AUTH + GlobalConstant.API_AUTH_GET_REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidContentForAccessTokenByRefreshToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    private UserDto getUserInfo(){
        UserDto usetDto = UserDto.builder()
                .username("order.manager@ticketing.com")
                .email("order.manager@ticketing.com")
                .password("12345")
                .firstname("first name")
                .lastname("first name").build();
    return  usetDto;
    }

    private AuthDto getAuthInfo(){
        AuthDto authDto = new AuthDto();

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setScope("profile email");
        accessTokenResponse.setToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBQ25kZDlLUFFPZ0l0cDFsYnItYWxnSE5OdFRtSXVlUjRveDEtWm1TTFpZIn0.eyJleHAiOjE2Mzk5NjExMjQsImlhdCI6MTYzOTk1NzUyNCwianRpIjoiNmUwOTA4OTctMDkwYy00YTM0LWIyZDItMjY3NTRiZmY3NzI3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2xvY2FsLW1hc3Rlci1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzNTUxYTMwOS0zZjQ1LTRmZmItYjUxNi01NGExNDRiMGI5MWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYmUtYXBwbGljYXRpb24iLCJzZXNzaW9uX3N0YXRlIjoiYjYzNTAwMDItZGIwYy00MDJmLWI1YjEtNjAyN2E1NjQ2ZTBjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODUiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbG9jYWwtbWFzdGVyLXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJPcmRlcl9NYW5hZ2VyIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYmUtYXBwbGljYXRpb24iOnsicm9sZXMiOlsiVGlja2V0aW5nX0FnZW50Il19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJiNjM1MDAwMi1kYjBjLTQwMmYtYjViMS02MDI3YTU2NDZlMGMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJmaXJzdCBuYW1lIGxhc3QgbmFtZSIsInByZWZlcnJlZF91c2VybmFtZSI6InRpY2tldGluZy5hZ2VudEB0aWNrZXRpbmcuY29tIiwiZ2l2ZW5fbmFtZSI6ImZpcnN0IG5hbWUiLCJmYW1pbHlfbmFtZSI6Imxhc3QgbmFtZSIsImVtYWlsIjoidGlja2V0aW5nLmFnZW50QHRpY2tldGluZy5jb20ifQ.TN77hrPSVgEp_1jDJ77VgAfom1IN2HBdvC44Cp1uCjD76xvYvBiJwtufM3ApMhG5rn3EUDUIGq_qPW9_oOuZM_mG8GmUcJc_vNOclJzsT0KOE3TuyHlbu_TcYtNk_03cXdZb4jxd74PMGsmn9JZrLR7AiUUaQrcaiconBoyNEMEmSQmPGaL8Gco0VI6YEU6i-9MNxP5kfjqkGkzFtG_jMSxT0sDB6Rh6iK9m9dnnhU2lRkcx6mbPEwcCbPBvx9OEMhPTtsBC_BB4pPb9JbO3WwEv0VGVHYV6L-GqCvrUl0rSkh4TyLzCXYke1rwaz-si7BBVafeVfECyq-emMG02Pw");
        accessTokenResponse.setExpiresIn(3600);
        accessTokenResponse.setRefreshExpiresIn(1800);
        accessTokenResponse.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwYTY5MWE2Yi0zYjQzLTQxZGQtOGVlZS1lNTkzYjQ2ODY3ZDAifQ.eyJleHAiOjE2Mzk5NTkzMjQsImlhdCI6MTYzOTk1NzUyNCwianRpIjoiOGYzYmFmYTctMjFmMS00M2VkLTkzOGYtNjM1MGEzNmVkYTc2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2xvY2FsLW1hc3Rlci1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hdXRoL3JlYWxtcy9sb2NhbC1tYXN0ZXItcmVhbG0iLCJzdWIiOiIzNTUxYTMwOS0zZjQ1LTRmZmItYjUxNi01NGExNDRiMGI5MWIiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoidGlja2V0aW5nLWJlLWFwcGxpY2F0aW9uIiwic2Vzc2lvbl9zdGF0ZSI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyIsInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyJ9.N68tUJxrVGjGNsOWNI_dIg_PxlHtt3sDDekYKtFe1BU");
        accessTokenResponse.setTokenType("Bearer");
        accessTokenResponse.setIdToken("null");
        accessTokenResponse.setNotBeforePolicy(1639916936);
        accessTokenResponse.setSessionState("b6350002-db0c-402f-b5b1-6027a5646e0c");
        accessTokenResponse.setError("null");
        authDto.setAccessTokenResponse(accessTokenResponse);
        authDto.setRoles(List.of("Ticketing_Agent"));
        authDto.setRefreshToken("null");

        return authDto;
    }

    private String getValidContentForAccessToken() throws JsonProcessingException {
        UserDto usetDto = UserDto.builder()
                .username("order.manager@ticketing.com")
                .email("order.manager@ticketing.com")
                .password("12345")
                .firstname("first name")
                .lastname("first name").build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(usetDto);
        return requestJson;
    }

    private String getInValidContentForAccessToken() throws JsonProcessingException {
        UserDto usetDto = UserDto.builder()
                .username("invalid")
                .email("invalid")
                .password("invalid")
                .firstname("first name")
                .lastname("first name").build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(usetDto);
        return requestJson;
    }

    private AuthDto getAuthInfoForRefreshToken(){
        AuthDto authDto = AuthDto.builder()
                .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwYTY5MWE2Yi0zYjQzLTQxZGQtOGVlZS1lNTkzYjQ2ODY3ZDAifQ.eyJleHAiOjE2Mzk5NTkzMjQsImlhdCI6MTYzOTk1NzUyNCwianRpIjoiOGYzYmFmYTctMjFmMS00M2VkLTkzOGYtNjM1MGEzNmVkYTc2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2xvY2FsLW1hc3Rlci1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hdXRoL3JlYWxtcy9sb2NhbC1tYXN0ZXItcmVhbG0iLCJzdWIiOiIzNTUxYTMwOS0zZjQ1LTRmZmItYjUxNi01NGExNDRiMGI5MWIiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoidGlja2V0aW5nLWJlLWFwcGxpY2F0aW9uIiwic2Vzc2lvbl9zdGF0ZSI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyIsInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyJ9.N68tUJxrVGjGNsOWNI_dIg_PxlHtt3sDDekYKtFe1BU")
                .build();
        return authDto;
    }

    private String getValidContentForAccessTokenByRefreshToken() throws JsonProcessingException {
        AuthDto authDto = AuthDto.builder()
                .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwYTY5MWE2Yi0zYjQzLTQxZGQtOGVlZS1lNTkzYjQ2ODY3ZDAifQ.eyJleHAiOjE2Mzk5NTkzMjQsImlhdCI6MTYzOTk1NzUyNCwianRpIjoiOGYzYmFmYTctMjFmMS00M2VkLTkzOGYtNjM1MGEzNmVkYTc2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2xvY2FsLW1hc3Rlci1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hdXRoL3JlYWxtcy9sb2NhbC1tYXN0ZXItcmVhbG0iLCJzdWIiOiIzNTUxYTMwOS0zZjQ1LTRmZmItYjUxNi01NGExNDRiMGI5MWIiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoidGlja2V0aW5nLWJlLWFwcGxpY2F0aW9uIiwic2Vzc2lvbl9zdGF0ZSI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyIsInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImI2MzUwMDAyLWRiMGMtNDAyZi1iNWIxLTYwMjdhNTY0NmUwYyJ9.N68tUJxrVGjGNsOWNI_dIg_PxlHtt3sDDekYKtFe1BU")
                .build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(authDto);
        return requestJson;
    }

    private String getInValidContentForAccessTokenByRefreshToken() throws JsonProcessingException {
        AuthDto authDto = AuthDto.builder()
                .refreshToken("invalid")
                .build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(authDto);
        return requestJson;
    }

}
