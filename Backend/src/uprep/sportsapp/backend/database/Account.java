package uprep.sportsapp.backend.database;

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
	
	public Account(String name, String password, boolean admin) {
		this.name = name;
		this.password = password;
		this.admin = admin;
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
