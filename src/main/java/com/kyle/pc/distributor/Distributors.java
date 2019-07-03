package com.kyle.pc.distributor;

import java.util.ArrayList;

import com.kyle.pc.broker.Broker;

public class Distributors {
	public static Distributor newPartitionDistributor(Broker producerBroker, ArrayList<Broker> consumerBrokers) {
		return new PartitionDistributor(producerBroker, consumerBrokers);
	}
}
