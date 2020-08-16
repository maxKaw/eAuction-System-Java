package eAuction;

/**
 * This class is an extension of the abstract class User. This class inherits
 * all the attributes stored in the User class. This allows an Admin user to
 * have a username and a password without having to duplicate the code used in
 * the User class.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2020-03-15 
 */
public class Admin extends User {
	public Admin(String username, String password) {
		super(username, password);
	}

}
