package com.drift.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileUtil {
	
	private static final int MAX_PHOTO_COUNT = 5;
	
	public static void limitFiles(String pathname) {
		File path = new File(pathname);
		File[] files = path.listFiles();
		if(files.length > MAX_PHOTO_COUNT) {
			Arrays.sort(files, new FileComparator());
			int remove_count = files.length - MAX_PHOTO_COUNT;
			for(int i=0; i<remove_count ; i++) {
				if(files[i].isDirectory()) {
					System.out.println("Directory: " + files[i].getName());
				} else {
					System.out.println("Remove file: " + files[i].getName());
					files[i].delete();
				}
			}
		}
	}
}

class FileComparator implements Comparator<File> {
	
	public final int compare(File file1, File file2) {
		
		long diff = file1.lastModified() - file2.lastModified();
		if(diff > 0)
			return 1;
		else if(diff < 0)
			return -1;
		else {
			return 0;
		}
	}
	
}