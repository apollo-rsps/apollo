package org.apollo.net.codec.update;

public class OnDemandInfo {

	private final long releaseNumber;

	public OnDemandInfo(long releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public long getReleaseNumber() {
		return releaseNumber;
	}
}
