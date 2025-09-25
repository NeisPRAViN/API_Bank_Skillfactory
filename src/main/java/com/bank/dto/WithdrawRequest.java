package com.bank.dto;

import java.math.BigDecimal;

public class WithdrawRequest {
    private Long accountId;
    private BigDecimal amount;

    // Геттеры и сеттеры

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}