package com.maxip.authenticationservice.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserJwt
{
    String username;
    String id;
}
