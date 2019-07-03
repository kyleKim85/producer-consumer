package com.kyle.pc.producer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.config.Configuration;

public class FileReadProducer extends Producer {
	private final static Logger LOG = Logger.getLogger("FileReadProducer");
	
	private final static String FILTER_PATTERN = "(^[a-zA-Z].*$)";

	public FileReadProducer(Broker broker) {
		super(broker);
	}

	@Override
	protected void produce() {
		try(BufferedReader br = new BufferedReader(new FileReader(Configuration.getReadFile()))) {
			String line;
			while((line = br.readLine()) != null) {
				if(Pattern.matches(FILTER_PATTERN, line)) {
					sendData(line);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.severe(e.getMessage());
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		} catch (InterruptedException e) {
			LOG.warning("Interrupted when produce!");
		}
	}

}
