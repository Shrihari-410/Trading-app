package com.swarckinfolabs.userapi.app.entity;

import java.time.LocalDate;
   
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="USER_DETAILS")
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Long mobile;
	
	private String gender;
	
	private LocalDate dob;
	
	private String password;
	
	private String accStatus;
	
	private String country;
	
}
