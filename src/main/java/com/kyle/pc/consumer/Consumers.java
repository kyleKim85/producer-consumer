package com.kyle.pc.consumer;

import com.kyle.pc.broker.Broker;

public class Consumers {
	public static Consumer newFileWriteConsumer(Broker broker) {
		return new FileWriteConsumer(broker); 
	}
}
