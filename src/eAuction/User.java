package eAuction;

import java.io.Serializable;

/**
 * The User class is an abstract class that contains the username and password
 * data for every buyer, seller and admin.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public abstract class User implements Serializable {
	protected String username;
	protected String password;

	public User(String username, String password) {
		setPassword(password);
		setUsername(username);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method makes sure that the password the user has entered is correct
	 * before granting them access to their menu to buy or sell. If the password is
	 * not correct then the user is rejected from logging in and informed of the
	 * mistake.
	 * 
	 * @param password - Password entered by the user
	 * @param boolean - Response whether the password entered is correct or not.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public boolean checkPassword(String password) {
		return (getPassword().equals(password) ? true : false);
	}
}
