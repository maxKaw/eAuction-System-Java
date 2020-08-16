package eAuction;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an extension of the abstract class User. This class inherits
 * the attributes from the user class and adds the 2 lists of auction objects.
 * One for won auctions, one for lost auctions.
 * 
 * @author Maksymilian Kawulam, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */

public final class Buyer extends User {

	List<Auction> wonAuctions = new ArrayList<Auction>();
	List<Auction> lostAuctions = new ArrayList<Auction>();

	public Buyer(String username, String password) {
		super(username, password);
	}

	/**
	 * This method is used to add an auction to the won auctions list stored in a
	 * buyer object.
	 * 
	 * @author Maksymilian Kawulam, Philip Hughes
	 */
	public void victory(Auction auction) {
		this.wonAuctions.add(auction);
	}

	/**
	 * This method is used to add an auction to the lost auctions list stored in a
	 * buyer object.
	 * 
	 * @author Maksymilian Kawulam, Philip Hughes
	 */
	public void lost(Auction auction) {
		this.lostAuctions.add(auction);
	}

	/**
	 * This method retrieves all of the won auctions stored in a buyer object.
	 * 
	 * @author Maksymilian Kawulam, Philip Hughes
	 */
	public List<Auction> getWonAuctions() {
		return this.wonAuctions;
	}

	/**
	 * This method retrieves all of the lost auctions stored in the list of a buyer
	 * object.
	 * 
	 * @author Maksymilian Kawulam, Philip Hughes
	 */
	public List<Auction> getLostAuctions() {
		return this.lostAuctions;
	}

}
