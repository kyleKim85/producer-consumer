package com.kyle.pc.distributor;

import java.util.ArrayList;
import java.util.HashMap;

import com.kyle.pc.broker.Broker;

public class PartitionDistributor extends Distributor{	
	private HashMap<Character, Integer> partitionMap;

	public PartitionDistributor(Broker producerBroker, ArrayList<Broker> consumerBrokers) {
		super(producerBroker, consumerBrokers);
	}

	@Override
	protected void setup() {
		this.partitionMap = createPartitionMap();
	}

	@Override
	protected void distribute(String data) throws InterruptedException {
		sendData(partitionMap.get(data.charAt(0)), data);
	}
	
	private HashMap<Character, Integer> createPartitionMap() {
		HashMap<Character, Integer> partitionMap = new HashMap<>();
		
		int consumerQueuesIndex = 0;
		for(int i = 'a' ; i <= 'z'; i++) {
			partitionMap.put((char)i, consumerQueuesIndex % consumerBrokerSize());
			consumerQueuesIndex++;
		}
		
		for(int i = 'A' ; i <= 'Z'; i++) {
			partitionMap.put((char)i, consumerQueuesIndex % consumerBrokerSize());
			consumerQueuesIndex++;
		}
		
		return partitionMap;
	}
}
