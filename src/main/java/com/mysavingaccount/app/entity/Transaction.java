package com.mysavingaccount.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "saving_plan_id")
	private SavingPlan savingPlan;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	private String status;
	private String gatewayRefId;
	private LocalDateTime transactionDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public SavingPlan getSavingPlan() {
		return savingPlan;
	}
	public void setSavingPlan(SavingPlan savingPlan) {
		this.savingPlan = savingPlan;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGatewayRefId() {
		return gatewayRefId;
	}
	public void setGatewayRefId(String gatewayRefId) {
		this.gatewayRefId = gatewayRefId;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	
	

}
