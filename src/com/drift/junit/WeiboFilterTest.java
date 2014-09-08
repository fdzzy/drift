package com.drift.junit;

import com.gy.ke.pre.PreProcess;

import junit.framework.TestCase;

public class WeiboFilterTest extends TestCase {
	
	private void helper(String weibo) {
		String result = PreProcess.pre(weibo);
		System.out.println(weibo + "\n" + result);
	}

	public void testPre() {
		helper("[汗daw]我能说什么呢，（分享自 @图灵社区） http://www.t.cn/8FE3PAw"
				+ "图书馆居然没开门");
		helper("//@胡师傅很和尚: 转发微博");
		helper("//@卖野猪的://@187_一鸣:可以再补充一些LXC方面的//@DataScientist:[good]");		
	}

}
