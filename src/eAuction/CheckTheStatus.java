package eAuction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for checking the auctions in accordance with the current time
 * and finishing them when the close time is reached.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public class CheckTheStatus implements Runnable {

	private List<Auction> auctions;
	private int time;

	public CheckTheStatus(List<Auction> auctions, int secs) {
		setAuctions(auctions);
		setTime(secs * 10);
	}

	/**
	 * This method is used to get all of the auctions stored in the List of Auction
	 * objects.
	 * 
	 * @return List<Auction> - List of the auctions.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public List<Auction> getAuctions() {
		return this.auctions;
	}

	
	/**
	 * This method is used to add an list of Auction objects.
	 * 
	 * @param  List<Auction> - List of the auctions for the future use.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setAuctions(List<Auction> auctions) {
		this.auctions = auctions;
	}

	/**
	 * This method is used to set the time duration of the thread sleep.
	 * 
	 * @param time - Time to be set in seconds.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * This method is used to retrieve the time duration of the thread sleep.
	 * 
	 * @return time - Time in seconds. 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public int getTime() {
		return this.time / 10;
	}

	/**
	 * This method is used to close an auction when an auction time has reached 0.
	 * This informs a user that the auction has finished by printing the auction
	 * ending to the console. The close method will also be run from this method for
	 * the particular auction.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public synchronized void finishAuction(Auction a) {
		a.close();
		System.err.println();
		System.err.println("---------------------------");
		System.err.println("Auction with item " + a.getItem().getName() + " is closed!");
		System.err.println("---------------------------");
		activeAuctions();
	}

	/**
	 * This method checks whether there are some active auctions and displays them when found any.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public synchronized void activeAuctions() {
		List<Auction> items = auctions.stream().filter(p -> p.getStatus().equals(EnumStatus.started))
				.filter(p -> p.getStatus() == EnumStatus.started).collect(Collectors.toList());
		if (items.isEmpty()) {
			System.err.println();
			System.err.println("---------------------------");
			System.err.println("None active auctions left");
			System.err.println("---------------------------");
		} else {
			System.err.println();
			System.err.println("---------------------------");
			System.err.println("---Active auctions---");
			items.stream().forEach(p -> System.err.println(p.getItem().getName()));
			System.err.println("---------------------------");
		}
	}

	/**
	 * This method is used to check if an auction has finished by checking the
	 * status of an Auction. Then the closing date/time is compared against the 
	 * current date/time. If the time for the closing date/time is earlier than 
	 * the current date/time, then the finish auction method is executed for 
	 * the particular Auction.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(time);
				synchronized (auctions) {
					auctions.stream().filter(p -> p.getStatus().equals(EnumStatus.started))
							.filter(p -> p.getCloseDate().isBefore(LocalDateTime.now())).forEach(p -> finishAuction(p));
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
