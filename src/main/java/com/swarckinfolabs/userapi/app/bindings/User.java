package com.swarckinfolabs.userapi.app.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Long mobile;
	
	private String gender;
	
	private LocalDate dob;
	
	private String country;

}
