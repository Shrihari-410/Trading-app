package com.swarckinfolabs.userapi.app.service;

import java.util.List;

import com.swarckinfolabs.userapi.app.bindings.ActivateAccount;
import com.swarckinfolabs.userapi.app.bindings.Login;
import com.swarckinfolabs.userapi.app.bindings.User;

public interface UserApiService {	

	 boolean saveUser(User user);
	
	 boolean activateUserAcc(ActivateAccount activateAcc);
	
	 List<User> getAllUser();
	
	 User getUserById(Integer userId);
	 
	 User updateUser(User us,Integer userId);
	
	 boolean deleteUserById(Integer userId);
	
	 boolean changeAccountStatus(Integer userId,String status);
	
	 String login(Login login);
	
	 String forgotPassword(String email);

}
