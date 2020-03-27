package com.ticketfinder.domain.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String email;
    private String hashPassword;
}
