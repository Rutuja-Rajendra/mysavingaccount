package com.mysavingaccount.app.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysavingaccount.app.dto.SavingPlanResponse;
import com.mysavingaccount.app.service.SavingPlanService;

@RestController
@RequestMapping("/api/savings")
public class SavingPlanController {
	
	@Autowired
	private SavingPlanService savingPlanService;
	

	@PostMapping("/start")
	public SavingPlanResponse createSavingPlan(@RequestBody BigDecimal amount)
	{
		String email = SecurityContextHolder.getContext().getAuthentication().getName();	
		
		return savingPlanService.createSavingPlan(email, amount);
	}
	
	@PutMapping("/modify")
	public String modifySavingPlan(@RequestBody BigDecimal amount)
	{
		String email = SecurityContextHolder.getContext().getAuthentication().getName();	
		return savingPlanService.modifySavingPlan(email, amount);
	}
	
	@PutMapping("/stop")
	public String stopSavingPlan()
	{
		String email = SecurityContextHolder.getContext().getAuthentication().getName();	
		return savingPlanService.stopSavingPlan(email);
	}
}
