package com.kyle.pc.broker;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueBroker implements Broker {
	
	private final ArrayBlockingQueue<String> queue;

	public ArrayBlockingQueueBroker(int capacity) {
		this.queue = new ArrayBlockingQueue<String>(capacity);
	}
	
	@Override
	public void put(String data) throws InterruptedException {
		this.queue.put(data);
	}

	@Override
	public String take() throws InterruptedException {
		return this.queue.take();
	}

}
