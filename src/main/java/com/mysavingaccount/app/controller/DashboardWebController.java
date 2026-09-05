package com.mysavingaccount.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysavingaccount.app.entity.SavingPlan;
import com.mysavingaccount.app.entity.User;
import com.mysavingaccount.app.repository.UserRepository;
import com.mysavingaccount.app.service.SavingPlanService;

@Controller
public class DashboardWebController {

    @Autowired
    private SavingPlanService savingPlanService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email);
        model.addAttribute("name", user.getName());

        try {
            SavingPlan plan = savingPlanService.getActivePlan(email);
            model.addAttribute("plan", plan);
        } catch (RuntimeException e) {
            model.addAttribute("plan", null);
        }

        return "dashboard";
    }
}