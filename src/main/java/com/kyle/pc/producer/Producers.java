package com.kyle.pc.producer;

import com.kyle.pc.broker.Broker;

public class Producers {
	public static Producer newFileReadProducer(Broker broker) {
		return new FileReadProducer(broker);
	}
}
