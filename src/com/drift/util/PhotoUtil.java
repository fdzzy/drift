package com.drift.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.drift.core.DBConnector;
import com.drift.core.User;
import com.drift.servlet.MyServletUtil;

public class PhotoUtil {
	
	public static int uploadPhoto(HttpServletRequest request, String path, int uid) {
		
		int dbStatus = DBConnector.DB_STATUS_ERR_GENERIC;
		
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
