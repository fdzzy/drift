package com.gy.ke.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadText {
	private BufferedReader bufferedReader= null;
	private InputStreamReader read = null;
	/**
	 * path
	 * @param path
	 */
	public ReadText(String path){
		
		try{
			//File pwd = new File(".");
			//System.out.println(pwd.getAbsolutePath());
			//System.out.println(path);
			
			File file = new File(path);
			
			if(file.isFile() && file.exists()){ //判断文件是否存在
				//String fileEncode = System.getProperty("file.encoding"); 
				read = new InputStreamReader(
			            new FileInputStream(file),"UTF-8");
				
			    bufferedReader = new BufferedReader(read);
			   
			}else{
			    System.out.println("找不到指定的文件");
			}
		}catch (Exception e) {
		    System.out.println("读取文件内容出错");
		    e.printStackTrace();
		}
		
	}
	
	public ReadText(String path,String code){
		try{
			
			File file=new File(path);
			
			if(file.isFile() && file.exists()){ //判断文件是否存在
				
				read = new InputStreamReader(
			            new FileInputStream(file),code);
				
			    bufferedReader = new BufferedReader(read);
			   
			}else{
			    System.out.println("找不到指定的文件");
			}
		}catch (Exception e) {
		    System.out.println("读取文件内容出错");
		    e.printStackTrace();
		}
		
	}
	
	public String readLine(){
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void close(){
		try {
			bufferedReader.close();
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
