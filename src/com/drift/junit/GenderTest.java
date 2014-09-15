package com.drift.junit;

import com.drift.bean.Gender;

import junit.framework.TestCase;

public class GenderTest extends TestCase {

	public void testGender() {
		assertEquals(0, Gender.MALE.ordinal());
		assertEquals(1, Gender.FEMALE.ordinal());
		assertEquals("male", Gender.MALE.toString());
		assertEquals("female", Gender.FEMALE.toString());
		assertEquals(Gender.MALE, Gender.makeGender(0));
		assertEquals(Gender.FEMALE, Gender.makeGender(1));
		assertEquals(true, Gender.makeGender(1).equals(Gender.FEMALE));
	}
}
