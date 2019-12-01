package com.java.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Account;

@Transactional
public interface AccountRepository extends JpaRepository<Account, String> {
	
	Account findByUsernameAndPassword(String username, String password);
	
	@Modifying
	@Query(nativeQuery=true, value= "update account_cart set cart = ?3 where cart_key = ?2 and account_username = ?1")
	void updateCartItem( String username, String productId, Integer quantity );
	
	@Modifying
	@Query(nativeQuery=true, value= "insert into account_cart( account_username, cart_key, cart ) values ( ?1, ?2, ?3 )")
	void insertCartItem( String username, String productId, int quantity );
	
	@Query(nativeQuery=true, value= "select cart_key, cart from account_cart where account_username = ?1")
	List<Object[]> getCart(String username);
	
	@Query(nativeQuery=true, value= "select cart from account_cart where account_username = ?1 and cart_key = ?2")
	Integer retrieveQuantity( String username, String productId );
	
	@Modifying
	@Query(nativeQuery=true, value= "delete from account_cart where account_username = ?1 and cart_key = ?2 " )
	void deleteCartItem( String username, String productId );
	
	@Modifying
	@Query(nativeQuery=true, value= "delete from account_cart where account_username = ?1")
	void clearCart(String username );
	
	@Modifying
	@Query(nativeQuery=true, value= "update account_addresses set addresses = ?3 where account_username = ?1 and index = ?2")
	void updateAddress( String username, int index, String newAddress );
	
	@Modifying
	@Query(nativeQuery=true, value= "insert into account_addresses(account_username, index, addresses) values (?1, ?2, ?3)")
	void insertAddress( String username, int index, String newAddress );
	
	@Modifying
	@Query(nativeQuery=true, value= "delete from account_addresses where account_username = ?1 and index = ?2)")
	void insertAddress( String username, int index );
	
}
