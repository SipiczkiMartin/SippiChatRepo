package help;

public class DatabaseLike {

	private String name;
	private String password;
	
	public DatabaseLike(String name,String pass) {
		this.name = name;
		this.password = pass;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
