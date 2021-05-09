package networking;

import java.net.URL;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import utilities.Communication;
import utilities.CommunicationList;
import utilities.Friend;
import utilities.FriendsList;
import utilities.Login;
import utilities.State;

public class NetworkComm {
	
	private String serverBaseUrl;
	
	private RestTemplate rest = new RestTemplate();
	
	public NetworkComm(String serverUrl) {
		this.serverBaseUrl = serverUrl;
	}
	
	public boolean login(String name, String pass) {
		try {
			URL url = new URL(serverBaseUrl + "/login");
			Login log = new Login(name, pass);
			
			Communication result = rest.postForObject(url.toURI(), log, Communication.class);
			
			if(result.getState() == State.Okay) {
				System.out.println("FUCK ME IT WORKS");
				return true;
			}else {
				System.out.println(result.getExceptionMessage());
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean send(String sender,String receiver, String message) {
		Communication c = new Communication(sender, receiver, message);
		try {
			URL url = new URL(serverBaseUrl + "/send");
			
			Communication result = rest.postForObject(url.toURI(), c, Communication.class);
			
			if(result.getState() == State.Okay) {
				return true;
			}else {
				return false;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Communication> check(String receiver) {
		ArrayList<Communication> comms = new ArrayList<>();
		try {
			URL url = new URL(serverBaseUrl + "/" + receiver + "/check");
			
			CommunicationList result = rest.getForObject(url.toURI(), CommunicationList.class);
			
			comms = result.getComms();
			
			
			return comms;
		}catch(Exception e) {
			comms.add(new Communication(e));
			return comms;
		}
	}
	
	public boolean addFriend(String user, String name) {
		try {
			URL url = new URL(serverBaseUrl + "/addFriend");
			
			Communication c = new Communication(user, name, "New Friend");
			
			Communication result = rest.postForObject(url.toURI(), c, Communication.class);
			
			if(result.getState() == State.Okay) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
	
	public ArrayList<Friend> getFriends(String user){
		try {
			URL url = new URL(serverBaseUrl + "/" + user + "/getFriends");
			
			FriendsList result = rest.getForObject(url.toURI(), FriendsList.class);
			
			if(result.getFriends().size() > 0 && result.getFriends() != null) {
				return result.getFriends();
			}else {
				return null;
			}
			
		}catch(Exception e) {
			return null;
		}
	}
	
	public boolean newUser(Login login) {
		try {
			URL url = new URL(serverBaseUrl + "/newUser");
			
			Communication c = rest.postForObject(url.toURI(), login, Communication.class);
			
			if(c.getState() == State.Okay) {
				return true;
			}else {
				return false;
			}
			//can return boolean if needed...
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void disconnectFromServer(String user) {
		try {
			URL url = new URL(serverBaseUrl + "/" + user + "/disconnect");
			
			rest.getForObject(url.toURI(), HttpStatus.class);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
