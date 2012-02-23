package org.jooq.debugger.console.remote;

/**
 * @author Christopher Deckers
 */
public class ServerLoggingActivationMessage implements Message {

	private boolean isLogging;
	
	public ServerLoggingActivationMessage(boolean isLogging) {
		this.isLogging = isLogging;
	}
	
	public boolean isLogging() {
		return isLogging;
	}
	
}
