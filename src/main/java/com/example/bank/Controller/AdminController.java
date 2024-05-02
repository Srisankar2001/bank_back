package com.example.bank.Controller;

import com.example.bank.Config.Response;
import com.example.bank.Dto.AdminDto;
import com.example.bank.Dto.AdminSignupDto;
import com.example.bank.Entity.Role;
import com.example.bank.Entity.User;
import com.example.bank.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public Response<User> register(@RequestBody AdminSignupDto adminSignupDto){
        User admin = User.builder()
                .name(adminSignupDto.getName())
                .email(adminSignupDto.getEmail())
                .password(adminSignupDto.getPassword())
                .birthdate(adminSignupDto.getBirthdate())
                .cash(null)
                .role(Role.ADMIN)
                .accountNumber(null)
                .is_active(true)
                .created_at(LocalDate.now())
                .build();
        return adminService.register(admin);
    }

    @PostMapping("/getAllUsers")
    public Response<List<User>> getAllUsers(@RequestBody AdminDto adminDto){
        return adminService.getAllUsers(adminDto.getAdminId());
    }

    @PostMapping("/block")
    public Response<User> blockUser(@RequestBody AdminDto adminDto){
        return adminService.blockUser(adminDto.getAdminId(),adminDto.getUserId());
    }

    @PostMapping("/unblock")
    public Response<User> unblockUser(@RequestBody AdminDto adminDto){
        return adminService.unblockUser(adminDto.getAdminId(),adminDto.getUserId());
    }
}
