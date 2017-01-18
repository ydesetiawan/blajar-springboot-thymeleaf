package com.ydes.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
	
	public static void main(String args[]){
		
		String password = "admin";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);

		System.out.println(hashedPassword);
	}

}
