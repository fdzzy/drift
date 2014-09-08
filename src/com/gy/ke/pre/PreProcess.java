package com.gy.ke.pre;

//微博 预处理
/**
 * 
 * 去除 表情
 * 只要原创
 * 去除链接
 * @author GY
 *
 */
public class PreProcess {

	public static String pre(String str){
		
		//\u005b为[
		str = str.replaceAll("(\\u005B.{1,5}]|"
							+ "http://([a-zA-Z]+\\u002E)+[a-zA-Z]+(/|[a-zA-Z0-9]*)*)", " ");//去掉表情和链接
		// 去除转载//@胡师傅很和尚：
		//str = str.replaceAll("@[\\w\\u4e00-\\u9fa5]+", "");
		// \uff1a为中文冒号“：”
		str = str.replaceAll("@.+?[:\\s\\uff1a]", " ");
		str = str.replaceAll("@.+?$", " ");
		//str = str.replaceAll("w", "a");			
		return str;
		
	}
	
	public static void main(String[] args){
		
		String s = PreProcess.pre("[汗daw]我能说什么呢，（分享自 @图灵社区） http://www.t.cn/8FE3PAw"
				+ "图书馆居然没开门");
		System.out.print(s);
		
	}
}
