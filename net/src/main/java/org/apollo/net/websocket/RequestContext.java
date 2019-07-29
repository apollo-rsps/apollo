package org.apollo.net.websocket;

public class RequestContext {
	private boolean isWebsocketRequest = false;

	public boolean isWebsocketRequest() {
		return isWebsocketRequest;
	}

	public void setWebsocketRequest(boolean websocketRequest) {
		isWebsocketRequest = websocketRequest;
	}
}
