package com.mycompany.api.unit.conversion.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseLoader implements CommandLineRunner {

	@Autowired UserRepository repo;
	
	@Override
	public void run(String... args) throws Exception {
		
		User user1 = new User("admin", "$2a$10$9U.Qw8EdjaZIlsDEgrZrWugXLyEysIYvCNVbxR.q8c.IvZvGvA/12"); //pass:admin
		User user2 = new User("raju", "$2a$10$Cwh.BoZ5zLt.mOcjynM5H.FfPVe5phg19bMX05m8FCKuvMKmr/Bi2"); //pass:raju
		User user3 = new User("rani", "$2a$10$ootfAWZHaJUQKxibFGTW8ezb0O6SjJZqpYKj8WJ3PEY0j.N2QjmLy"); //pass:rani
		
		repo.saveAll(List.of(user1, user2, user3));
		
		System.out.println("Loaded sample user data");
	}

}
