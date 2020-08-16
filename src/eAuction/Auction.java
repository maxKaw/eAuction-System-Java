package eAuction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import system.eAuctionSystem;

/**
 * This class stores all the data about an auction. It also uses the data
 * stored in the Seller class and the Item class.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public final class Auction implements Blockable, Serializable {
	private double startPrice;
	private double reservePrice;
	private LocalDateTime closeDate;
	private EnumStatus status;
	private Item item;
	private Seller seller;
	private LinkedList<Bid> bids = new LinkedList<Bid>();

	public Auction(double startPrice, double reservePrice, LocalDateTime closeDate, Item item, Seller seller)
			throws Exception {
		setCloseDate(closeDate);
		setReservePrice(reservePrice);
		setStartPrice(startPrice);
		setItem(item);
		setSeller(seller);
		setStatus(status.pending);
	}

	public Seller getSeller() {
		return this.seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public double getStartPrice() {
		return this.startPrice;
	}

	/**
	 * This method contains validation which ensures that the start price is not a negative number.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setStartPrice(double startPrice) throws Exception {
		if (startPrice >= 0) {
			this.startPrice = startPrice;
		} else {
			throw new Exception("Attempt to set negative start price (" + startPrice + ")!");
		}
	}

	public double getReservePrice() {
		return this.reservePrice;
	}

	/**
	 * This method contains validation to ensures that the reserve price is higher
	 * than the start price set by the user.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setReservePrice(double reservePrice) throws Exception {
		if (reservePrice >= this.startPrice) {
			this.reservePrice = reservePrice;
		} else {
			throw new Exception("Attempt to set reserve price (" + reservePrice + ") which is lower than start price ("
					+ this.startPrice + ")!");
		}

	}

	public synchronized LocalDateTime getCloseDate() {
		return this.closeDate;
	}

	/**
	 * This method calculates the upper increment from the
	 * current bid or start price if no bids are placed.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public double getUpperBiddingIncrement() {
		if (this.bids.isEmpty()) {
			return (getStartPrice() * 1.2);
		} else {
			return (this.bids.getLast().getAmount() * 1.2);
		}
	}

	/**
	 * This method calculates the upper increment from
	 * the current bid or start price if no bids are placed.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public double getLowerBiddingIncrement() {
		if (this.bids.isEmpty()) {
			return (getStartPrice() * 1.1);
		} else {
			return (this.bids.getLast().getAmount() * 1.1);
		}
	}

	/**
	 * This method contains some validation which allows setting 
	 * the close date at a maximum of 7 days from the start date.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setCloseDate(LocalDateTime closeDate) throws Exception {
		if (closeDate != null) {
			if (closeDate.isBefore(LocalDateTime.now().plusDays(7))) {
				this.closeDate = closeDate;
			} else {
				throw new Exception("Attempt to set close date (" + closeDate.toString()
						+ ") which is not 7 days after today's date!");
			}
		}
	}

	/**
	 * This method loops through all the buyers that have placed a bid on an item
	 * and haven't won it. It updates their list with lost auctions.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void informOutBid() {
		HashMap<String, Bid> user = new HashMap<String, Bid>();
		this.bids.stream().filter(p -> (!getLastBuyer().getUsername().equals(p.getWho().getUsername())))
				.forEach(p -> user.put(p.getWho().getUsername(), p));
		user.values().stream().forEach(p -> p.getWho().lost(this));
	}

	/**
	 * This method informs the buyer who placed the highest bid on an item in order to inform him
	 * that he is won by adding the item to his list of won auctions.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void informTheWinner() {
		this.bids.getLast().getWho().victory(this);
	}

	/**
	 * This method informs all the buyers that placed a bid that they
	 * lost because the reserve price was not met on the auction.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */

	public void informEveryBuyer() {
		HashMap<String, Bid> user = new HashMap<String, Bid>();
		this.bids.stream().forEach(p -> user.put(p.getWho().getUsername(), p));
		user.values().stream().forEach(p -> p.getWho().lost(this));
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public synchronized EnumStatus getStatus() {
		return this.status;
	}

	public void setStatus(EnumStatus status) {
		this.status = status;
	}

	/**
	 * This method adds the buyer's bid information to the list of bids.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void placeBid(double amount, Buyer who, LocalDateTime when) {
		this.bids.add(new Bid(amount, who, when));
	}

	public void verify() {
		setStatus(EnumStatus.started);
	}

	/**
	 * This method closes an auction which was active and executes methods to inform
	 * the winner, and people that lost.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	//
	public synchronized void close() {
		setStatus(EnumStatus.closed);
		if (getLastBid() != 0) {
			if (getReservePrice() > getLastBid()) {
				informEveryBuyer();
			} else {
				informOutBid();
				informTheWinner();
			}
		}
	}

	public Buyer getLastBuyer() {
		return this.bids.getLast().getWho();
	}

	public double getLastBid() {
		if (this.bids.isEmpty()) {
			return 0;
		}
		return this.bids.getLast().getAmount();
	}

	public synchronized Item getItem() {
		return this.item;
	}

	@Override
	public void setBlocked() {
		setStatus(EnumStatus.blocked);
	}

	@Override
	public boolean isBlocked() {
		return (getStatus() == EnumStatus.blocked ? true : false);
	}

}
