package com.drift.util;

import org.apache.commons.fileupload.FileItem;

import com.drift.core.User;

/*
 * This is an interface to handle the storage and request for a user Photo
 */
public interface PhotoHandler {

	public boolean savePhoto(FileItem item, User user, String path);
}
