package com.drift.dao;

import com.drift.bean.User;

public interface UserDao {
	
	// For Login
	
	/**
	 * Find User instance 
	 * @param uid
	 * @return
	 * @throws Exception 
	 */
	User findUserById(int uid) throws Exception;
	User findUserByName(String username) throws Exception;
	User findUserByEmail(String email) throws Exception;
	User findForeignUser(int type, String foreignUid) throws Exception;
	/**
	 * Verify password
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	boolean verifyPassword(User user, String password) throws Exception;
	/**
	 * Check if user is activated
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	boolean isActivated(User user) throws Exception;
	/**
	 * Set the user to be activated
	 */
	boolean setActivate(User user) throws Exception;
	/**
	 * Save a specific User instance
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	void save(User user, String password) throws Exception;
	void saveForeign(User user, int siteType, String foreignUid) throws Exception;
	/**
	 * Update a specific User instance
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	void update(User user) throws Exception;
	/**
	 * Delete a user, not supported for now
	 * @param user
	 * @return
	 */
	//int delete(User user);
	
	
	public int SUCCESS = 0;
	public int ERR_GENERIC = -1;
	public int ERR_SQL = -2;
	public int ERR_PASSWORD = -3;

}
