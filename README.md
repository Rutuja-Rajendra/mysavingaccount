MySavingAccount

A recurring savings platform that helps people build a saving habit — automatically.

The Problem

Most people genuinely want to save money every month, but good intentions rarely survive contact with real life. An emergency comes up, an unplanned expense appears, and the "I'll transfer money to savings this month" plan quietly falls apart — often after just one or two months.

MySavingAccount solves this by removing the human decision from the equation. Instead of relying on willpower and manual transfers, the user sets up a monthly amount once, authorizes it through a UPI/Card mandate, and the platform handles the rest automatically — acting like a strict, disciplined parent who won't let you skip a month.

Importantly, this app never touches or stores the user's money directly. It only orchestrates the schedule — the actual money movement is handled entirely by Razorpay, a licensed payment gateway.

How It Works
Register & Login — users create an account and log in securely.
Start a Saving Plan — the user enters a monthly amount they want to save.
Authorize the Mandate — the app creates a real Razorpay Plan + Subscription and redirects the user to Razorpay's checkout to authorize a recurring UPI/Card mandate.
Automatic Monthly Charging — once authorized, Razorpay itself owns the billing schedule and automatically charges the user each month — no manual action needed.
Stay Informed — the user receives email notifications at every key step: welcome email, plan created, plan modified, plan stopped.
Modify or Stop Anytime — users can change their monthly amount or stop their plan (which cancels the Razorpay subscription) directly from their dashboard.
Key Design Decision: Webhook-Driven, Not Polling-Driven

An early version of this project used a scheduler that polled the database every 30 seconds to check for due payments and simulated charging them. Once real Razorpay Subscriptions were integrated, this was intentionally retired.

Razorpay now owns the billing cycle entirely. The application only reacts to real-time webhook events Razorpay sends:

subscription.authenticated — user completed mandate registration
subscription.activated — subscription is live and ready to be billed
subscription.charged — a successful monthly payment occurred (a Transaction record is created)

All incoming webhooks are verified using HMAC-SHA256 signature validation before being trusted — an unsigned or forged request is rejected outright.

Features
JWT-secured REST API (for programmatic/API access)
Session-based authentication for the web UI (Spring Security + Thymeleaf)
Saving plan lifecycle: create, modify, stop
Real Razorpay Subscriptions integration (Plan + Subscription creation, UPI Autopay / Card mandates)
Signature-verified webhook handling for subscription and payment events
Transaction history tracking
Email notifications (welcome, plan created, modified, stopped) via Gmail SMTP
Server-rendered web dashboard built with Thymeleaf
Tech Stack
Layer	Technology
Language	Java
Framework	Spring Boot, Spring Security, Spring Data JPA
Authentication	JWT (REST API), Session-based (Web UI)
Database	PostgreSQL
Frontend	Thymeleaf, HTML, CSS
Payments	Razorpay Subscriptions API (Test Mode)
Email	Spring Mail (Gmail SMTP)
API Testing	Postman
Build Tool	Maven
Project Structure
src/main/java/com/mysavingaccount/app/
├── config/          # Security, Razorpay client configuration
├── controller/      # REST controllers (JSON) + Web controllers (Thymeleaf)
├── dto/             # Request/response data transfer objects
├── entity/          # JPA entities (User, SavingPlan, Transaction)
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter, UserDetailsService
├── service/         # Business logic (Auth, SavingPlan, Payment, Notification)
└── util/            # JWT utility

src/main/resources/
├── templates/       # Thymeleaf HTML pages
├── static/css/      # Stylesheets
└── application-example.properties
Getting Started
Prerequisites
Java 17+
Maven
PostgreSQL
A Razorpay account (Test Mode)
A Gmail account with an App Password (for email notifications)
Setup
Clone the repository
bash
   git clone https://github.com/Rutuja-Rajendra/mysavingaccount.git
   cd mysavingaccount
Create a PostgreSQL database
sql
   CREATE DATABASE mysavingaccount_db;
Copy application-example.properties to application.properties and fill in your own values:
properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/mysavingaccount_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password

   jwt.secret=your_jwt_secret
   jwt.expiration=86400000

   razorpay.key.id=your_razorpay_test_key_id
   razorpay.key.secret=your_razorpay_test_key_secret
   razorpay.webhook.secret=your_webhook_secret

   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_gmail_app_password
Run the application
bash
   ./mvnw spring-boot:run
Visit http://localhost:8080/register to create an account and get started.
Testing Webhooks Locally

Razorpay needs a public URL to send webhook events to. Use ngrok to expose your local server:

bash
ngrok http 8080

Then register the generated URL (https://your-ngrok-url.ngrok-free.app/api/webhooks/razorpay) in your Razorpay dashboard under Settings → Webhooks.

API Endpoints (REST)
Method	Endpoint	Description
POST	/api/auth/register	Register a new user
POST	/api/auth/login	Login, returns a JWT
POST	/api/savings/start	Create a new saving plan
PUT	/api/savings/modify	Modify the active plan's amount
PUT	/api/savings/stop	Stop the active plan
POST	/api/webhooks/razorpay	Razorpay webhook receiver (signature-verified)
Known Limitations
Razorpay's Test Mode sandbox checkout occasionally returns a generic server error unrelated to this application's code — this is a documented, known issue on Razorpay's side, not a bug in the integration itself.
Due-date reminder emails and post-payment confirmation emails are planned but not yet implemented, pending more reliable end-to-end testing of the full billing cycle.

Author
Rutuja Bandle GitHub · rutub175@gmail.com
