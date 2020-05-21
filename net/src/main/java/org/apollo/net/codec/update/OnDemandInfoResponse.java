package org.apollo.net.codec.update;

public class OnDemandInfoResponse {

	private final int response;

	public OnDemandInfoResponse(int response) {
		this.response = response;
	}

	public int getResponse() {
		return response;
	}
}
