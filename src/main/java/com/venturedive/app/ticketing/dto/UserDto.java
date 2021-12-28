package com.venturedive.app.ticketing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String username;
    private String firstname;
    private String lastname;
    private int statusCode;
    private String status;

    @Override
    public String toString() {
        return "email = " + email + ", password = " + password +
                ", username = " + username + ", firstname = " + firstname
                + ", lastname = " + lastname;
    }
}