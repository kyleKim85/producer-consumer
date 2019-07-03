package com.kyle.pc.util;

import com.kyle.pc.config.Configuration;

public class MessageUtils {
	public static boolean isQuitMessage(String data) {
		return getQuitMessage().equals(data);
	}
	
	public static String getQuitMessage() {
		return Configuration.getQuitMessage();
	}
}
