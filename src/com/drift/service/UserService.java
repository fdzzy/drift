package com.drift.service;

import com.drift.bean.User;
import com.drift.bean.User.SiteType;
import com.drift.service.impl.Result;

public interface UserService {
	/**
	 * login a user using password
	 * @param username
	 * @param password
	 * @return
	 */
	public Result login(String username, String password);
	/**
	 * login a foreign site user
	 * @param type
	 * @param foreignUid
	 * @return
	 */
	public Result loginForeign(SiteType type, String foreignUid);
	/**
	 * TODO: This is needed by API, may be we have ways to avoid exposing this interface
	 * @param uid
	 * @return
	 */
	public Result getUserById(int uid);
	/**
	 * Register a user
	 * @param user
	 * @param password
	 * @return
	 */
	public int register(User user, String password);
	public Result registerForeign(User user, SiteType type, String foreignUid);
	public int checkUsername(String username);
	public int checkEmail(String email);
	public int doActivation(String email, String codeInput);
	/**
	 * Update user profile
	 * @param user
	 * @return
	 */
	public int editProfile(User user);
	public String getFullPhotoUrl(User user);
	
}
