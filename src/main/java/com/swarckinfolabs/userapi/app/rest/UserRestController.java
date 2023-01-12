package com.swarckinfolabs.userapi.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swarckinfolabs.userapi.app.bindings.ActivateAccount;
import com.swarckinfolabs.userapi.app.bindings.Login;
import com.swarckinfolabs.userapi.app.bindings.User;
import com.swarckinfolabs.userapi.app.service.UserApiService;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserApiService service;
	
	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user){
		boolean isSaved = service.saveUser(user);
		if(isSaved) {
			return new ResponseEntity<String>("Registration Success",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String>("Registration Failed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount acc){
		boolean isActivated = service.activateUserAcc(acc);
		if(isActivated) {
			return new ResponseEntity<String>("Account activated",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Invalid Temporary password",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUser = service.getAllUser();
		return new ResponseEntity<>(allUser,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserId(@PathVariable Integer userId){
		User user = service.getUserById(userId);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@PutMapping("/user/{userId}")
	public ResponseEntity<User> updateUser(@RequestBody User u,@PathVariable Integer userId){
		User updatedUser = service.updateUser(u,userId);
		return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId){
		boolean isDeleted = service.deleteUserById(userId);
		if(isDeleted) {
			return new ResponseEntity<String>("Deleted",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Failed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId,@PathVariable String status){
		boolean isChanged = service.changeAccountStatus(userId, status);
		if(isChanged) {
			return new ResponseEntity<String>("Status Changed",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Failed to Change",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login){
		String status = service.login(login);
		return new ResponseEntity<String>(status,HttpStatus.OK);
	}
	
	@GetMapping("/forgotpwd/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email){
		String status = service.forgotPassword(email);
		return new ResponseEntity<String>(status,HttpStatus.OK);
	}
}
