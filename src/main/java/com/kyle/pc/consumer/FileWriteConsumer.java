package com.kyle.pc.consumer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.config.Configuration;

public class FileWriteConsumer extends Consumer{
	private final static Logger LOG = Logger.getLogger("FileWriteConsumer");
	
	private final static int GAB_LOWER_UPPER = 'A' - 'a';
	private final HashMap<Character, BufferedWriter> writerMap;

	public FileWriteConsumer(Broker broker) {
		super(broker);
		this.writerMap = new HashMap<>();
	}

	@Override
	protected void consume(String data) {
		try {
			BufferedWriter writer = getBufferedWriter(data);
			writer.append(data).append("\r\n");
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}
	
	private String generateFileName(String data) {
		return new StringBuilder().append(Configuration.getWriteDirectory()).append(String.valueOf(data.charAt(0))).append(".txt").toString().toLowerCase();
	}
	
	private BufferedWriter getBufferedWriter(String data) throws IOException {
		char key = data.charAt(0);
		if(key >= 'A') {
			key = (char)(key - GAB_LOWER_UPPER);
		}
		BufferedWriter writer = writerMap.get(key);
		if(writer == null) {
			writer = new BufferedWriter(new FileWriter(generateFileName(data), true));
			writerMap.put(key, writer);
		}
		return writer;
	}

	@Override
	protected void onDestroy() {
		for(BufferedWriter writer : writerMap.values()) {
			try (BufferedWriter r = writer){
				writer.close();
			} catch (IOException e) {
				LOG.severe(e.getMessage());
			}
		}
	}

}
