package com.mycompany.api.unit.conversion.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

}
