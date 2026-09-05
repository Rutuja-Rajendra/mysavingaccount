package com.mysavingaccount.app.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysavingaccount.app.dto.SavingPlanResponse;
import com.mysavingaccount.app.service.SavingPlanService;

@Controller
public class SavingPlanWebController {

    @Autowired
    private SavingPlanService savingPlanService;

    @GetMapping("/start-saving")
    public String showStartForm() {
        return "start-saving";
    }

    @PostMapping("/start-saving")
    public String startSavingPlan(@RequestParam BigDecimal amount, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        SavingPlanResponse response = savingPlanService.createSavingPlan(email, amount);

        model.addAttribute("shortUrl", response.getRazorpayShortUrl());
        return "start-saving";
    }
}