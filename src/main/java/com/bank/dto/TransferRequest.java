package com.bank.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private Long senderId;
    private Long recipientId;
    private BigDecimal amount;

    // Геттеры и сеттеры

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}