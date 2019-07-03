package com.kyle.pc.broker;

public class Brokers {
	public static Broker newArrayBlockingQueueBroker(int capacity) {
		return new ArrayBlockingQueueBroker(capacity);
	}
	public static Broker newLinkedBlockingQueueBroker(int capacity) {
		return new LinkedBlockingQueueBroker(capacity);
	}
}
