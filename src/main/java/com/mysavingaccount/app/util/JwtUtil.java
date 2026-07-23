package com.mysavingaccount.app.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	
	private SecretKey key;
	
	@PostConstruct
	public void init() {
	    key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
	}

	@Value("${jwt.secret}")
	private String secretKeyString;

	@Value("${jwt.expiration}")
	private long expiration;
	
	
	public String generateToken(String email)
	{
		String token = Jwts.builder()
				.subject(email)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(key)
				.compact();
		
		return token;
	}
	
	public String extractEmail(String token)
	{
		Claims claims = Jwts.parser()
			    .verifyWith(key)
			    .build()
			    .parseSignedClaims(token)
			    .getPayload();

			String email = claims.getSubject();
			
			return email;
	}
	
	public boolean isTokenExpired(String token)
	{
		Claims claims = Jwts.parser()
			    .verifyWith(key)
			    .build()
			    .parseSignedClaims(token)
			    .getPayload();
		
		Date expiryDate = claims.getExpiration();
		
		if(expiryDate.before(new Date()))
		{
			return true;
		}
		return false;
	}
	
	public boolean isTokenValid(String token, String email)
	{
		String extractedEmail = extractEmail(token);
	    return extractedEmail.equals(email) && !isTokenExpired(token);
	}
	
	
}
