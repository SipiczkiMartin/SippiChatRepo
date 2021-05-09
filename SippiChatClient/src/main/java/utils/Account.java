package utils;

import java.util.Collection;

public class Account {
	
	private String name;
	private String password;
	private Collection<Account> friends;
	
	public Account() {
		
	}
	
	public Account(String name,String pass) {
		this.name = name;
		this.password = pass;
	}
	
	public void addFriend(Account friend) {
		this.friends.add(friend);
	}
	
	public Collection getFriends() {
		return this.friends;
	}

}
