package com.mysavingaccount.app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.entity.SavingPlan;
import com.mysavingaccount.app.repository.SavingPlanRepository;


@Service
public class DueDateSchedulerService {

	@Autowired
	private SavingPlanRepository savingPlanRepository;
	
	@Autowired
	private TransactionService transactionService;
	
	
	@Scheduled(fixedRate = 30000)
	public void processDuePayments() {
		
		List<SavingPlan> duePlans = savingPlanRepository.findByNextDueDateLessThanEqualAndStatus(LocalDate.now(), "ACTIVE");
		
		for(SavingPlan plans : duePlans)
		{
			String gatewayRefId = "TEST_TXN_" + System.currentTimeMillis();
			transactionService.recordTransaction(plans, plans.getAmount(), "SUCCESS", gatewayRefId);			
			plans.setNextDueDate(plans.getNextDueDate().plusMonths(1));
			savingPlanRepository.save(plans);
		}
	}
}
