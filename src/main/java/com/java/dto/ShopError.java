package com.java.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ShopError {
	List<String> message;
}
