package com.drift.util;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;

import com.drift.core.User;

public class LocalPhotoHandler implements PhotoHandler {

	@Override
	public boolean savePhoto(FileItem item, User user, String path) {
		// TODO Auto-generated method stub
		if(item == null || user == null || path == null) {
			return false;
		}
		
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}



}
