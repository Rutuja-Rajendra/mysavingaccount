package com.mysavingaccount.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysavingaccount.app.entity.SavingPlan;


@Repository
public interface SavingPlanRepository extends JpaRepository<SavingPlan, Long>{

	List<SavingPlan> findByUserId(Long userId);
	
	List<SavingPlan> findByNextDueDateLessThanEqualAndStatus(LocalDate date, String status);
}
