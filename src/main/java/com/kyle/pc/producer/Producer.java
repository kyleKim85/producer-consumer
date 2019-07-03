package com.kyle.pc.producer;

import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.util.MessageUtils;

public abstract class Producer implements Runnable {
	private final static Logger LOG = Logger.getLogger("Producer");

	
	private final Broker broker;
	
	public Producer(Broker broker) {
		this.broker = broker;
	}

	@Override
	public void run() {
		LOG.info("start " + Thread.currentThread().getName());
		produce();
		try {
			sendQuitMessage();
		} catch (InterruptedException e) {
			LOG.severe("Interruped when shutdown!");
		}
		LOG.info("end " + Thread.currentThread().getName());
	}
	
	protected void sendData(String data) throws InterruptedException {
		this.broker.put(data);
	}
	
	private void sendQuitMessage() throws InterruptedException {
		sendData(MessageUtils.getQuitMessage());
	}
	
	protected abstract void produce();

}
