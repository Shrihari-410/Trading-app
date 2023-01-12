package com.swarckinfolabs.userapi.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarckinfolabs.userapi.app.entity.UserMaster;



public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {

	public UserMaster findByEmailAndPassword(String email,String password);
	
	public UserMaster findByEmail(String email);
}
