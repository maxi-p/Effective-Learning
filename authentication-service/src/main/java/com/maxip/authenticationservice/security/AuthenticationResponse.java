package com.maxip.authenticationservice.security;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse
{
    private String token;
}
