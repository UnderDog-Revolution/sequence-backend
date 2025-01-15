package com.seq.deleteAccount.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String username;
    private String password;
    private String confirm_password;
}