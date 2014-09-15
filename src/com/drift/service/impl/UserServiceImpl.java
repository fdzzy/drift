package com.drift.service.impl;

//import java.sql.SQLException;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drift.bean.User;
import com.drift.bean.User.SiteType;
import com.drift.dao.UserDao;
import com.drift.dao.impl.DaoFactory;
import com.drift.service.UserService;
import com.drift.servlet.MyServletUtil;
import com.drift.util.MD5Util;
import com.drift.util.SendEmail;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao = null;
	
	public UserServiceImpl() {
		userDao = DaoFactory.createUserDao(); 
	}
	
	@Override
	public Result login(String username, String password) {		
		Result result = new Result();
		User user = null;
		
		if(username == null || username.isEmpty() ||
				password == null || password.isEmpty()) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		String email = null;
		if(username != null && (username.indexOf('@') != -1)) {
			email = username;
		}
		
		try {
			if(email == null) { // login using username
				user = userDao.findUserByName(username);
				if(user == null) {
					result.setCode(Result.ERR_USER_NOT_EXIST);
					return result;
				}
			} else { // login using email
				user = userDao.findUserByEmail(email);
				if(user == null) {
					result.setCode(Result.ERR_EMAIL_NOT_EXIST);
					return result;
				}
			}
			if(userDao.verifyPassword(user, password)) {
				if(!(userDao.isActivated(user))) {
					result.setCode(Result.ERR_USER_NOT_ACTIVATED);
				} else {
					result.setResultObject(user);
					result.setCode(Result.SUCCESS);
				}
			} else {
				result.setCode(Result.ERR_PASSWORD);
			}
		//} catch (SQLException e) {
		//	result.setCode(Result.ERR_SQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result loginForeign(SiteType type, String foreignUid) {
		Result result = new Result();
		User user = null;
		int typeValue = type.ordinal();
		
		if(typeValue < 0 || typeValue >= SiteType.LAST.ordinal() || foreignUid == null 
				||foreignUid.isEmpty() ) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		try {
			user = userDao.findForeignUser(typeValue, foreignUid);
			if(user == null) {
				result.setCode(Result.ERR_USER_NOT_EXIST);
				return result;
			}

			if(!userDao.isActivated(user)) {
				/* Give the user some time to try out before activation is required 
				 */
				long now = System.currentTimeMillis() / 1000;
				long registerTs = user.getRegisterTs() / 1000;
				if((now - registerTs) > 24 * 3600) {
					System.err.println("now: " + now +", register: " + registerTs);
					System.err.println("uid " + user.getUid() + " not activated!");
					result.setCode(Result.ERR_USER_NOT_ACTIVATED);
					return result;
				}
			}
			result.setResultObject(user);
			result.setCode(Result.SUCCESS);
		//} catch (SQLException e) {
		//	result.setCode(Result.ERR_SQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result getUserById(int uid) {
		Result result = new Result();
		
		if(uid <= 0) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User user = null;
		try {
			user = userDao.findUserById(uid);
			if(user == null) {
				result.setCode(Result.ERR_USER_ID);
			} else {
				result.setResultObject(user);
				result.setCode(Result.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int register(User user, String password) {
		if(user == null || password == null || password.isEmpty()) {
			return Result.ERR_BAD_ARGS;
		}
		
		User u = null;
		try {
			u = userDao.findUserByName(user.getUsername());
			if(u != null) {
				return Result.ERR_USER_EXISTS;
			}
			if(!allowEmail(user.getEmail())) {
				return Result.ERR_EMAIL_REJECTED;
			}
			u = userDao.findUserByEmail(user.getEmail());
			if(u != null) {
				return Result.ERR_EMAIL_EXISTS;
			}
			String birthday = user.getBirthday();
			if(birthday == null || birthday.isEmpty()) {
				user.setBirthday("1990-01-01");
			}
			userDao.save(user, password);
			user = userDao.findUserByName(user.getUsername());
			sendActivationEmail(user);
			return Result.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.ERR_GENERIC;
	}

	@Override
	public Result registerForeign(User user, SiteType type, String foreignUid) {
		Result result = new Result();
		
		if(user == null || type.ordinal() < 0 || type.ordinal() > SiteType.LAST.ordinal() ||
				foreignUid == null || foreignUid.isEmpty()) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User u = null;
		try {
			u = userDao.findForeignUser(type.ordinal(), foreignUid);
			if(u != null) {
				result.setCode(Result.ERR_USER_EXISTS);
				return result;
			}
			if(!allowEmail(user.getEmail())) {
				result.setCode(Result.ERR_EMAIL_REJECTED);
				return result;
			}
			String birthday = user.getBirthday();
			if(birthday == null || birthday.isEmpty()) {
				user.setBirthday("1990-01-01");
			}
			userDao.saveForeign(user, type.ordinal(), foreignUid);
			user = userDao.findForeignUser(type.ordinal(), foreignUid);
			sendActivationEmail(user);
			result.setResultObject(user);
			result.setCode(Result.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int doActivation(String email, String codeInput) {
		int result = Result.ERR_GENERIC;
		
		if(email == null || !isLegalEmail(email) ||
				codeInput == null || codeInput.isEmpty()) {
			return Result.ERR_BAD_ARGS;
		}
		
		try {
			User user = userDao.findUserByEmail(email);
			//System.out.println(email);
			if(user != null) {
				if(getActivationCode(user).equals(codeInput)) {
					userDao.setActivate(user);
					result = Result.SUCCESS;
				}
			} else {
				result = Result.ERR_EMAIL_NOT_EXIST;
			}
		} catch (SQLException e) {
			// catch exception from setActivate
			result = Result.ERR_SQL;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * Send user an activation email upon registration
	 */
	private void sendActivationEmail(User user) {
		String activationCode = getActivationCode(user);
		String username = user.getUsername();
		String email = user.getEmail();
		
		String subject = username + "，欢迎注册寻寻觅觅";
		StringBuffer content = new StringBuffer();
		content.append("尊敬的用户" + username + ":<br/><br/>");
		content.append("您好！欢迎注册寻寻觅觅！<br/><br/>");
		content.append("请点击以下链接激活账号，链接仅使用一次，请尽快激活！<br/>");    		  
		String link = MyServletUtil.entryURL + "/register?action=activate&email="
				+ email + "&activateCode=" + activationCode;
		content.append("<a href=\"" + link + "\">");
		content.append("点击此处激活你的账号");
		content.append("</a>");
		content.append("<br/><br/>寻寻觅觅工作组");
		SendEmail.send(email, subject, content.toString());		
	}

	private String getActivationCode(User user) {
		StringBuffer sb = new StringBuffer();
		sb.append(user.getUid());
		sb.append(user.getEmail());
		sb.append(user.getUsername());
		sb.append(user.getSex().toString());
		sb.append(user.getRegisterTs());
		return MD5Util.encode2hex(sb.toString());
	}

	@Override
	public int checkUsername(String username) {
		if(username == null || username.isEmpty()) {
			return Result.ERR_BAD_ARGS;
		}

		User u = null;
		try {
			u = userDao.findUserByName(username);
			if(u != null) {
				return Result.ERR_USER_EXISTS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.SUCCESS;
	}

	@Override
	public int checkEmail(String email) {
		if(email == null || !isLegalEmail(email) ) {
			return Result.ERR_BAD_ARGS;
		}
		
		if(!allowEmail(email)) {
			return Result.ERR_EMAIL_REJECTED;
		}

		User u = null;
		try {
			u = userDao.findUserByEmail(email);
			if(u != null) {
				return Result.ERR_EMAIL_EXISTS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.SUCCESS;
	}

	/**
	 * Allow only *@*.edu.cn for now
	 * @param email
	 * @return
	 */
	private boolean allowEmail(String email) {
		String patternStr = "@\\w+\\.edu\\.cn";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
	
	private boolean isLegalEmail(String email) {
		String patternStr = "@\\w+\\.\\w+";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}

	@Override
	public int editProfile(User user) {
		int result = Result.ERR_GENERIC;

		String birthday = user.getBirthday();
		if(birthday == null || birthday.isEmpty()) {
			user.setBirthday("1990-01-01");
		}
		
		try {
			userDao.update(user);
			result = Result.SUCCESS;
		} catch (SQLException e) {
			// catch Exception from update
			result = Result.ERR_USER_ID;			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
	}

	@Override
	public String getFullPhotoUrl(User user) {
		String url = user.getPhotoUrl();
		
		if(url == null || url.isEmpty()) {
			url = "img/" + user.getSex().toString() + ".jpg";
		} else {
			url = "photo/" + MyServletUtil.timestampToDate(user.getRegisterTs()) +
					"/" + user.getUid() + "/" + url;
		}
		
		return url;
	}


}
