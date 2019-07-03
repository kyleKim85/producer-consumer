package com.kyle.pc.config;

public class Configuration {
	private static String readFile;
	private static int partition;
	private static String writeDirectory;
	private static int producerQueueSize;
	private static int consumerQueueSize;
	private static String quitMessage;
	
	private Configuration() {}
	
	public static String getReadFile() {
		return readFile;
	}
	
	public static void setReadFile(String file) {
		readFile = file;
	}
	
	public static int getPartition() {
		return partition;
	}
	
	public static void setPartition(int partitionValue) {
		partition = partitionValue;
	}
	
	public static String getWriteDirectory() {
		return writeDirectory;
	}
	
	public static void setWriteDirectory(String directory) {
		writeDirectory = directory;
	}

	public static int getProducerQueueSize() {
		return producerQueueSize;
	}
	
	public static void setProducerQueueSize(int queueSize) {
		producerQueueSize = queueSize;
	}

	public static int getConsumerQueueSize() {
		return consumerQueueSize;
	}
	
	public static void setConsumerQueueSize(int queueSize) {
		consumerQueueSize = queueSize;
	}
	
	public static String getQuitMessage() {
		return quitMessage;
	}
	
	public static void setQuitMessage(String message) {
		quitMessage = message;
	}
}
