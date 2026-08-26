package com.mysavingaccount.app.controller;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysavingaccount.app.entity.SavingPlan;
import com.mysavingaccount.app.repository.SavingPlanRepository;
import com.mysavingaccount.app.service.TransactionService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

	
	@Autowired
	private SavingPlanRepository savingPlanRepository;
	
	@Autowired
	private TransactionService transactionService;
	
	
	@Value("${razorpay.webhook.secret}")
	private String webhookSecret;
	
	
	@PostMapping("/razorpay")
	public ResponseEntity<String> handleRazorpayWebhook(
	        @RequestBody String payload,
	        @RequestHeader("X-Razorpay-Signature") String signature)
	{
	    try {
	        boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
	        if (!isValid) {
	            return ResponseEntity.status(400).body("Invalid signature");
	        }
	    } catch (RazorpayException e) {
	        return ResponseEntity.status(400).body("Signature verification failed");
	    }

	    JSONObject event = new JSONObject(payload);
	    String eventType = event.getString("event");
	    System.out.println("Received event: " + eventType);

	    String subscriptionId = event
	            .getJSONObject("payload")
	            .getJSONObject("subscription")
	            .getJSONObject("entity")
	            .getString("id");

	    System.out.println("subscriptionId: " + subscriptionId);

	    SavingPlan plan = savingPlanRepository.findByRazorpaySubscriptionId(subscriptionId);

	    if (plan == null) {
	        return ResponseEntity.ok("Plan not found, ignoring");
	    }

	    if (eventType.equals("subscription.authenticated")) {
	        plan.setStatus("AUTHENTICATED");
	        savingPlanRepository.save(plan);
	    }
	    else if (eventType.equals("subscription.activated")) {
	        plan.setStatus("ACTIVE");
	        savingPlanRepository.save(plan);
	    }
	    else if (eventType.equals("subscription.charged")) {
	        String paymentID = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("id");
	        long amountInPaise = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getLong("amount");

	        BigDecimal amount = BigDecimal.valueOf(amountInPaise).divide(BigDecimal.valueOf(100));
	        transactionService.recordTransaction(plan, amount, "SUCCESS", paymentID);

	        plan.setStatus("ACTIVE");
	        savingPlanRepository.save(plan);
	    }

	    return ResponseEntity.ok("ok");
	}
}
