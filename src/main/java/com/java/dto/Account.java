package com.java.dto;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
	// will use relational database
	@Id// (strategy=GenerationType.AUTO)
	private String username;
	private String password;
	@Enumerated
	private Gender gender;
	@Enumerated
	private UserRole role;
	private Long mobile;
	@ElementCollection
	@OrderColumn(name = "index")
	private List<String> addresses;
	private String email;
	@ElementCollection
	private Map<String, Integer> cart;
	public enum UserRole {
		ADMIN, USER
	}
	
	public enum Gender {
		MALE, FEMALE
	}
	
}


