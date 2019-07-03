package com.kyle.pc.distributor;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.kyle.pc.broker.Broker;
import com.kyle.pc.util.MessageUtils;

public abstract class Distributor implements Runnable {
	private final static Logger LOG = Logger.getLogger("Distributor");
	
	private final Broker producerBroker;
	private final ArrayList<Broker> consumerBrokers;
	
	public Distributor(Broker producerBroker, ArrayList<Broker> consumerBrokers) {
		this.producerBroker = producerBroker;
		this.consumerBrokers = consumerBrokers;
	}
	
	public int consumerBrokerSize() {
		return this.consumerBrokers.size();
	}

	@Override
	public void run() {
		LOG.info("start " + Thread.currentThread().getName());
		if(!isValidate()) {
			LOG.info("end " + Thread.currentThread().getName());
			return;
		}
		
		setup();
		
		try {
			while(true) {
					String data = receiveData();
					if(MessageUtils.isQuitMessage(data)) break;
					distribute(data);
			}
		} catch (InterruptedException e) {
			LOG.warning("Interruped when distribute!");
		}
		
		try {
			sendQuitMessage();
		} catch (InterruptedException e) {
			LOG.severe("Interruped when shutdown!");
		}
		LOG.info("end " + Thread.currentThread().getName());
	}
	
	protected void sendData(int consumerIndex, String data) throws InterruptedException {
		this.consumerBrokers.get(consumerIndex).put(data);
	}
	
	private void sendQuitMessage() throws InterruptedException {
		for(int i = 0; i < consumerBrokerSize(); i++) {
			sendData(i, MessageUtils.getQuitMessage());
		}
	}
	
	

	protected boolean isValidate() {
		if(this.producerBroker == null) {
			LOG.warning("No have producer queue!");
			return false;
		}
		
		if(this.consumerBrokers.size() == 0) {
			LOG.warning("No have consumer queues!");
			return false;
		}
		
		return true;
	}
	
	protected abstract void setup();
	protected abstract void distribute(String data) throws InterruptedException;
	
	private String receiveData() throws InterruptedException {
		return this.producerBroker.take();
	}
}
