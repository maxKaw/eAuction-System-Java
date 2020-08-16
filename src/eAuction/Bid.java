package eAuction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This class stores all the attributes of each bid including the buyer's name
 * and amount of the bid.
 * 
 * @author Maksymilian Kawulam, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public class Bid implements Serializable {
	private double Amount;
	private Buyer who;
	private LocalDateTime when;

	public Bid(double amount, Buyer who, LocalDateTime when) {
		setAmount(amount);
		setWhen(when);
		setWho(who);
	}

	public double getAmount() {
		return this.Amount;
	}

	public void setAmount(double amount) {
		this.Amount = amount;
	}

	public Buyer getWho() {
		return this.who;
	}

	public void setWho(Buyer who) {
		this.who = who;
	}

	public LocalDateTime getWhen() {
		return this.when;
	}

	public void setWhen(LocalDateTime when) {
		this.when = when;
	}

}
