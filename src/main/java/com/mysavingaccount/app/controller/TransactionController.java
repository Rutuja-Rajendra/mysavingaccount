package com.mysavingaccount.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysavingaccount.app.entity.Transaction;
import com.mysavingaccount.app.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	
	@GetMapping("/history")
	public List<Transaction> getTransactionHistory() {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return transactionService.getTransactionHistory(email);
	}
}
