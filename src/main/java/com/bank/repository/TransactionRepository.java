package com.bank.repository;

import com.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderIdOrRecipientIdAndTimestampBetween(Long senderId, Long recipientId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findBySenderIdOrRecipientId(Long senderId, Long recipientId);
}