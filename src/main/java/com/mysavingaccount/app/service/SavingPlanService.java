package com.mysavingaccount.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.dto.SavingPlanResponse;
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
	
	
	public SavingPlanResponse createSavingPlan(String userEmail, BigDecimal amount)
	{
		User user = userRepository.findByEmail(userEmail);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }
	    
	    boolean hasActive = false;
	    
	    List<SavingPlan> savingPlans = savingPlanRepository.findByUserId(user.getId());
	    
	    for(SavingPlan plan : savingPlans)
	    {
	    	if(plan.getStatus().equals("ACTIVE"))
	    	{
	    		hasActive = true;
	    		break;
	    	}
	    }
	    
	    if (hasActive) {
	        throw new RuntimeException("You already have an active saving plan. Modify it instead of creating a new one.");
	    }
	    
		SavingPlan savingPlan = new SavingPlan();
		
		LocalDate nextDueDate = LocalDate.now().plusMonths(1);
		
		savingPlan.setUser(user);
		savingPlan.setAmount(amount);
		savingPlan.setFrequency("MONTHLY");
		savingPlan.setStatus("ACTIVE");
		savingPlan.setNextDueDate(nextDueDate);
		savingPlan.setCreatedAt(LocalDateTime.now());
		
		SavingPlan savedPlan =  savingPlanRepository.save(savingPlan);
		
		return mapToResponse(savedPlan);
	}
	
	private SavingPlanResponse mapToResponse(SavingPlan savingPlan)
	{
		SavingPlanResponse savingPlanResponse = new SavingPlanResponse();
		
		savingPlanResponse.setId(savingPlan.getId());
		savingPlanResponse.setAmount(savingPlan.getAmount());
		savingPlanResponse.setFrequency(savingPlan.getFrequency());
		savingPlanResponse.setStatus(savingPlan.getStatus());
		savingPlanResponse.setNextDueDate(savingPlan.getNextDueDate());
		savingPlanResponse.setMandateId(savingPlan.getMandateId());
		savingPlanResponse.setCreatedAt(savingPlan.getCreatedAt());
		
		return savingPlanResponse;
	}
	
	public String modifySavingPlan(String email, BigDecimal amount)
	{
		
		User user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }
	    
	    
	    List<SavingPlan> savingPlans = savingPlanRepository.findByUserId(user.getId());
	    
	    SavingPlan activePlan = null;
	    for (SavingPlan plan : savingPlans) {
	        if (plan.getStatus().equals("ACTIVE")) {
	            activePlan = plan;
	            break;
	        }
	    }
	    
	    if (activePlan == null) {
	        throw new RuntimeException("No active saving plan found to modify");
	    }
	    
	    activePlan.setAmount(amount);
	 // planChangeLog logic here later
	    
	    SavingPlan modifiedPlan =  savingPlanRepository.save(activePlan);
	    return "Saving plan has modified successfully";
	}
	
	public String stopSavingPlan(String email)
	{
		User user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }
	    
	    List<SavingPlan> savingPlans = savingPlanRepository.findByUserId(user.getId());

	    SavingPlan activePlan = null;
	    for (SavingPlan plan : savingPlans) {
	        if (plan.getStatus().equals("ACTIVE")) {
	            activePlan = plan;
	            break;
	        }
	    }

	    if (activePlan == null) {
	        throw new RuntimeException("No active saving plan found to stop");
	    }
	    
	    activePlan.setStatus("PAUSED");   
	    savingPlanRepository.save(activePlan);

	    return "Saving plan has been paused";
	}
	
	
}
