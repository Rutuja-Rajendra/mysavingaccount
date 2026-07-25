package com.mysavingaccount.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.entity.SavingPlan;
import com.mysavingaccount.app.entity.User;
import com.mysavingaccount.app.repository.SavingPlanRepository;
import com.mysavingaccount.app.repository.UserRepository;

@Service
public class SavingPlanService {

	
	@Autowired
	private SavingPlanRepository savingPlanRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public SavingPlan createSavingPlan(String userEmail, BigDecimal amount)
	{
		User user = userRepository.findByEmail(userEmail);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }
	    
		SavingPlan savingPlan = new SavingPlan();
		
		LocalDate nextDueDate = LocalDate.now().plusMonths(1);
		
		savingPlan.setUser(user);
		savingPlan.setAmount(amount);
		savingPlan.setFrequency("MONTHLY");
		savingPlan.setStatus("ACTIVE");
		savingPlan.setNextDueDate(nextDueDate);
		savingPlan.setCreatedAt(LocalDateTime.now());
		
		return savingPlanRepository.save(savingPlan);
	}
}
