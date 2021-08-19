package com.example.demo.controller;

import com.example.demo.model.SenderResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DemoController {

	@GetMapping(value = "/demo")
	public SenderResponse adminEndPoint(Authentication authentication) {
		return new SenderResponse("Welcome to  : " + authentication.getPrincipal());
	}

	@GetMapping(value = "/manager")
	public SenderResponse managerEndPoint(Authentication authentication) {
		return new SenderResponse("Welcome to  : " + authentication.getName());
	}
}