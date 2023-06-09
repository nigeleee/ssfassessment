package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class ProtectedController {

	@GetMapping(path = {"/protected", "/view1.html"})
	public String protectedPage() {
		return "view1";
	}
	// TODO Task 5
	// Write a controller to protect resources rooted under /protected
}
