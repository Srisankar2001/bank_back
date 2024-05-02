package com.example.bank.Service;

import com.example.bank.Entity.Transaction;
import com.example.bank.Entity.Type;
import com.example.bank.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public void withdraw(Integer userId, Double amount, Double total){
        Transaction transaction = Transaction.builder()
                .userId(userId)
                .receiverId(null)
                .type(Type.WITHDRAW)
                .amount(amount)
                .total(total)
                .localDate(LocalDate.now())
                .localTime(LocalTime.now())
                .build();
        transactionRepository.save(transaction);
    }
    public void deposit(Integer userId, Double amount, Double total){
        Transaction transaction = Transaction.builder()
                .userId(userId)
                .receiverId(null)
                .type(Type.DEPOSIT)
                .amount(amount)
                .total(total)
                .localDate(LocalDate.now())
                .localTime(LocalTime.now())
                .build();
        transactionRepository.save(transaction);
    }


    public void transfer(Integer userId,Integer receiverId, Double amount, Double total){
        Transaction transaction = Transaction.builder()
                .userId(userId)
                .receiverId(receiverId)
                .type(Type.TRANSFER)
                .amount(amount)
                .total(total)
                .localDate(LocalDate.now())
                .localTime(LocalTime.now())
                .build();
        transactionRepository.save(transaction);
    }
}
