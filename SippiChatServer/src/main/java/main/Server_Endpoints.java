package main;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import database.DBConnection;
import help.DatabaseLike;
import utilities.Communication;
import utilities.CommunicationList;
import utilities.FriendsList;
import utilities.Login;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@Controller
@RequestMapping(value = "/chat")
public class Server_Endpoints {
	
	private ArrayList<DatabaseLike> clients = new ArrayList<>();
	
	private DBConnection dbconnection;
	
	
	
	/**
	 * Login expects xml data from Login class...Need jar
	 * @param login
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Communication login( @RequestBody Login login) {
		
		dbconnection = new DBConnection();
		
		int answer = dbconnection.login(login.getName(), login.getPassword());
		
		Communication c = null;
		Exception e = null;
		
		switch(answer) {
		case(0):
			e = new Exception("user does not exist");
			c = new Communication(e);
			return c;
		case(1):
			c = new Communication();
			return c;
		case(2):
			e = new Exception("incorrect password");
			c = new Communication(e);
			return c;
		case(3):
			e = new Exception("something went totaly wrong in db");
			c = new Communication(e);
			return c;
		
		}
		
		return c;
	}
	
	@RequestMapping(value = "/newUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Communication addUser( @RequestBody Login login) {
		dbconnection = new DBConnection();		
		
		boolean result = dbconnection.addUserToDB(login.getName(), login.getPassword());
		
		if(result) {
			return new Communication();
		}else {
			Exception e = new Exception("User already exists");
			return new Communication(e);
		}
	}
	
	/**
	 * Login with GET Method....Used for testing purposes only!!!
	 * @param name
	 * @param pass
	 * @return
	 */
	@RequestMapping(value = "/{name}/{pass}/login", method = RequestMethod.GET,produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Communication login(@PathVariable String name, @PathVariable String pass) {
		Communication c = null;
		this.clients.add(new DatabaseLike("Martin", "Martin"));
		this.clients.add(new DatabaseLike("Bobo", "Bobo"));
		
		for(DatabaseLike d : this.clients) {
			if(d.getName().contentEquals(name) && d.getPassword().contentEquals(pass)) {
				c = new Communication();
				return c;
			}
		}
		
		
		Exception e = new Exception("incorrect name or password");
		c = new Communication(e);
			
		return c;
		
	}
	
	@RequestMapping(value = "/tryout", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody CommunicationList getComms() {
		CommunicationList communications = new CommunicationList();
		
		communications.addComm(new Communication("Martin", "Peter", "Hi"));
		communications.addComm(new Communication("bobo", "Martin", "Hello there"));
		
		return communications;
	}
	
	/**
	 * for sending messages to server
	 * @param comms
	 */
	
	@RequestMapping(value = "/send", method = RequestMethod.POST,consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Communication sendMessage( @RequestBody Communication comms) {
		dbconnection = new DBConnection();
		
		boolean result = dbconnection.addMessage(comms.getSenderName(), comms.getReceiverName(), comms.getMessage());
		
		if(result) {
			return new Communication();
		}else {
			Exception e = new Exception("Some error occured in database");
			return new Communication(e);
		}
	
	}
	
	/**
	 * for now only one client can check in and only one message is send back
	 * @return
	 */
	@RequestMapping(value = "/{receiver}/check", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody CommunicationList getMessage(@PathVariable String receiver) { //need list wrapper for more communications
		dbconnection = new DBConnection();
		CommunicationList comms = new CommunicationList();
		
		ArrayList<Communication> messages = dbconnection.getMessages(receiver);
		if(messages != null && messages.size() > 0) {
			for(Communication c : messages) {
				comms.addComm(c);
			}
		}else if(messages.size() == 0) {
			comms.addComm(new Communication(new Exception("No new messages")));
		}else {
			comms.addComm(new Communication(new Exception("Error occured")));
		}
		
		return comms;
	}
	
	@RequestMapping(value = "/addFriend", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Communication addFriend( @RequestBody Communication friend) {
		dbconnection = new DBConnection();
		
		boolean result = dbconnection.addFriendToDB(friend.getSenderName(), friend.getReceiverName());
		
		if(result) {
			return new Communication();
		}else {
			return new Communication(new Exception("Something wrong in server addFriend"));
		}
		
	}
	
	@RequestMapping(value = "/{user}/getFriends", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody FriendsList getFriends(@PathVariable String user) {
		dbconnection = new DBConnection();
		FriendsList friends = new FriendsList();
		
		friends.serFriends(dbconnection.getFriends(user));
		
		return friends;
	}
	
	@RequestMapping(value = "/{user}/disconnect", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void disconnectFromServer(@PathVariable String user) {
		dbconnection = new DBConnection();
		dbconnection.disconnected(user);
	}
	
}
