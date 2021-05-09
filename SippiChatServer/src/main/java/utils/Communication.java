package utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "communication")
@XmlAccessorType(XmlAccessType.NONE)
public class Communication {
	
	@XmlElement(name = "senderName", required = true)
	private final String senderName;
	
	
	@XmlElement(name = "receiverName", required = true)
	private final String receiverName;
	
	@XmlElement(name = "message", required = true)
	private final String message;
	
	@XmlElement(name = "exceptionMessage")
	private final String exceptionMessage;
	
	@XmlElement(name = "state", required = true)
	private final State state;
	
	public Communication() {
		state = State.Okay;
		this.senderName = null;
		this.receiverName = null;
		this.message = null;
		
		this.exceptionMessage = "nothing to report";
	}
	
	public Communication(String sender,String receiver, String message) {
		this.state = State.Okay;
		this.senderName = sender;
		this.receiverName = receiver;
		this.message = message;
		
		this.exceptionMessage = "Nothing to report";
	}
	
	
	public Communication(Exception e) {
		this.state = State.Error;
		this.senderName = null;
		this.receiverName = null;
		this.message = null;
		this.exceptionMessage = e.getMessage();
	}

	

	public String getSenderName() {
		return senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public String getMessage() {
		return message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public State getState() {
		return state;
	}
	
	
	
	

}
