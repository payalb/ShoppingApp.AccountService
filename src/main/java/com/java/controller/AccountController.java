package com.java.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.java.dao.AccountRepository;
import com.java.dto.Account;
import com.java.dto.ShopError;

@RequestMapping("/accounts")
@RestController
@CrossOrigin(origins="*")
public class AccountController {
	
	@Autowired AccountRepository ar;
	
	@PostMapping(consumes= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> registerAccount(@RequestBody Account account, BindingResult result, HttpServletRequest request) {
		if ( account == null ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Empty details")).build());
		}else if(result.hasErrors()) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(
					result.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList())));
		} else {
			ar.save(account);
			URI uri=UriComponentsBuilder.fromUriString(request.getRequestURI().toString()).path(account.getUsername()).build().toUri();
			return ResponseEntity.created(uri).build();
		}
	}
	
	@GetMapping(path="/{accountId}")
	public ResponseEntity retrieveAccountById(@PathVariable String accountId ) {
		Optional<Account> acc = ar.findById(accountId);
		if( acc.isPresent() ) {
			return ResponseEntity.ok().body(acc.get());
		}
		else {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("User details not correct")).build());
		}
	}
	
	@PatchMapping(consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Object> updateAccount( Account account, BindingResult result ) {
		if ( account == null || !ar.findById(account.getUsername()).isPresent() ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("User details not correct")).build());
		}else if(result.hasErrors()) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(
					result.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList())));
		} else {
			ar.save(account);
			return ResponseEntity.ok().build();
		}
	}

	@DeleteMapping(path="/{accountId}")
	public ResponseEntity<Object> deleteAccount( @PathVariable String accountId )
	{
		if ( accountId.isEmpty() || !ar.findById(accountId).isPresent() ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("User details not correct")).build());
		}
		else {
			ar.deleteById(accountId);
			return ResponseEntity.ok().build();
		}
	}
	
	@PostMapping("/{accountId}/cart")
	public ResponseEntity<Object> addToCart( @PathVariable String accountId, @RequestParam String productId, @RequestParam Integer quantity)// , BindingResult result)
	{
		if ( !ar.findById(accountId).isPresent() || productId.isEmpty() || quantity == null || quantity < 1 ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Details you entered not correct")).build());
		}
		else {
			Integer temp = ar.retrieveQuantity(accountId, productId);
			if( temp == null)
				ar.insertCartItem(accountId, productId, quantity);
			else
				ar.updateCartItem(accountId, productId, (temp + quantity));
			return ResponseEntity.ok().build();
		}
	}
	
	@GetMapping(path="/{accountId}/cart")
	public ResponseEntity<List<Object[]>> getCart( @PathVariable String accountId ){
		Optional<Account> acc = ar.findById(accountId);
		if( acc.isPresent() ) {
			return ResponseEntity.ok().body(ar.getCart(accountId));
		}
		else {
			return null;
		}
	}
	
	@PatchMapping("/{accountId}/cart")
	public ResponseEntity<Object> modifyCart( @PathVariable String accountId, @RequestParam String productId, @RequestParam Integer quantity) //, BindingResult result) 
	{
		if ( !ar.findById(accountId).isPresent() || productId.isEmpty() || quantity < 1 ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Details you entered not correct")).build());
		}
		else {
			ar.updateCartItem(accountId, productId, quantity);
			return ResponseEntity.ok().build();
		}
	}
	
	@DeleteMapping("/{accountId}/cart")
	public ResponseEntity<Object> deleteCartItem( @PathVariable String accountId, @RequestParam String productId ) //, BindingResult result ) 
	{
		if ( !ar.findById(accountId).isPresent() || productId.isEmpty() || ar.retrieveQuantity(accountId, productId) == null || ar.retrieveQuantity(accountId, productId) < 1  ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Details you entered not correct")).build());
		}
		else {
			ar.deleteCartItem(accountId, productId);
			return ResponseEntity.ok().build();
		}
	}
	
	@DeleteMapping("/{accountId}/EmptyCart")
	public ResponseEntity<Object> emptyCart( @PathVariable String accountId ) //, BindingResult result )
	{
		if ( !ar.findById(accountId).isPresent() ) {
			return ResponseEntity.badRequest().body(ShopError.builder().message(Arrays.asList("Details you entered not correct")).build());
		}
		else {
			ar.clearCart(accountId);
			return ResponseEntity.ok().build();
		}
	}
}
