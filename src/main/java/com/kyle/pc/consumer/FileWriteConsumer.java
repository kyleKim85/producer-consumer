package com.kyle.pc.consumer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.config.Configuration;

public class FileWriteConsumer extends Consumer{
	private final static Logger LOG = Logger.getLogger("FileWriteConsumer");

	public FileWriteConsumer(Broker broker) {
		super(broker);
	}

	@Override
	protected void consume(String data) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(generateFileName(data), true))) {
			writer.append(data).append("\r\n");
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}
	
	private String generateFileName(String data) {
		return new StringBuilder().append(Configuration.getWriteDirectory()).append(String.valueOf(data.charAt(0))).append(".txt").toString().toLowerCase();
	}

}
