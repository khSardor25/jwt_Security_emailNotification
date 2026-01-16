package org.example.email_entity.service;


import lombok.AllArgsConstructor;
import org.example.email_entity.dto.BankResponse;
import org.example.email_entity.dto.EmailBody;
import org.example.email_entity.dto.UserRequest;
import org.example.email_entity.entity.Account;
import org.example.email_entity.entity.AccountStatus;
import org.example.email_entity.entity.User;
import org.example.email_entity.repository.AccountRepository;
import org.example.email_entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public String saveNewUser(UserRequest user){


        Account acc = Account.builder()
                .balance(0L)
                .status(AccountStatus.ACTIVE)
                .build();

        String rawPassword = user.getPassword();

        User newUser = User.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .password(passwordEncoder.encode(rawPassword))
                .created(LocalDateTime.now())
                .account(acc)
                .build();


     BankResponse response = BankResponse.builder()
              .fullName(newUser.getFullName())
              .email(newUser.getEmail())
               .status(AccountStatus.ACTIVE)
               .initBalance(acc.getBalance())
                .build();


     userRepository.save(newUser);


     return "Successfully created";

    }

    //Increment amount
    public BankResponse deposit(Long amount){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (userRepository.existsByEmail(email)){
            Account acc = userRepository.findByEmail(email).get().getAccount();
            acc.setBalance(acc.getBalance() + amount);

            accountRepository.save(acc);

            BankResponse response = BankResponse.builder()
                .fullName(userRepository.findByEmail(email).get().getFullName())
                .email(userRepository.findByEmail(email).get().getEmail())
                .status(AccountStatus.ACTIVE)
                .initBalance(acc.getBalance())
                .build();

            return response;
        }
        else{
            return new BankResponse();
        }


    }

    public BankResponse withdraw(Long amount){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (userRepository.existsByEmail(email)){
            Account acc = userRepository.findByEmail(email).get().getAccount();
            if (acc.getBalance() - amount < 0){
                BankResponse response = BankResponse.builder()
                        .fullName(userRepository.findByEmail(email).get().getFullName())
                        .email(userRepository.findByEmail(email).get().getEmail())
                        .status(AccountStatus.BLOCKED)
                        .initBalance(acc.getBalance())
                        .build();

                return response;
            }

            acc.setBalance(acc.getBalance() - amount);

            accountRepository.save(acc);

            BankResponse response = BankResponse.builder()
                    .fullName(userRepository.findByEmail(email).get().getFullName())
                    .email(userRepository.findByEmail(email).get().getEmail())
                    .status(AccountStatus.ACTIVE)
                    .initBalance(acc.getBalance())
                    .build();

            return response;
        }
        else{
            return new BankResponse();
        }






    }

    public String transfer(String toEmail, Long amount) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account fromAccount = userRepository.findByEmail(email).get().getAccount();
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }


        User toUser = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account toAccount = toUser.getAccount();

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return "Successfully transfered " + amount + " to " + toUser.getEmail();


    }


}
