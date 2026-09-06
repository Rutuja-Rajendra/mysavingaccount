package com.mysavingaccount.app.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to MySavingAccount!");
        message.setText("Hi " + name + ",\n\nYour account has been created successfully. "
                + "Start your first saving plan today and build your financial discipline!\n\n"
                + "- MySavingAccount Team");

        mailSender.send(message);
    }
    
    public void sendPlanCreatedEmail(String toEmail, String name, BigDecimal amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Saving Plan Has Been Created");
        message.setText("Hi " + name + ",\n\nYour saving plan of ₹" + amount + " per month has been created. "
                + "Please complete the authorization to activate it.\n\n"
                + "- MySavingAccount Team");
        mailSender.send(message);
    }
    
    public void sendPlanModifiedEmail(String toEmail, String name, BigDecimal newAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Saving Plan Has Been Updated");
        message.setText("Hi " + name + ",\n\nYour saving plan amount has been updated to ₹" + newAmount + " per month.\n\n"
                + "- MySavingAccount Team");
        mailSender.send(message);
    }

    public void sendPlanStoppedEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Saving Plan Has Been Stopped");
        message.setText("Hi " + name + ",\n\nYour saving plan has been paused/stopped as requested. "
                + "You can start a new one anytime from your dashboard.\n\n"
                + "- MySavingAccount Team");
        mailSender.send(message);
    }
}