package org.eimsystems.chat;

public class TelException extends Exception {
	String cause;
	public TelException(String cause) {
		this.cause = cause;
	}
	private String getCauseString() {
		return cause;
	}
	private void setCause(String cause) {
		this.cause = cause;
	}
}
