package com.example.bank.Service;

import com.example.bank.Config.Response;
import com.example.bank.Entity.User;
import com.example.bank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionService transactionService;

    public Response<User> register(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("User already exist")
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

    public Response<User> login(User user){
        Optional<User> loginUser = userRepository.findByEmail(user.getEmail());
        if(loginUser.isPresent()){
            loginUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if(loginUser.isPresent()){
                if(loginUser.get().getIs_active()){
                    return Response.<User>builder()
                            .status(true)
                            .data(loginUser.get())
                            .error(null)
                            .build();
                }else{
                    return Response.<User>builder()
                            .status(false)
                            .data(null)
                            .error("You're Account is Locked")
                            .build();
                }
            }else{
                return Response.<User>builder()
                        .status(false)
                        .data(null)
                        .error("Password is Wrong")
                        .build();
            }
        }
        else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("Account Not Exist")
                    .build();
        }
    }

    public Response<User> deposit(Integer id,Double money){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
           if(user.get().getIs_active()){
               user.get().setCash(user.get().getCash()+money);
               User savedUser = userRepository.save(user.get());
               transactionService.deposit(id,money,savedUser.getCash());
               return Response.<User>builder()
                       .status(true)
                       .data(savedUser)
                       .error(null)
                       .build();
           }else{
               return Response.<User>builder()
                       .status(false)
                       .data(null)
                       .error("User is blocked")
                       .build();
           }
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("User not found")
                    .build();
        }

    }

    public Response<User> withdraw(Integer id,Double money){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            if(user.get().getIs_active()){
                if(user.get().getCash() >= money){
                    user.get().setCash(user.get().getCash() - money);
                    User savedUser = userRepository.save(user.get());
                    transactionService.withdraw(id,money,savedUser.getCash());
                    return Response.<User>builder()
                            .status(true)
                            .data(savedUser)
                            .error(null)
                            .build();
                }else{
                    return Response.<User>builder()
                            .status(false)
                            .data(null)
                            .error("Account balance is Low")
                            .build();
                }
            }else{
                return Response.<User>builder()
                        .status(false)
                        .data(null)
                        .error("User is blocked")
                        .build();
            }
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("User not found")
                    .build();
        }

    }

    public Response<User> transfer(Integer userId,Integer receiverId,Double money){
        Optional<User> user = userRepository.findById(userId);
        Optional<User> receiver = userRepository.findById(receiverId);
        if(user.isPresent()){
            if(user.get().getIs_active()){
                if(receiver.isPresent()){
                    if(receiver.get().getIs_active()){
                        user.get().setCash(user.get().getCash() - money);
                        User savedUser = userRepository.save(user.get());
                        receiver.get().setCash(receiver.get().getCash() + money);
                        userRepository.save(receiver.get());
                        transactionService.transfer(userId,receiverId,money,savedUser.getCash());
                        return Response.<User>builder()
                                .status(true)
                                .data(savedUser)
                                .error(null)
                                .build();
                    }else{
                        return Response.<User>builder()
                                .status(false)
                                .data(null)
                                .error("Receiver Account is Blocked")
                                .build();
                    }
                }else{
                    return Response.<User>builder()
                            .status(false)
                            .data(null)
                            .error("Receiver not found")
                            .build();
                }
            }else{
                return Response.<User>builder()
                        .status(false)
                        .data(null)
                        .error("User is blocked")
                        .build();
            }
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("User not found")
                    .build();
        }
    }


    public Response<User> receiver(String accountNumber){
        Optional<User> user = userRepository.findByAccountNumber(accountNumber);
        if(user.isPresent()){
            return Response.<User>builder()
                    .status(true)
                    .data(user.get())
                    .error(null)
                    .build();
        }else{
            return Response.<User>builder()
                    .status(false)
                    .data(null)
                    .error("Receiver Account is Not Exist")
                    .build();
        }
    }

    public String generateAccountNumber(){
        StringBuilder accountNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char randomUppercaseLetter = (char) (random.nextInt(26) + 'A');
            accountNumber.append(randomUppercaseLetter);
        }
        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10);
            accountNumber.append(randomNumber);
        }

        Optional<User> user = userRepository.findByAccountNumber(String.valueOf(accountNumber));
        if(user.isPresent()){
            return generateAccountNumber();
        }else{
            return String.valueOf(accountNumber);
        }
    }
}
