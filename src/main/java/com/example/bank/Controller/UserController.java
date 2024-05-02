package com.example.bank.Controller;

import com.example.bank.Config.Response;
import com.example.bank.Dto.*;
import com.example.bank.Entity.Role;
import com.example.bank.Entity.User;
import com.example.bank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Response<User> register(@RequestBody SigninDto signinDto){
        User user = User.builder()
                .name(signinDto.getName())
                .email(signinDto.getEmail())
                .password(signinDto.getPassword())
                .cash(0.0)
                .birthdate(signinDto.getBirthdate())
                .created_at(LocalDate.now())
                .is_active(true)
                .role(Role.USER)
                .accountNumber(userService.generateAccountNumber())
                .build();

        return userService.register(user);
    }

    @PostMapping("/login")
    public Response<User> login(@RequestBody LoginDto loginDto){
        User user = User.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .build();

        return userService.login(user);
    }

    @PostMapping("/receiver")
    public Response<User> receiver(@RequestBody ReceiverDto receiverDto){
        return userService.receiver(receiverDto.getAccountNumber());
    }
    @PostMapping("/deposit")
    public Response<User> deposit(@RequestBody DepositDto depositDto){
        return userService.deposit(depositDto.getId(), depositDto.getCash());
    }

    @PostMapping("/withdraw")
    public Response<User> withdraw(@RequestBody WithdrawDto withdrawDto){
        return userService.withdraw(withdrawDto.getId(), withdrawDto.getCash());
    }

    @PostMapping("/transfer")
    public Response<User> transfer(@RequestBody TransferDto transferDto){
        return userService.transfer(transferDto.getUserId(), transferDto.getReceiverId(),transferDto.getCash());
    }

}
