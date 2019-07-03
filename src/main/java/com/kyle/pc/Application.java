package com.kyle.pc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.broker.Brokers;
import com.kyle.pc.config.Configuration;
import com.kyle.pc.consumer.Consumers;
import com.kyle.pc.distributor.Distributors;
import com.kyle.pc.producer.Producers;
import com.kyle.pc.util.FileUtils;

public class Application {
	private final static Logger LOG = Logger.getLogger("Application");

	private final static int MIN_PARTITION = 2;
	private final static int MAX_PARTITION = 26;

	private final static String PROPS_PRODUCER_QUEUE_SIZE_NAME = "producerQueueSize";
	private final static String PROPS_CONSUMER_QUEUE_SIZE_NAME = "consumerQueueSize";
	private final static String PROPS_QUIT_MESSAGE_NAME = "quitMessage";

	public static void main(String[] args) {
		Application app = new Application();
		app.run(args);
	}

	public void run(String[] args) {
		if (!initialize(args))
			return;

		Broker producerBroker = Brokers.newLinkedBlockingQueueBroker(Configuration.getProducerQueueSize());
		ArrayList<Broker> consumerBrokers = new ArrayList<>();

		runConsumers(consumerBrokers);
		runProducer(producerBroker);
		runDistributor(producerBroker, consumerBrokers);
	}

	private void runProducer(Broker producerBroker) {
		Thread threadP = new Thread(Producers.newFileReadProducer(producerBroker), "Producer");
		threadP.start();
	}

	private void runConsumers(ArrayList<Broker> consumerBrokers) {
		ThreadGroup consumerThreads = new ThreadGroup("Consumers");
		for (int i = 0; i < Configuration.getPartition(); i++) {
			Broker consumerBroker = Brokers.newLinkedBlockingQueueBroker(Configuration.getConsumerQueueSize());
			consumerBrokers.add(consumerBroker);
			Thread threadC = new Thread(consumerThreads, Consumers.newFileWriteConsumer(consumerBroker),
					"Consumer" + i);
			threadC.start();
		}
	}

	private void runDistributor(Broker producerBroker, ArrayList<Broker> consumerBrokers) {
		Thread threadD = new Thread(Distributors.newPartitionDistributor(producerBroker, consumerBrokers),
				"PartitionDistributor");
		threadD.start();
	}

	private boolean initialize(String[] args) {
		if (isNotHaveThreeArguments(args))
			return false;
		if (isNotExistsReadFile(args[0]))
			return false;
		if (isNotExistsWriteDirectory(args[1]))
			return false;
		if (isInvalidPartition(args[2]))
			return false;

		setConfigurationByArguments(args);

		Properties prop = readProperties();
		if (prop == null)
			return false;
		if (isInvalidProducerQueueSize(prop.getProperty(PROPS_PRODUCER_QUEUE_SIZE_NAME)))
			;
		if (isInvalidConsumerQueueSize(prop.getProperty(PROPS_CONSUMER_QUEUE_SIZE_NAME)))
			;

		setConfigurationByProperties(prop);

		return true;
	}

	private boolean isNotHaveThreeArguments(String[] args) {
		if (args.length != 3) {
			LOG.severe("Three arguments are required. [readFile, writeDirectory, partition]");
			return true;
		}
		return false;
	}

	private boolean isNotExistsReadFile(String value) {
		if (FileUtils.isNotFileExists(value)) {
			LOG.severe("File not found : " + value);
			return true;
		}
		return false;
	}

	private boolean isNotExistsWriteDirectory(String value) {
		if (FileUtils.isNotDirectoryExists(value)) {
			LOG.severe("Write directory not found : " + value);
			return true;
		}
		return false;
	}

	private boolean isInvalidPartition(String value) {
		try {
			int partition = Integer.parseInt(value);
			if (partition < MIN_PARTITION) {
				LOG.warning("partition value is too small. The value must be greater than or equal to " + MIN_PARTITION
						+ ".");
				return true;
			} else if (partition > MAX_PARTITION) {
				LOG.warning(
						"partition value is too large. The value must be less than or equal to " + MAX_PARTITION + ".");
				return true;
			}
		} catch (NumberFormatException e) {
			LOG.severe("partition value ​​is numeric only.");
			return true;
		}
		return false;
	}

	private void setConfigurationByArguments(String[] args) {
		Configuration.setReadFile(args[0]);
		Configuration.setWriteDirectory(args[1]);
		Configuration.setPartition(Integer.parseInt(args[2]));
	}

	private Properties readProperties() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("config/config.properties");
		String path;
		if(url == null) {
			path = "config/config.properties";
		} else {
			path = url.getPath();
		}
		LOG.info("read properties from " + path);
		Properties properties = new Properties();
		try (FileInputStream stream = new FileInputStream(path)){
			properties.load(stream);
		} catch (FileNotFoundException fnfe) {
			LOG.severe("Cannot found properties file : " + path);
			return null;
		} catch (IOException e) {
			LOG.severe("Cannot read properties file : " + path);
			return null;
		}
		return properties;
	}

	private boolean isInvalidProducerQueueSize(String queueSize) {
		try {
			int producerQueueSize = Integer.parseInt(queueSize);
			if (producerQueueSize < 1) {
				LOG.warning("producerQueueSize value is too small. The value must be greater than or equal to 1");
				return true;
			}
		} catch (NumberFormatException nfe) {
			LOG.severe("producerQueueSize value is numeric only");
			return true;
		}
		return false;
	}

	private boolean isInvalidConsumerQueueSize(String queueSize) {
		try {
			int consumerQueueSize = Integer.parseInt(queueSize);
			if (consumerQueueSize < 1) {
				LOG.warning("consumerQueueSize value is too small. The value must be greater than or equal to 1");
				return true;
			}
		} catch (NumberFormatException nfe) {
			LOG.severe("consumerQueueSize value is numeric only");
			return true;
		}
		return false;
	}

	private void setConfigurationByProperties(Properties properties) {
		Configuration.setProducerQueueSize(Integer.parseInt(properties.getProperty(PROPS_PRODUCER_QUEUE_SIZE_NAME)));
		Configuration.setConsumerQueueSize(Integer.parseInt(properties.getProperty(PROPS_CONSUMER_QUEUE_SIZE_NAME)));
		Configuration.setQuitMessage(properties.getProperty(PROPS_QUIT_MESSAGE_NAME));
	}
}