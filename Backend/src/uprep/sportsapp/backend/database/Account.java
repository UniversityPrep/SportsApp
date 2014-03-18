package uprep.sportsapp.backend.database;

import java.security.MessageDigest;
import java.security.SecureRandom;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "accounts")
public class Account {
	
	@DatabaseField (id = true)
	private String name;
	
	@DatabaseField (canBeNull = false)
	private String password;
	
	@DatabaseField (canBeNull = false)
	private boolean admin;
	
	@DatabaseField (canBeNull = false)
	private byte[] passwordSalt;
	
	public Account(String name, String password, boolean admin) {
		this.name = name;
		this.admin = admin;
		this.setPassword(password);
	}
	
	public void setPassword(String password) {
		this.passwordSalt = this.generateRandomSalt();
		this.password = hashPass(password + passwordSalt).toString();
	}
	
	private byte[] hashPass(String password) { 
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(password.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private byte[] generateRandomSalt() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return bytes;
	}
	
	/**
	 * It is highly recommended not to use this.
	 * This constructor is only present because of the
	 * requirements of ORMLite
	 */
	Account() {
		this("_name", "_secret", false);
	}
	
}
