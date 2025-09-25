package com.bank.controller;

import com.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.model.Transaction;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions/transfer")
    public ResponseEntity<String> transferMoney(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam BigDecimal amount) {
        try {
            transactionService.transferMoney(senderId, recipientId, amount);
            return new ResponseEntity<>("Транзакция успешно обработана!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<String> putMoney(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        try {
            transactionService.putMoney(accountId, amount);
            return new ResponseEntity<>("Пополнение успешно выполнено!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<String> takeMoney(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        try {
            transactionService.takeMoney(accountId, amount);
            return new ResponseEntity<>("Снятие успешно выполнено!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/account/balance")
    public ResponseEntity<?> getBalance(@RequestParam Long accountId) {
        try {
            BigDecimal balance = transactionService.getBalance(accountId);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/operations")
    public ResponseEntity<List<Transaction>> getOperationList(
            @RequestParam Long accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Transaction> operations = transactionService.getOperationList(accountId, startDate, endDate);
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }
}