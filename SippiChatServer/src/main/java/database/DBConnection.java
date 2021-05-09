package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import utilities.Communication;
import utilities.Friend;
import utilities.State;


public class DBConnection {
	
	private static Connection conn;
	
	public DBConnection() {
		try {
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sippichatdb", "root", "Mikulas91");	
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
	}
	
	public int login(String name,String password) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ResultSet result = stmt.executeQuery("SELECT * FROM user WHERE name = \"" + name +"\"");
			
			if(!result.next()) {
				return 0; //for user doesn't exist
			}else {
				if(result.getString("password").contentEquals(password)) {
					result.updateString("status", "online");
					result.updateRow();
					return 1; //for ok
				}else {
					return 2; //for password not correct
				}
			}
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
		return 3; //total fuck up
	}
	
	public boolean addUserToDB(String name,String password) { 
		boolean check = this.checkUser(name);
		
		if(!check) {
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (name,password) VALUES (?,?)");
				stmt.setString(1, name);
				stmt.setString(2, password);
				
				stmt.execute();
				stmt.close();
				
				return true;
			}catch(SQLException e) {
				System.out.println("SQLExcepton: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());			
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
		
	}
	
	public boolean addMessage(String sender, String receiver, String message) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO message (sender,receiver,message_text,is_read) VALUES (?,?,?,0)");
			
			stmt.setString(1, sender);
			stmt.setString(2, receiver);
			stmt.setString(3, message);
			
			boolean result = stmt.execute();
			stmt.close();
			
			return result;
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Communication> getMessages(String receiver){
		ArrayList<Communication> messages = new ArrayList<>();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			ResultSet result = stmt.executeQuery("SELECT * FROM message WHERE receiver = \"" + receiver +"\" AND is_read = 0");
			
			while(result.next()) {
				Communication c = new Communication(result.getString("sender"), result.getString("receiver"), result.getString("message_text"));
				messages.add(c);
				result.updateInt( "is_read", 1);
	            result.updateRow();
			}
			
			return messages;
			
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public boolean addFriendToDB(String user, String friend) {
		if(checkUser(user)) {
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO friend (user1,user2) VALUES(?,?)");
				stmt.setString(1, user);
				stmt.setString(2, friend);
				
				boolean result = stmt.execute();
				stmt.close();
				
				if(result) {
					return true;
				}else {
					return false;
				}
				
			}catch(SQLException e) {
				System.out.println("SQLExcepton: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());			
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
	}
	
	private boolean checkUser(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM user WHERE name = ?");
			stmt.setString(1, name);
			
			ResultSet set = stmt.executeQuery();
			
			return set.next();
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Friend> getFriends(String name){
		ArrayList<Friend> friends = null;
		try {
			friends = new ArrayList<>();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT user2 FROM friend WHERE user1 = ?");
			stmt.setString(1, name);
			
			ResultSet result = stmt.executeQuery();
			
			while(result.next()) {
				
				PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM user WHERE name = ?");
				stmt2.setString(1, result.getString("user2"));
				
				ResultSet finalResult = stmt2.executeQuery();
				
				while(finalResult.next()) {
					if(finalResult.getString("status").contentEquals("online")) {
						friends.add(new Friend(finalResult.getString("name"), State.Online));
					}else {
						friends.add(new Friend(finalResult.getString("name"), State.Offline));
					}
				}
				
			}
			
			return friends;
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
			return null;
		}
	}
	
	public void disconnected(String user) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			ResultSet result = stmt.executeQuery("SELECT * FROM user WHERE name = \"" + user + "\"");
			
			while(result.next()) {
				result.updateString("status", "offline");
				result.updateRow();
			}
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////try querries/////////////////////////////////////////////////////////////////////
	
	public void selectQuerry() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("SELECT name FROM user");
			
			while(result.next()) {
				System.out.println("Name: " + result.getString("name"));
			}
			
			result.close();
			stmt.close();
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
	}
	
	public void insertQuerry() {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (name,password) VALUES (?,?)");
			stmt.setString(1,"Martin");
			stmt.setString(2, "Martin");
			
			stmt.execute();
			stmt.close();
			
			System.out.println("Executed");
		}catch(SQLException e) {
			System.out.println("SQLExcepton: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());			
			e.printStackTrace();
		}
	}
}
