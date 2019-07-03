package com.kyle.pc.util;

import java.io.File;

public class FileUtils {
	public static boolean isFileExists(String path) {
		File file = new File(path);
	    return file.isFile();
	}
	
	public static boolean isNotFileExists(String path) {
	    return !isFileExists(path);
	}
	
	public static boolean isDirectoryExists(String path) {
		File directory = new File(path);
	    return directory.isDirectory();
	}

	public static boolean isNotDirectoryExists(String path) {
	    return !isDirectoryExists(path);
	}
}
