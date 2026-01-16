package org.example.email_entity.controller;


import lombok.AllArgsConstructor;
import org.example.email_entity.dto.BankResponse;
import org.example.email_entity.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BusinessLogicController {

    @Autowired
    private final UserServiceImpl userService;

    @PatchMapping("/users/user/deposit/{amount}")
    public ResponseEntity<BankResponse> depositMoney(@PathVariable Long amount){
        return new ResponseEntity<>(userService.deposit(amount), HttpStatus.OK);

    }

    @PatchMapping("/users/user/withdraw/{id}/{amount}")
    public ResponseEntity<BankResponse> withdrawMoney(@PathVariable Long amount){
        return new ResponseEntity<>(userService.withdraw(amount), HttpStatus.OK);
    }

    @PatchMapping("/users/user/transfer/{email2}/{amount}")
    public ResponseEntity<String> transfer(@PathVariable String email2, @PathVariable Long amount){
        return new ResponseEntity<>(userService.transfer(email2,amount), HttpStatus.OK);
    }



}
