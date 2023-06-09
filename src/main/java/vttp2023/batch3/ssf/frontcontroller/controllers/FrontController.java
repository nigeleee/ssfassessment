package vttp2023.batch3.ssf.frontcontroller.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import vttp2023.batch3.ssf.frontcontroller.model.Login;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
@RequestMapping
public class FrontController {

	@Autowired
	private AuthenticationService service;
	private RedisTemplate<String, Integer> redisTemplate;
	@GetMapping(produces = "application/x-www-form-urlencoded")
	public String homePage(Model model) {

		model.addAttribute("login", new Login());
		return "view0";	

		Captcha captcha = service.createCaptcha();
        redisTemplate.opsForValue().set("captcha", captcha.getResult());

        model.addAttribute("login", new Login());
        model.addAttribute("captcha", captcha.getExpression());

        return "login";
	}

	@PostMapping(path = "/authenticate", produces ="application/json") 
	public String authentication(Model model, @RequestParam("captcha") int captcha, @Valid Login login, BindingResult result) throws Exception {
		
		if(result.hasErrors()){
			model.addAttribute("login", login);
			return "view0";
		}

		Integer captchaResult = redisTemplate.opsForValue().get("captcha");
		if(captchaResult == null || captcha != captchaResult){
			model.addAttribute("error", "invalid captcha");
			return "view0";
		}
		service.authenticate(login.getUsername(), login.getPassword());

		redisTemplate.delete("captcha");
		return "redirect:/protectedPage";
	}

	
	
}


	
	




// TODO: Task 2, Task 3, Task 4, Task 6