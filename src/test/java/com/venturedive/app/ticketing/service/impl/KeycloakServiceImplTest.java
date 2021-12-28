package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.UserDto;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class KeycloakServiceImplTest {

    private KeycloakServiceImpl keycloakServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        keycloakServiceImplUnderTest = new KeycloakServiceImpl();
    }

    @Test
    void shouldReturnAccessToken() {
        UserDto userDto = new UserDto("ticketing.agent@ticketing.com", "12345", "ticketing.agent@ticketing.com", "first name", "last name", 0, "status");
        AuthDto result = keycloakServiceImplUnderTest.getAccessToken(userDto);
        assertThat(result.getAccessTokenResponse().getToken()).isNotEmpty();
    }

    /*
    @Test
    void shouldReturnAccessTokenByRefreshToken() {
        AuthDto response = getAuthInfo();
        AuthDto expected = getAuthInfo();
        when(keycloakServiceImplUnderTest.getAccessTokenByRefreshToken(response.getAccessTokenResponse().getToken())).thenReturn(response.getAccessTokenResponse());
        AuthDto result = getAuthInfo();keycloakServiceImplUnderTest.getAccessTokenByRefreshToken(response.getAccessTokenResponse().getToken());
        assertThat(result.getAccessTokenResponse().getToken()).isEqualTo(expected.getAccessTokenResponse().getToken());
    }*/


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
}
