package org.example.email_entity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.email_entity.entity.AccountStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {

    private String fullName;
    private String email;
    private AccountStatus status;
    private Long initBalance;


}
