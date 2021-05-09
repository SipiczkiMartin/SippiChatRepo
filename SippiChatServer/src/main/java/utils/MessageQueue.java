package utils;

import java.util.ArrayDeque;
import java.util.Deque;

public class MessageQueue {
	
	private Deque<Communication> messages;
	
	public MessageQueue() {
		this.messages = new ArrayDeque<Communication>();
	}
	
	public void addMessage(Communication message) {
		this.messages.add(message);
	}
	
	public Communication getMessage(String receiver) {//can rewrite to compare senders and/or getters of messages here if needed
		Communication c = messages.element();
		if(c.getReceiverName().contentEquals(receiver)) {
			Communication c1 = c;
			messages.remove(c);
			return c1;
		}else {
			return null;
		}
	}

}
