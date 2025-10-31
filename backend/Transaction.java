package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Transaction {
    private Long transactionId;
    private Long accountId;
    private BigDecimal transactionAmount;
    // --- UPDATED --- Removed transactionType
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionTime;
    private String transactionMode;
    private String receiverDetails;
    private String senderDetails;
    private String description;

    // Getters and Setters (with transactionType getter/setter removed)
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getReceiverDetails() {
        return receiverDetails;
    }

    public void setReceiverDetails(String receiverDetails) {
        this.receiverDetails = receiverDetails;
    }

    public String getSenderDetails() {
        return senderDetails;
    }

    public void setSenderDetails(String senderDetails) {
        this.senderDetails = senderDetails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
