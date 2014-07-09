package com.drift.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.drift.core.DBConnector;
import com.drift.core.User;
import com.drift.servlet.MyServletUtil;

public class PhotoUtil {
	
	public static int downloadForeignPhoto(String address, String path, User user) {
		
		if(address == null || address.isEmpty() ||
				path == null || path.isEmpty() || user == null) {
			return DBConnector.DB_STATUS_ERR_BAD_ARGS;
		}
		
		int dbStatus = DBConnector.DB_STATUS_ERR_GENERIC;
		
		int uid = user.getUid();
		long currentTime = System.currentTimeMillis();
		String dateStr = MyServletUtil.timestampToDate(user.getRegisterTs());			
		String uploadPath = path + "/" + dateStr + "/" + uid; 
		File userpath = new File(uploadPath);
		System.out.println(userpath);
		if(!userpath.exists())
			userpath.mkdirs();
		String newFileName = Long.toString(currentTime)+".jpg";
		//System.out.println(newFileName);
		dbStatus = DBConnector.setPhoto(uid, newFileName);					
		urlDownload(address, uploadPath, newFileName);
		FileUtil.limitFiles(uploadPath);
		
		return dbStatus;		
	}
	
	private static void urlDownload(String urlAddress, String path, String filename) {
		
		OutputStream os = null;
		InputStream is = null;
		URLConnection uCon = null;
		
		System.out.println("Path is: " + path);
		System.out.println("File name is: " + filename);
		
		try {
			int byteRead, byteWritten = 0;
			URL url = new URL(urlAddress);
			byte[] buf = new byte[4096];
			os = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));

			uCon = url.openConnection();
			is = uCon.getInputStream();
			while ((byteRead = is.read(buf)) != -1) {
				os.write(buf,0,byteRead);
				byteWritten += byteRead;
			}
			System.out.println("File length: " + byteWritten);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int uploadPhoto(HttpServletRequest request, String path, int uid) {
		
		int dbStatus = DBConnector.DB_STATUS_ERR_GENERIC;
		
		if(uid <=0 || request == null || path == null || path.isEmpty()) {
			return DBConnector.DB_STATUS_ERR_BAD_ARGS;
		}
		
		File tempPathFile = new File(path+"/tmp");
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}
		
		try {
			User user = DBConnector.getUser(uid);
			
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
			factory.setRepository(tempPathFile);//设置缓冲区目录

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(524288);  // 设置最大文件尺寸，这里是512KB

			List<FileItem> items = upload.parseRequest(request);//得到所有的文件
			Iterator<FileItem> i = items.iterator();
			//PhotoHandler photoHandler = new LocalPhotoHandler();

			while (i.hasNext()) {
				//System.out.println("hasNext:");
				FileItem fi = (FileItem) i.next();
				String fileName = fi.getName();
				//System.out.println(fileName);
				if (fileName != null) {
					//File fullFile = new File(fi.getName());
					//System.out.println(fullFile);
					long currentTime = System.currentTimeMillis();
					//String newFileName = user.getEmail()+"_"+Long.toString(currentTime)+".jpg";
					String dateStr = MyServletUtil.timestampToDate(user.getRegisterTs());			
					String uploadPath = path + "/" + dateStr + "/" + uid; 
					File userpath = new File(uploadPath);
					System.out.println(userpath);
					if(!userpath.exists())
						userpath.mkdirs();
					String newFileName = Long.toString(currentTime)+".jpg";
					//System.out.println(newFileName);
					File savedFile = new File(userpath, newFileName);
					fi.write(savedFile);
					dbStatus = DBConnector.setPhoto(uid, newFileName);					
					FileUtil.limitFiles(uploadPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dbStatus;
	}
}
