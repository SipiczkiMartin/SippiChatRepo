package utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "login")
@XmlAccessorType(XmlAccessType.NONE)
public class Login {
	
	@XmlElement(name = "loginName")
	private final String name;
	
	@XmlElement(name = "loginPassword")
	private final String password;
	
	public Login() {
		name = null;
		password = null;
	}
	
	public Login(String name, String pass) {
		this.name = name;
		this.password = pass;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	
	
	
	

}
