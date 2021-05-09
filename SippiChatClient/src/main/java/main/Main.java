package main;
import frontend.GUI;

public class Main {

	private static GUI gui;
	private static final String serverUrl = "http://192.168.1.103:13334/chat";
	//private static final String serverUrl = "http://192.168.1.103:13334/chat";
	
	public static void main(String[] args) throws InterruptedException {
	
		gui = new GUI(serverUrl);
		
		GUI.login();
		
		new Thread(new Runnable() {
		    public void run() {
		    	checkMessages();
		    }
		}).start();
			

		new Thread(new Runnable() {
		    public void run() {
		    	updateNames();
		    }
		}).start();
	}


	private static void checkMessages() {
		while(true) {
			gui.checkForMessages();
			System.out.println("In check messages");
			try {
				Thread.sleep(2000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void updateNames() {
		while(true) {
			gui.updateNames();
			System.out.println("In update names");
			try {
				Thread.sleep(10000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
