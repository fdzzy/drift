package com.drift.junit;

import javax.naming.Context;



import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

import junit.framework.TestCase;

public class UserServiceTest extends TestCase {
	UserService service = null;
	Context context = null;

	protected void setUp() throws Exception {
		super.setUp();
		service = ServiceFactory.createUserService();
		//context = new InitialContext();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRegister() {
		fail("Not yet implemented");
	}

	public void testLogin() {
		String username = "admin";
		String password = "admin";
		Result result = null;
		
		result = service.login(username, password);
		assertEquals(Result.SUCCESS, result.getCode());
	}

}
