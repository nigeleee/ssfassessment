package vttp2023.batch3.ssf.frontcontroller.services;

import org.springframework.http.MediaType;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationRepository repo;
	private RestTemplate template;
	private RedisTemplate<String, Object> redisTemplate;


	public void setTemplate(RestTemplate template) {
		this.template = template;
	}

	public AuthenticationService(RestTemplate template) {
		this.template = template;
	}

	public void authenticate(String username, String password) throws Exception {
		String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
		
		HttpHeaders head = new HttpHeaders();
		head.setContentType(MediaType.APPLICATION_JSON);
		head.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<>(requestBody, head);
		
		ResponseEntity<String> templateResponse = template.exchange("https://authservice-production-e8b2.up.railwayapp/api/authenticate", 
		HttpMethod.POST, entity, 
		String.class);

		if(templateResponse.getStatusCode() == HttpStatus.OK) {
			String message = "{\"message\": Authenticated " + username + "\"}";
			System.out.println(message);

		} else {
			throw new Exception("Authentication failed: " + templateResponse.getStatusCode());
		}
	}

		public void disableUser(String username) {
		int failedAttempts = numAttempts(username);

		if(failedAttempts >= 3) {
			redisTemplate.opsForValue().set("DISABLED_TEMP" + username, 1, Duration.ofMinutes(30));
		}
	}
	public boolean isLocked(String username) {
		return redisTemplate.hasKey("DISABLED_TEMP" + username);
	}

	public boolean verifyCaptcha(String username, int userAnswer) {
	int expectedUserAnswer = get(username);

	return userAnswer == expectedUserAnswer;
}
    private int numAttempts(String username) {
        String key = "UNSUCCESSFUL_ATTEMPTS_" + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            attempts = 0;
        }

        attempts++;
        redisTemplate.opsForValue().set(key, attempts);

        return attempts;
    }

    private int get(String username) {
        String key = "CAPTCHA_" + username;
        Integer captcha = (Integer) redisTemplate.opsForValue().get(key);

        if (captcha == null) {
            captcha = createCaptcha();
            redisTemplate.opsForValue().set(key, captcha, Duration.ofMinutes(5));
        }

        return captcha;
    }
	private int getRandomNumber(int i, int j) {
		Random random = new Random();
		return random.nextInt(50-1) + 1;
	}

	private String getRandomOperator() {
		String[] operators = {"+", "-", "*", "/"};
		int index = getRandomNumber(0, operators.length - 1);
		return operators[index];
	}

	private int calculate(int num1, int num2, String operator) {
		switch(operator) {
			case "+":
				return num1 + num2;
			case "-":
				return num1 - num2;
			case "*":
				return num1 * num2;
			case "/":
				return num1 / num2;
			default: throw new IllegalArgumentException("Invalid operator: " + operator);
			}
		}

	public int createCaptcha() {	
		int num1 = getRandomNumber(1,50);
		int num2 = getRandomNumber(1,50);
		String operator = getRandomOperator();

		int result = calculate(num1, num2, operator);

		return result;
	}	
   
}
