package com.mysavingaccount.app.service;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Plan;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;

@Service
public class PaymentService{

	@Autowired
	private RazorpayClient razorpayClient;
	
	public SubscriptionHelper createPlanAndSubscription(BigDecimal amount) throws RazorpayException
	{
		//Plan
		JSONObject planRequest = new JSONObject();
		
		planRequest.put("period", "monthly");
		planRequest.put("interval", 1);
		
		
		JSONObject item = new JSONObject();
		
		item.put("name", "Monthly Saving Plan");
		item.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		item.put("currency", "INR");
		item.put("description", "Monthly saving plan of ₹" + amount);
		
		
		planRequest.put("item", item);

		Plan plan = razorpayClient.plans.create(planRequest);
		
		String planID = plan.get("id");
		
		//Subscription
		
		JSONObject subscriptionRequest = new JSONObject();
		
		subscriptionRequest.put("plan_id", planID);
		subscriptionRequest.put("total_count", 120);
		subscriptionRequest.put("customer_notify", 1);
		
		Subscription subscription = razorpayClient.subscriptions.create(subscriptionRequest);
		
		SubscriptionHelper result = new SubscriptionHelper();
		result.planId = planID;
		result.subscriptionId = subscription.get("id");
		result.shortUrl = subscription.get("short_url");
		return result;
	
	}
}

class SubscriptionHelper 
{
	String planId;
	String subscriptionId;
	String shortUrl;
}

