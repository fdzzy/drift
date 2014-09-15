package com.drift.dao;

import com.drift.bean.Bottle;
import com.drift.bean.User;

public interface BottleDao {

	/**
	 * Find a bottle by bottle ID
	 * @param bottleId
	 * @return
	 * @throws Exception
	 */
	Bottle findBottleById(int bottleId) throws Exception;
	/**
	 * Get a bottle for the user
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Bottle getBottle(User user) throws Exception;
	/**
	 * Post a bottle into the DB
	 * @param user
	 * @param content
	 * @return
	 * @throws Exception: no bottle, sql error
	 */
	public void postBottle(User user, String content) throws Exception;
	/**
	 * Set bottle be unread
	 * @param bottle
	 * @throws Exception 
	 */
	void setBottleUnread(int bottleId) throws Exception;
	/**
	 * Set bottle's receiver to the user argument
	 * @param bottleId
	 * @param user
	 * @throws Exception
	 */
	//void setBottleReceiver(int bottleId, User user) throws Exception;
	
}
