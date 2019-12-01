package com.java.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.java.dao.AccountRepository;
import com.java.dto.Account;
import com.java.dto.ShopError;

@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/login")
@Controller
public class LoginController {
	
	@Autowired AccountRepository ar;
	
	@PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity login(@RequestBody Account account, BindingResult result, HttpServletRequest request) {
		if ( account.getUsername().isEmpty() || account.getPassword().isEmpty() || ar.findByUsernameAndPassword(account.getUsername(), account.getPassword()) == null ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Empty details")).build());
		}else if(result.hasErrors()) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(
					result.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList())));
		} else {
			URI uri=UriComponentsBuilder.fromUriString(request.getRequestURI().toString()).path(account.getUsername()).build().toUri();
			return ResponseEntity.created(uri).build();
		}
	}
	
}
