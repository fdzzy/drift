package com.drift.util;

import org.apache.commons.fileupload.FileItem;

import com.drift.core.User;

public class LocalPhotoHandler implements PhotoHandler {

	@Override
	public boolean savePhoto(FileItem item, User user, String path) {
		if(item == null || user == null || path == null) {
			return false;
		}
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return true;
	}



}
