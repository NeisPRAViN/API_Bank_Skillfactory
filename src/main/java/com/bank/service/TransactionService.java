package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transferMoney(Long senderId, Long recipientId, BigDecimal amount) {
        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Отправитель не найден"));

        Account recipient = accountRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Получатель не найден"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете отправителя");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(recipient);

        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transaction.setType("перевод");
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Transactional
    public void putMoney(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setRecipientId(accountId);
        transaction.setAmount(amount);
        transaction.setType("пополнение");
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Transactional
    public void takeMoney(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setSenderId(accountId);
        transaction.setAmount(amount);
        transaction.setType("снятие со счета");
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
        return account.getBalance();
    }

    public List<Transaction> getOperationList(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findBySenderIdOrRecipientIdAndTimestampBetween(accountId, accountId, startDate, endDate);
        } else {
            return transactionRepository.findBySenderIdOrRecipientId(accountId, accountId);
        }
    }
}