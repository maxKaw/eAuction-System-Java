package eAuction;

import java.util.ArrayList;
import java.util.List;

/**
 * The seller class extends the user class and inherits the data stored in the
 * user class. Seller also contains an Enum status and a list of items they are
 * going to auction off.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */

public final class Seller extends User implements Blockable {

	private List<Item> items = new ArrayList<Item>();
	private EnumStatus status;

	public Seller(String username, String password) {
		super(username, password);
		setActive();
	}

	/**
	 * This method adds a new item to the collection of Items.
	 * 
	 * @param newItem - Item object due to be added to the seller's list of items
	 */
	public void addItem(Item newItem) {
		items.add(newItem);
	}

	/**
	 * This method allows the user to see the name of any of the items stored in the
	 * item list for the seller object.
	 * 
	 * @param name - Item that the user wants to see.
	 * @return Item - Item object reflecting the provided name or null
	 */
	public Item getItemName(String name) {
		return items.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	/**
	 * This method returns every item object stored in the list for the Seller
	 * Object.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	*/
	public List<Item> getItems() {
		return items;
	}

	/**
	 * This method allows the Admin account to check the status of the sellers account
	 * whether the seller can set an auction or not.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	@Override
	public boolean isBlocked() {
		if (status == EnumStatus.blocked) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method allows the Admin account to block sellers from setting
	 * auctions.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	@Override
	public void setBlocked() {
		this.status = EnumStatus.blocked;
	}
	
	/**
	 * This method allows the Admin account to unblock sellers from setting
	 * auctions.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setActive() {
		this.status = EnumStatus.active;
	}
}
