package com.example.bank.Service;

import com.example.bank.Config.Response;
import com.example.bank.Entity.Role;
import com.example.bank.Entity.User;
import com.example.bank.Repository.TransactionRepository;
import com.example.bank.Repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public Response<User> register(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("Admin already exist")
                    .build();
        }else{
            User savedUser = userRepository.save(user);
            return  Response.<User>builder()
                    .status(true)
                    .data(savedUser)
                    .error(null)
                    .build();
        }
    }
    public boolean isExist(Integer id){
        Optional<User> admin = userRepository.findById(id);
        if(admin.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isAdmin(Integer id){
        Optional<User> admin = userRepository.findById(id);
        if(admin.isPresent()){
            if(admin.get().getRole() == Role.ADMIN){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    public Response<List<User>> getAllUsers(Integer id){
        if(isExist(id)){
            if(isAdmin(id)){
                List<User> user = userRepository.findAllByRole(Role.USER);
                return Response.<List<User>>builder()
                        .status(true)
                        .data(user)
                        .error(null)
                        .build();
            }else{
                return Response.<List<User>>builder()
                        .status(false)
                        .data(null)
                        .error("This is not admin")
                        .build();
            }
        }else{
            return Response.<List<User>>builder()
                    .status(false)
                    .data(null)
                    .error("Admin not exist")
                    .build();
        }
    }

    public Response<User> blockUser(Integer adminId,Integer userId){
        if(isExist(adminId)){
            if(isAdmin(adminId)){
                if(isExist(userId)){
                    if(!isAdmin(userId)){
                        Optional<User> user = userRepository.findById(userId);
                        user.get().setIs_active(false);
                        User savedUser = userRepository.save(user.get());
                        return Response.<User>builder()
                                .status(true)
                                .data(savedUser)
                                .error(null)
                                .build();
                    }else{
                        return Response.<User>builder()
                                .status(false)
                                .data(null)
                                .error("Admin can't be blocked")
                                .build();
                    }
                }else{
                    return Response.<User>builder()
                            .status(false)
                            .data(null)
                            .error("User not found")
                            .build();
                }
            }else{
                return Response.<User>builder()
                        .status(false)
                        .data(null)
                        .error("This is not admin")
                        .build();
            }
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("Admin not exist")
                    .build();
        }
    }

    public Response<User> unblockUser(Integer adminId,Integer userId){
        if(isExist(adminId)){
            if(isAdmin(adminId)){
                if(isExist(userId)){
                        Optional<User> user = userRepository.findById(userId);
                        user.get().setIs_active(true);
                        User savedUser = userRepository.save(user.get());
                        return Response.<User>builder()
                                .status(true)
                                .data(savedUser)
                                .error(null)
                                .build();
                }else{
                    return Response.<User>builder()
                            .status(false)
                            .data(null)
                            .error("User not found")
                            .build();
                }
            }else{
                return Response.<User>builder()
                        .status(false)
                        .data(null)
                        .error("This is not admin")
                        .build();
            }
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("Admin not exist")
                    .build();
        }
    }
}
