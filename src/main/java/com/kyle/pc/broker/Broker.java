package com.kyle.pc.broker;

public interface Broker {
	public String take() throws InterruptedException;
	public void put(String data) throws InterruptedException;
}
