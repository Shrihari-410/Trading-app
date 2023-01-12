package com.swarckinfolabs.userapi.app.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.swarckinfolabs.userapi.app.bindings.ActivateAccount;
import com.swarckinfolabs.userapi.app.bindings.Login;
import com.swarckinfolabs.userapi.app.bindings.User;
import com.swarckinfolabs.userapi.app.entity.UserMaster;
import com.swarckinfolabs.userapi.app.repo.UserMasterRepo;
import com.swarckinfolabs.userapi.app.utils.EmailUtils;

@Service
public class UserApiServiceImpl implements UserApiService{

	@Autowired
	private UserMasterRepo userMasterRepo ;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Override
	public boolean saveUser(User user) {
		UserMaster entity=new UserMaster();
		BeanUtils.copyProperties(user, entity);
		
		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("in-Active");
		
		UserMaster save = userMasterRepo.save(entity);
		
		String subject="Your Registration Success";
		String filename="REG-EMAIL-BODY.txt";
		String body=readEmailBody(entity.getFirstName(), entity.getPassword(),filename);
		
		emailUtils.sendEmail(user.getEmail(),subject, body);
		
		return save.getUserId() !=null;
	}

	@Override
	public boolean activateUserAcc(ActivateAccount activateAcc) {
		UserMaster entity=new UserMaster();
		entity.setEmail(activateAcc.getEmail());
		entity.setPassword(activateAcc.getTempPwd());
		
		//select * from user_master where email=? and pwd=?
		Example<UserMaster> of=Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(of);
		if( findAll.isEmpty()) {
			return false;
		}else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAcc.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}
	@Override
	public List<User> getAllUser() {
		List<UserMaster> findAll = userMasterRepo.findAll();
		List<User> users=new ArrayList<>();
		for(UserMaster entity:findAll) {
			User user=new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if(findById.isPresent()) {
			 User user=new User();
			 UserMaster entity=findById.get();
			 BeanUtils.copyProperties(entity, user);
			 return user;
		}
		return null;
	}

	@Override
	public User updateUser(User us,Integer userId) {
		Optional<UserMaster> findByUid = userMasterRepo.findById(userId);
		if(findByUid.isPresent()) {
			User user=new User();
			 UserMaster entity=findByUid.get();
					 entity.setFirstName(us.getFirstName());			 
					 entity.setLastName(us.getLastName());
					 entity.setEmail(us.getEmail());
					 entity.setMobile(us.getMobile());
					 entity.setDob(us.getDob());
					 entity.setGender(us.getGender());
					 entity.setCountry(us.getCountry());
			userMasterRepo.save(entity);
			 BeanUtils.copyProperties(entity, user);
			 return user;

		}
		return null;
	}
	
	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if(findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {
		UserMaster entity = userMasterRepo.findByEmailAndPassword(login.getEmail(), login.getPassword());
		if(entity==null) {
			return "Invalid Credentials";
		}
		if(entity.getAccStatus().equals("Active")) {
			return "SUCCESS";
		}else {
			return "Account Not Activated";
		}
	}

	@Override
	public String forgotPassword(String email) {
		UserMaster entity = userMasterRepo.findByEmail(email);
		
		if(entity==null) {
			return "Invalid Email";
		}
		String subject = "Forgot Password";
		String filename = "RECOVER-PWD-BODY.txt";
		String body = readEmailBody(entity.getFirstName(), entity.getPassword(), filename);
		
		boolean sendEmail=emailUtils.sendEmail(email, subject, filename); 
		if(sendEmail) {
			return "Password sent to your regestered email";
		}
		return null;
	}

	private String generateRandomPwd() {
	    String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String numbers = "0123456789";
	    String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
	    
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    int length = 6;
	    for(int i = 0; i < length; i++) {
	      int index = random.nextInt(alphaNumeric.length());
	      char randomChar = alphaNumeric.charAt(index);
	      sb.append(randomChar);
	    }
	    return sb.toString();
	}
	
	private String readEmailBody(String firstName,String pwd,String filename) {
		String url="";
		String mailBody=null;
		try {
			
			FileReader fr=new FileReader(filename);
			BufferedReader br= new BufferedReader(fr);
			 
			StringBuffer buffer=new StringBuffer();
			
			String line=br.readLine();			
			while(line !=null) {
				buffer.append(line);
				line=br.readLine();
			}
			br.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FIRSTNAME}", firstName);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}

	
}
