package utils;

import java.util.Collection;

public class Accounts {
	
	private Collection<Account> chatters;
	
	
	public Collection getAccounts() {
		return this.chatters;
	}
	
	public void addAccount(Account account) {
		this.chatters.add(account);
	}

}
