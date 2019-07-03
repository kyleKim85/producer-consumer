package com.kyle.pc.broker;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueBroker implements Broker{
	
	private final LinkedBlockingQueue<String> queue;
	
	public LinkedBlockingQueueBroker(int capacity) {
		this.queue = new LinkedBlockingQueue<String>(capacity);
	}
	
	@Override
	public String take() throws InterruptedException {
		return this.queue.take();
	}

	@Override
	public void put(String data) throws InterruptedException {
		this.queue.put(data);
	}

}
