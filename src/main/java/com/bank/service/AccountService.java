package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getBalance(Long id) {
        return accountRepository.findById(id)
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Счет с id " + id + " не найден"));
    }

    @Transactional
    public void deposit(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Счет с id " + id + " не найден"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setRecipientId(id);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Счет с id " + id + " не найден"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("На счете недостаточно средств");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setSenderId(id);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAWAL");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void transferMoney(Long senderId, Long recipientId, BigDecimal amount) {
        Account senderAccount = accountRepository.findById(senderId)
                .orElseThrow(() -> new AccountNotFoundException("Счет отправителя с id " + senderId + " не найден"));
        Account recipientAccount = accountRepository.findById(recipientId)
                .orElseThrow(() -> new AccountNotFoundException("Счет получателя с id " + recipientId + " не найден"));

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("На счете отправителя недостаточно средств для перевода");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transaction.setType("TRANSFER");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}