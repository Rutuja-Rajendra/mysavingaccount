package com.mysavingaccount.app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.entity.SavingPlan;
import com.mysavingaccount.app.entity.Transaction;
import com.mysavingaccount.app.entity.User;
import com.mysavingaccount.app.repository.SavingPlanRepository;
import com.mysavingaccount.app.repository.TransactionRepository;
import com.mysavingaccount.app.repository.UserRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private SavingPlanRepository savingPlanRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Transaction recordTransaction(SavingPlan savingPlan, BigDecimal amount, String status, String gatewayRefId)
	{
		Transaction transaction = new Transaction();
		transaction.setSavingPlan(savingPlan);
		transaction.setAmount(amount);
		transaction.setStatus(status);
		transaction.setGatewayRefId(gatewayRefId);
		transaction.setTransactionDate(LocalDateTime.now());
		
		return transactionRepository.save(transaction);
	}
	
	public List<Transaction> getTransactionHistory(String userEmail) {
	    User user = userRepository.findByEmail(userEmail);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }

	    List<SavingPlan> savingPlans = savingPlanRepository.findByUserId(user.getId());

	    List<Transaction> allTransactions = new ArrayList<>();
	    for (SavingPlan plan : savingPlans) {
	        List<Transaction> transactionsForThisPlan = transactionRepository.findBySavingPlanId(plan.getId());
	        allTransactions.addAll(transactionsForThisPlan);
	    }

	    return allTransactions;
	}

}
