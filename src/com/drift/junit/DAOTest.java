package com.drift.junit;

//import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.drift.core.DAO;


import junit.framework.TestCase;

public class DAOTest extends TestCase {
	
	private boolean allowEmail(String email) {
		
		String patternStr = "@\\w+\\.edu\\.cn";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
		
	}
	
	public void testAllowEmail() {
	
		/*try {
			Method m = DAO.class.getDeclaredMethod("allowEmail", new Class[]{String.class});
			m.setAccessible(true);
			
			boolean result = false;
			String email = "0511@fudan.edu.cn";
			result = (boolean)m.invoke(DAO.class, new Object[]{email});
			assertEquals(true, result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		assertEquals(true, allowEmail("0511@fudan.edu.cn"));
		assertEquals(true, allowEmail("0ads1@sjtu.edu.cn"));
		assertEquals(true, allowEmail("0511_sdx@tongji.edu.cn"));
		assertEquals(false, allowEmail("kadfafudan.edu.cn"));
		assertEquals(false, allowEmail("asdgklj@163.com"));
		
		
	}

}
