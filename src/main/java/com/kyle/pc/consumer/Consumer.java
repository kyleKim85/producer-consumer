package com.kyle.pc.consumer;

import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.util.MessageUtils;

public abstract class Consumer implements Runnable{
	private final static Logger LOG = Logger.getLogger("Consumer");
	
	private final Broker broker;
	
	public Consumer(Broker broker) {
		this.broker = broker;
	}

	@Override
	public void run() {
		LOG.info("start " + Thread.currentThread().getName());
		try {
			while(true) {
				String data = receiveData();
				if(MessageUtils.isQuitMessage(data)) break;
				consume(data);
			}
		} catch (InterruptedException e) {
			LOG.severe("Consumer InterruptedException");
		}
		LOG.info("end " + Thread.currentThread().getName());
	}
	
	private String receiveData() throws InterruptedException {
		return broker.take();
	}
	
	protected abstract void consume(String data);

}
