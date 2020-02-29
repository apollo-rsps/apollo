package org.apollo.net.codec.update;

public class OnDemandEncryptionMessage {

	private final int key;

	public OnDemandEncryptionMessage(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}
}
