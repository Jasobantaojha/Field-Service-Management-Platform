package com.KeyStone.FieldServiceProject.Repository;

import java.beans.Customizer;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KeyStone.FieldServiceProject.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer>findByEmail(String email);
	boolean existsByEmail(String email);
	
}
