package system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

import eAuction.Admin;
import eAuction.Auction;
import eAuction.Bid;
import eAuction.Buyer;
import eAuction.CheckTheStatus;
import eAuction.EnumItemCondition;
import eAuction.EnumStatus;
import eAuction.Item;
import eAuction.Seller;
import eAuction.User;

/**
 * eAuction system that allows members of the service to buy and sell various items by a bidding system. 
 * Each member of the service might be either a seller or buyer. 
 * The whole system is based on multithreading and concurrency, so multiple auctions are going on in the background. 
 * The users are always serialized and written into a file where the objects are represented by a sequence of bytes.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since 2019-03-15
 */
public final class eAuctionSystem {

	private final static Scanner scan = new Scanner(System.in);

	private Buyer buyer;
	private Seller seller;
	private static Admin admin;
	private String pathUsers = "src/system/users.ser";
	private List<User> users;
	private static List<Auction> auctions = Collections.synchronizedList(new LinkedList<Auction>());
	private static CheckTheStatus checkTheStatus = new CheckTheStatus(auctions, 3);

	/**
	 * The method that is responsible for initial settings, such as calling method that is responsible 
	 * for deserialisation of existing users, placing some auctions with items, and starting an additional 
	 * thread which is responsible for constantly checking the status of auctions.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	public eAuctionSystem() {
		deSerialize();

		// Adding new users
//		users.add(new Buyer("Bob", "123"));
//		users.add(new Buyer("Bill", "123"));
//		users.add(new Buyer("Ben", "123"));
//		users.add(new Seller("Steve", "123"));
//		users.add(new Seller("Simon", "123"));
//		users.add(new Admin("Glyn", "123a"));

		try {
			// Adding new items
//			Seller.class.cast(users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()).addItem(new Item("Ball", "Blue ball", EnumItemCondition.neW));
//			Seller.class.cast(users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get()).addItem(new Item("Table", "Big round table", EnumItemCondition.used));
//			Seller.class.cast(users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()).addItem(new Item("Playing Cards", "Disney Edition", EnumItemCondition.nearlyNew));
//			Seller.class.cast(users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get()).addItem(new Item("Vase", "Hourglass, Tinted pink", EnumItemCondition.used));
//			Seller.class.cast(users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()).addItem(new Item("Car", "Audi A6 2006", EnumItemCondition.spareOrRepair));

			// Adding new auctions
			Auction newAuction = new Auction(10, 14, LocalDateTime.now().plusSeconds(20),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get())
							.getItemName("Ball"),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()));
			newAuction.verify();
			auctions.add(newAuction);

			newAuction = new Auction(20, 30, LocalDateTime.now().plusSeconds(35),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get())
							.getItemName("Table"),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get()));
			newAuction.verify();
			auctions.add(newAuction);

			newAuction = new Auction(10, 12, LocalDateTime.now().plusSeconds(50),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get())
							.getItemName("Playing Cards"),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()));
			newAuction.verify();
			auctions.add(newAuction);

			newAuction = new Auction(51, 64, LocalDateTime.now().plusSeconds(120),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get())
							.getItemName("Vase"),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Simon")).findFirst().get()));
			newAuction.verify();
			auctions.add(newAuction);

			newAuction = new Auction(500, 650, LocalDateTime.now().plusSeconds(300),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get())
							.getItemName("Car"),
					Seller.class.cast(
							users.stream().filter(p -> p.getUsername().equalsIgnoreCase("Steve")).findFirst().get()));
			newAuction.verify();
			auctions.add(newAuction);

		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(checkTheStatus).start();
	}

	/**
	 * The method that runs the main menu of the system before any user is logged in.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 * @throws Exception It appears when the method is not able to access the setupAccount or login method.
	 */
	public void runMainMenu() throws Exception {
		String choice;
		do {
			System.out.println();
			System.out.println("---Main Menu---");
			System.out.println("1. Create Account");
			System.out.println("2. Login");
			System.out.println("3. Browse Auctions");
			System.out.println("4. Quit");
			System.out.println();
			System.out.print("Please select an option from the menu above: ");
			choice = scan.nextLine().toUpperCase();

			switch (choice) {
			case "1": {
				setupAccount();
				break;
			}
			case "2": {
				login();
				break;
			}
			case "3": {
				browseAuction();
				break;
			}
			case "4": {
				System.out.println("---------------------------");
				System.out.println("See You Again Soon!");
				System.out.println("---------------------------");
				buyer = null;
				seller = null;
				serialize();
				System.exit(0);
				break;
			}
			default:
				System.out.println("---------------------------");
				System.out.println("Please choose an option from the menu displayed");
				System.out.println("---------------------------");
				break;
			}

		} while (!choice.equals("4"));

	}

	/**
	 * The method which is responsible for searching and displaying any active auctions.
	 * 
	 * @return Boolean value whether any auctions or active or not.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private boolean browseAuction() {
		List<Auction> auctionsStarted = auctions.stream().filter(p -> p.isBlocked() == false)
				.filter(p -> p.getStatus() == EnumStatus.started).collect(Collectors.toList());

		if (auctionsStarted.isEmpty()) {
			System.out.println("---------------------------");
			System.out.println("None active auctions.");
			System.out.println("---------------------------");
			return false;
		} else {
			System.out.println();
			System.out.println("---Active Auctions---");
			auctionsStarted.stream()
					.forEach(p -> System.out.printf(
							"Item: %s, Condition: %s, Start Price: �%.2f, Time left: %s"
									+ (p.getLastBid() != 0 ? ", Last bid: �%.2f%n" : ", No bids so far%n"),
							p.getItem().getName(), getCondition(p.getItem().getCondition()), p.getStartPrice(),
							getTimeLeft(p.getCloseDate()), p.getLastBid()));
			return true;
		}
	}
	
	/**
	 * The method that compares the provided time as a parameter with the current zone time and returns the time left.
	 * 
	 * @param closeDate - Close time provided for one of the auctions
	 * @return String of the time left after the calculations.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private String getTimeLeft(LocalDateTime closeDate) {
		Date now = new Date();
		Date closeDateConverted = new Date().from(closeDate.atZone(ZoneId.systemDefault()).toInstant());

		long difference = closeDateConverted.getTime() - now.getTime();
		return "(Days: " + (difference / (24 * 60 * 60 * 1000)) + ", Hours: " + (difference / (60 * 60 * 1000) % 24)
				+ ", Minutes: " + (difference / (60 * 1000) % 60) + ", Seconds: " + (difference / 1000 % 60) + ")";
	}

	/**
	 * The method that is responsible for converting enum condition status into the string.
	 * 
	 * @param condition - Enum condition status due to converting.
	 * @return String of converted condition status.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private String getCondition(EnumItemCondition condition) {
		switch (condition) {
		case used: {
			return "Used";
		}
		case nearlyNew: {
			return "Nearly new";
		}
		case neW: {
			return "New";
		}
		case spareOrRepair: {
			return "Spare Or Repair";
		}
		}
		return null;
	}

	/**
	 * The method that deserializes the file which consists of serialized data, and then inserts all the data into the array.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void deSerialize() {
		ObjectInputStream stream;
		try {
			stream = new ObjectInputStream(new FileInputStream(pathUsers));
			users = (List<User>) stream.readObject();
			stream.close();
		} catch (Exception e) {
			System.out.printf("Error: %s%n", e.getMessage());
		}
	}

	/**
	 * The method that serializes the data and saves it back to the file. 
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void serialize() {
		ObjectOutputStream stream;
		try {
			stream = new ObjectOutputStream(new FileOutputStream(pathUsers));
			stream.writeObject(users);
			stream.close();
		} catch (Exception e) {
			System.out.printf("Error: %s%n", e.getMessage());
		}
	}

	/**
	 * The method that is responsible for displaying the menu of registration. 
	 * 
	 * @throws Exception It appears when the method is not able to access the sellerMenu or buyerMenu method.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void setupAccount() throws Exception {
		String username;
		String register;
		do {
			System.out.println();
			System.out.println("---Registration---");
			System.out.print("Please type in a username: ");
			username = scan.nextLine();
			if (getUser(username) != null) {
				System.out.println("---------------------------");
				System.out.println("Username: " + username + " is already taken!");
				System.out.println("Please choose different username!");
				System.out.println("---------------------------");
			}
		} while (getUser(username) != null);
		System.out.print("Please Type in a password: ");
		String password = scan.nextLine();
		do {
			System.out.println("---------------------------");
			System.out.println("Do you want to register as [S]eller or [B]uyer?");
			register = scan.nextLine().toUpperCase();
			switch (register) {
			case "S": {
				seller = new Seller(username, password);
				users.add(seller);
				System.out.println("---------------------------");
				System.out.println("Your seller account has been successfully created!");
				System.out.println("---------------------------");
				sellerMenu();
				return;
			}
			case "B": {
				buyer = new Buyer(username, password);
				users.add(buyer);
				System.out.println("---------------------------");
				System.out.println("Your buyer account has been successfully created!");
				System.out.println("---------------------------");
				buyerMenu();
				return;
			}
			default:
				System.out.println("---------------------------");
				System.out.println("Please type in 'S' for a Seller account or 'B' for a Buyer account!");
				System.out.println("---------------------------");
			}
		} while (true);

	}

	/**
	 * The method that is responsible for displaying the menu of login.
	 * 
	 * @throws Exception It appears when the method is not able to access the sellerMenu or buyerMenu method.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void login() throws Exception {
		System.out.println();
		System.out.println("---Login---");
		System.out.print("Please Enter Your Username: ");
		String username = scan.nextLine();
		System.out.print("Please Enter Your Password: ");
		String password = scan.nextLine();

		User user = getUser(username);

		if (user != null) {
			if (user.checkPassword(password)) {
				if (Buyer.class.isInstance(user)) {
					buyer = (Buyer) user;
					buyerMenu();
				} else if (Seller.class.isInstance(user)) {
					seller = (Seller) user;
					if (seller.isBlocked()) {
						System.out.println("---------------------------");
						System.out.println("This seller is blocked!");
						System.out.println("Please Contact the Site Admin for more Information");
						System.out.println("---------------------------");
						seller = null;
					} else {
						sellerMenu();
					}
				} else {
					admin = (Admin) user;
					adminMenu();
				}
			} else {
				loginoutor();
			}
		} else {
			loginoutor();
		}
	}

	private void loginoutor() {
		System.out.println("---------------------------");
		System.out.println("Username or password is incorrect!");
		System.out.println("You will be redirected to the Main Menu");
		System.out.println("---------------------------");
	}

	private User getUser(String username) {
		return users.stream().filter(p -> p.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
	}

	private Auction getAuction(String auction) {
		return auctions.stream().filter(p -> p.getItem().getName().equalsIgnoreCase(auction)).findFirst().orElse(null);
	}

	/**
	 *  The method that is responsible for displaying the menu for sellers and some of the actions that the seller can take,
	 *  such as displaying sold items and displaying pending auctions. 
	 * 
	 * @throws Exception It appears when the method is not able to access the placeAuction method.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void sellerMenu() throws Exception {
		String sellerChoice;
		do {
			System.out.println();
			System.out.printf("---%s's Seller Menu---%n", seller.getUsername());
			System.out.println("1. Display active auctions");
			System.out.println("2. Sell an item");
			System.out.println("3. See sold items");
			System.out.println("4. Display auctions in pending");
			System.out.println("5. Logout");
			System.out.println();
			System.out.print("Please select an option from the menu above: ");
			sellerChoice = scan.nextLine();
			switch (sellerChoice) {
			case "1": {
				browseAuction();
				break;
			}
			case "2": {
				placeAuction();
				break;
			}
			case "3": {
				List<Auction> finishedAuctions = auctions.stream().filter(p -> p.getSeller().equals(seller))
						.filter(p -> p.getStatus().equals(EnumStatus.closed))
						.filter(p -> p.getLastBid() >= p.getReservePrice()).filter(p -> p.isBlocked() == false)
						.collect(Collectors.toList());

				if (finishedAuctions.isEmpty()) {
					System.out.println("---------------------------");
					System.out.println("None sold items.");
					System.out.println("---------------------------");
				} else {
					System.out.println("");
					System.out.println("---Sold items---");
					finishedAuctions.stream().forEach(p -> System.out.printf("Item: %s, Sold for: �%.2f%n",
							p.getItem().getName(), p.getLastBid()));
				}

				break;
			}
			case "4": {
				List<Auction> auction = auctions.stream()
						.filter(p -> p.getSeller().getUsername().equals(seller.getUsername()))
						.filter(p -> p.getStatus() == EnumStatus.pending).collect(Collectors.toList());

				if (auction.isEmpty()) {
					System.out.println("---------------------------");
					System.out.println("None auctions to verify.");
					System.out.println("---------------------------");
				} else {
					System.out.println();
					System.out.println("Auctions that were not verified: ");
					auction.stream().forEach(p -> System.out.println(p.getItem().getName()));
					String verify;
					System.out.println();
					System.out.print("Do you want to verify any of them? Type in [Y] if yes: ");
					verify = scan.nextLine().toUpperCase();
					if (verify.equals("Y")) {
						String item;
						do {
							System.out.println();
							System.out.print("Please type in which one you want to veify: ");
							item = scan.nextLine();
							final String finalItem = item;
							if (auction.stream().anyMatch(p -> p.getItem().getName().equalsIgnoreCase(finalItem))) {
								auctions.stream().filter(p -> p.getSeller().getUsername().equals(seller.getUsername()))
										.filter(p -> p.getStatus() == EnumStatus.pending)
										.filter(p -> p.getItem().getName().equalsIgnoreCase(finalItem)).findAny()
										.ifPresent(p -> p.setStatus(EnumStatus.started));

								System.out.println("---------------------------");
								System.out.println("All done!");
								System.out.println("---------------------------");
								break;
							} else {
								System.out.println("---------------------------");
								System.out.println("Provided item doesn't exist!");
								System.out.println("Please type in valid item.");
								System.out.println("---------------------------");
							}
						} while (true);
					} else {
						System.out.println("---------------------------");
						System.out.println("Returning to the main menu");
						System.out.println("---------------------------");
					}
				}
				break;
			}
			case "5": {
				System.out.println("---------------------------");
				System.out.println("Logout successful");
				System.out.println("Returning to the Main Menu");
				System.out.println("---------------------------");
				seller = null;
				break;
			}
			default:
				System.out.println("---------------------------");
				System.out.println("Please choose from the options displayed");
				System.out.println("---------------------------");
			}
		} while (seller != null);

	}

	/**
	 * The method that is responsible for the management of sellers by typing in the username.
	 * The seller can be blocked and unblocked by the admin.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void sellerManag() {
		String username;
		String choice;
		User user;
		do {
			System.out.println();
			System.out.print("Please type in the username of seller you want to manage: ");
			username = scan.nextLine();
			user = getUser(username);
			if (user != null) {
				if (Seller.class.isInstance(user)) {
					seller = (Seller) user;
					System.out.println();
					System.out.printf("---Seller %s management---%n", user.getUsername());
					System.out.printf("%s is currectly %s.%n", user.getUsername(),
							seller.isBlocked() ? "blocked" : "active");
					System.out.printf("1. %s%n", seller.isBlocked() ? "Unblock" : "Block");
					System.out.println("2. Quit");
					System.out.println("");
					System.out.print("Please select an option from the menu above: ");
					choice = scan.nextLine().toUpperCase();
					switch (choice) {
					case "1": {
						if (seller.isBlocked()) {
							seller.setActive();
							System.out.println("---------------------------");
							System.out.println("Seller succesfully unblocked!");
							System.out.println("Back to the main menu!");
							System.out.println("---------------------------");
							seller = null;
						} else {
							seller.setBlocked();
							System.out.println("---------------------------");
							System.out.println("Seller succesfully blocked!");
							System.out.println("Back to the main menu!");
							System.out.println("---------------------------");
							seller = null;
						}
						return;
					}
					case "2": {
						System.out.println("---------------------------");
						System.out.println("Back to the Admin menu!");
						System.out.println("---------------------------");
						return;
					}
					default: {
						System.out.println("---------------------------");
						System.out.println("Please type in a number from the menu!");
						System.out.println("---------------------------");
					}
					}
				} else {
					System.out.println("---------------------------");
					System.out.println("This username isn't a seller!");
					System.out.println("Please enter valid username.");
					System.out.println("---------------------------");
				}
			} else {
				System.out.println("---------------------------");
				System.out.println("This username doesn't exist!");
				System.out.println("Please enter valid username.");
				System.out.println("---------------------------");
			}
		} while (true);
	}

	/**
	 * The method that is responsible for the management of auctions by typing in the item that is assigned
	 * to the specified auction.
	 * The auction can be blocked and unblocked by the admin.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void auctionManag() {
		String item;
		String choice;
		Auction auction;
		String confirm;
		do {
			System.out.println();
			System.out
					.println("Please type in item's name which is assigned to the auction you would like to manage: ");
			item = scan.nextLine();
			auction = getAuction(item);
			if (auction != null) {
				do {
					System.out.println("---------------------------");
					System.out.println("Name of the item: " + auction.getItem().getName() + ", descritpion: "
							+ auction.getItem().getDescription() + ", last bid: " + auction.getLastBid());
					System.out.print(
							"Please confirm that the auction above is the one you want to manage by typing in [Y]es or [N]o: ");
					confirm = scan.nextLine().toUpperCase();
					switch (confirm) {
					case "Y": {
						do {
							System.out.println();
							System.out.printf("---%s's auction management---%n", auction.getItem().getName());
							System.out.printf("Auction with item %s is currectly %s%n", auction.getItem().getName(),
									auction.isBlocked() ? "blocked" : "active");
							System.out.printf("1. %s%n", auction.isBlocked() ? "Unblock" : "Block");
							System.out.println("2. Quit");
							System.out.println("");
							System.out.print("Please select an option from the menu above: ");
							choice = scan.nextLine().toUpperCase();
							switch (choice) {
							case "1": {
								if (auction.isBlocked()) {
									auction.setStatus(EnumStatus.active);
									System.out.println("---------------------------");
									System.out.println("Auction succesfully unblocked!");
									System.out.println("Back to the main menu!");
									System.out.println("---------------------------");
								} else {
									auction.setBlocked();
									System.out.println("---------------------------");
									System.out.println("Auction succesfully blocked!");
									System.out.println("Back to the main menu!");
									System.out.println("---------------------------");
								}
								return;
							}
							case "2": {
								System.out.println("---------------------------");
								System.out.println("Back to the Admin menu!");
								System.out.println("---------------------------");
								return;
							}
							default: {
								System.out.println("---------------------------");
								System.out.println("Please type in a number from the menu!");
								System.out.println("---------------------------");
							}
							}
						} while (true);
					}
					case "N": {
						System.out.println("---------------------------");
						System.out.println("Back to the main menu!");
						System.out.println("---------------------------");
						return;
					}
					default: {
						System.out.println("---------------------------");
						System.out.println("Please type in a number from the menu!");
						System.out.println("---------------------------");
					}
					}
				} while (true);
			} else {
				System.out.println("---------------------------");
				System.out.println("Provided auction doesn't exist!");
				System.out.println("Please type in valid name of item!");
				System.out.println("---------------------------");
			}
		} while (true);
	}

	/**
	 * The method that is responsible for the management of thread sleep duration for checking status. 
	 * The current duration is showed to the admin, and then the admin can increase it
	 * or decrease it.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void setDuration() {
		String choice = null;
		do {
			System.out.println();
			System.out.println("Current duration is: " + checkTheStatus.getTime() + "sec");
			System.out.print("Please type in [Y] if you would like to change it or [N] if not: ");
			choice = scan.nextLine().toUpperCase();
			switch (choice) {
			case "Y": {
				int newDuration = 0;
				do {
					System.out.println();
					System.out.println("Type in new duration you want to set: ");
					try {
						newDuration = scan.nextInt();
					} catch (Exception e) {
						System.out.println("---------------------------");
						System.out.println("Please type in valid duration!");
						System.out.println("---------------------------");
					}
					if (newDuration <= 0) {
						System.out.println("---------------------------");
						System.out.println("Please provide duration higher than 0!");
						System.out.println("---------------------------");
					}
					scan.nextLine();
				} while (newDuration <= 0);
				checkTheStatus.setTime(newDuration * 100);
				System.out.println("---------------------------");
				System.out.println("The duration has succesfully been changed!");
				System.out.println("---------------------------");
				break;
			}
			case "N": {
				System.out.println("---------------------------");
				System.out.println("Back to the main menu!");
				System.out.println("---------------------------");
				break;
			}
			default: {
				System.out.println("---------------------------");
				System.out.println("Please type in valid input [Y/N]");
				System.out.println("---------------------------");
			}
			}
		} while (!(choice.equals("N")) && !(choice.equals("Y")));
	}

	/**
	 * The method that is responsible for displaying the menu for admins.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void adminMenu() {
		String adminChoice;
		do {
			System.out.println();
			System.out.printf("---%s's Admin Menu---%n", admin.getUsername());
			System.out.println("1. Block/unblock a seller");
			System.out.println("2. Block/unblock an auction");
			System.out.println("3. Set duration of the second thread");
			System.out.println("4. View Sold Items");
			System.out.println("5. Logout");
			System.out.print("Please select an option from the menu above: ");
			adminChoice = scan.nextLine();
			switch (adminChoice) {
			case "1": {
				sellerManag();
				break;
			}
			case "2": {
				auctionManag();
				break;
			}
			case "3": {
				setDuration();
				break;
			}
			case "4": {
				viewSoldItems();
				break;
			}
			case "5": {
				System.out.println("---------------------------");
				System.out.println("Log Out Succesful!");
				System.out.println("Returning to Main Menu");
				System.out.println("---------------------------");
				admin = null;
				break;
			}
			}
		} while (admin != null);
	}

	/**
	 * The method that is responsible for displaying all sold items.
	 * 
	 * @return Boolean that represents whether there are some sold items or not
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private boolean viewSoldItems() {
		List<Auction> auctionsSold = auctions.stream().filter(p -> p.isBlocked() == false)
				.filter(p -> p.getStatus() == EnumStatus.closed).filter(p -> p.getLastBid() >= p.getReservePrice())
				.collect(Collectors.toList());

		if (auctionsSold.isEmpty()) {
			System.out.println("---------------------------");
			System.out.println("No Sold Items to Display.");
			System.out.println("---------------------------");
			return false;
		} else {
			System.out.println();
			System.out.println("---Sold Items---");
			auctionsSold.stream().forEach(
					p -> System.out.printf("Item: %s, Sold For: �%.2f%n", p.getItem().getName(), p.getLastBid()));
			return true;
		}
	}

	/**
	 *  The method that is responsible for displaying the menu for buyers and some of the actions that the buyer can take,
	 *  such as displaying lost and won items by the user.
	 * 
	 * @throws Exception It appears when the method is not able to access the buyItem method.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void buyerMenu() throws Exception {
		String buyerChoice;
		do {
			System.out.println();
			System.out.printf("---%s's Buyer Menu---%n", buyer.getUsername());
			System.out.println("1. Display active auctions");
			System.out.println("2. Buy item");
			System.out.println("3. See won and lost auctions");
			System.out.println("4. Logout");
			System.out.println();
			System.out.print("Please select an option from the menu above: ");
			buyerChoice = scan.nextLine();
			switch (buyerChoice) {
			case "1": {
				browseAuction();
				break;
			}
			case "2": {
				buyItem();
				break;
			}
			case "3": {
				List<Auction> wonAuctions = buyer.getWonAuctions().stream().collect(Collectors.toList());

				if (wonAuctions.isEmpty()) {
					System.out.println("---------------------------");
					System.out.println("None won auctions.");
					System.out.println("---------------------------");
				} else {
					System.out.println();
					System.out.println("---Won auctions---");
					wonAuctions.stream().forEach(p -> System.out.printf("Item: %s%n", p.getItem().getName()));
				}

				List<Auction> lostAuctions = buyer.getLostAuctions().stream().collect(Collectors.toList());

				if (lostAuctions.isEmpty()) {
					System.out.println("---------------------------");
					System.out.println("None lost auctions.");
					System.out.println("---------------------------");
				} else {
					System.out.println();
					System.out.println("---Lost auctions---");
					lostAuctions.stream().forEach(p -> System.out.printf("Item: %s%n", p.getItem().getName()));
				}

				break;
			}
			case "4": {
				System.out.println("---------------------------");
				System.out.println("Logout Successful");
				System.out.println("Returning to the Main Menu");
				System.out.println("---------------------------");
				buyer = null;
				break;
			}
			default:
				System.out.println("---------------------------");
				System.out.println("Please choose from the options displayed");
				System.out.println("---------------------------");
				break;
			}
		} while (buyer != null);
	}

	/**
	 * The method that is responsible for the process of bidding an item. It takes the user step-by-step
	 * through the whole process, taking the name of the item that the user wants to buy and then taking
	 * the bid and checking if it's valid against the minimum and maximum amount of price that can be 
	 * provided.
	 * 
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void buyItem() {
		do {
			if (browseAuction() == true) {
				System.out.println("");
				System.out.print("Please type in an item which you want to buy: ");
				String item = scan.nextLine();

				Auction auction = auctions.stream().filter(p -> p.getItem().getName().equalsIgnoreCase(item))
						.findFirst().orElse(null);

				if (auction != null) {
					double upper = (Math.round(auction.getUpperBiddingIncrement() * 100.00) / 100.00);
					double lower = (Math.round(auction.getLowerBiddingIncrement() * 100.00) / 100.00);
					double userPrice = 0;

					do {
						System.out.println();
						System.out.printf(
								"Start price: �%.2f"
										+ (auction.getLastBid() == 0 ? ", none bids%n" : ", Latest bid: �%.2f%n"),
								auction.getStartPrice(), auction.getLastBid());
						System.out.printf("To place a bid enter a price between �%.2f and �%.2f%n", lower, upper);
						System.out.print("Please type in your bid: �");
						try {
							userPrice = scan.nextDouble();
							scan.nextLine();
						} catch (Exception e) {
							System.out.println("---------------------------");
							System.out.println("Please provide valid bid!");
							System.out.println("---------------------------");
						}
						if (userPrice >= lower && userPrice <= upper) {
							if (auction.getStatus() == EnumStatus.closed) {
								System.out.println("---------------------------");
								System.out.println("Auction already closed");
								System.out.println("---------------------------");
							} else {
								auction.placeBid(userPrice, buyer, LocalDateTime.now());
								System.out.println("---------------------------");
								System.out.printf("Success! You are currently the highest bidder with �%.2f%n",
										userPrice);
								System.out.println("---------------------------");
							}
							return;
						} else {
							System.out.println("---------------------------");
							System.out.printf(
									"Attempted to set price (%.2f) which is not within lower and upper bidding increments!%n",
									userPrice);
							System.out.println("---------------------------");
						}
					} while (true);
				} else {
					System.out.println("---------------------------");
					System.out.printf("Attempted to bid on item: %s - which doesn't exist!%n", item);
					System.out.println("Please Try Again");
					System.out.println("---------------------------");
				}
			} else {
				break;
			}
		} while (true);
	}

	/**
	 * The method that is responsible for checking the format of the data and time provided by the user
	 * for the purpose of close date and time for the specific auction. The data and time need to 
	 * be checked against the rule that it cannot be set on later than 7 days from the current date.
	 * 
	 * @param date The date and time provided by the user.
	 * @return LocalDateTime data format which consists date and time provided by the user if it's valid, otherwise current date + 7 days.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private LocalDateTime dateFormatCheck(String date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		try {
			LocalDateTime dateAuction = LocalDateTime.parse(date, format);
			String result = dateAuction.format(format);
			if (result.equals(date)) {
				LocalDateTime maxDate = LocalDateTime.now().plusDays(7);
				if (dateAuction.isAfter(maxDate)) {
					System.out.println("---------------------------");
					System.out.println(
							"Provided data is not within the nearest 7 days, Closing date automatically adjusted:");
					System.out.println(maxDate);
					System.out.println("---------------------------");
					return maxDate;
				} else {
					return dateAuction;
				}
			} else {
				return null;
			}
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	/**
	 * The method that is responsible for placing the auction with an item. It consists of a number of steps, 
	 * such as choosing the item, adding description, providing condition, price, reserve price, and close date and time.
	 * At the very end of the whole process, the seller has an opportunity to review the whole auction before confirming it.
	 * 
	 * @throws Exception It appears when the method is not able to access the Auction object.
	 * @author Maksymilian Kawula, Philip Hughes
	 */
	private void placeAuction() throws Exception {
		String nameItem;
		System.out.println();
		System.out.println("---Place an Auction---");
		do {
			System.out.print("Please type in the name of an item you want to sell: ");
			nameItem = scan.nextLine();
			if (nameItem.length() <= 1) {
				System.out.println("---------------------------");
				System.out.println("Please enter a valid item name (at least 1 character)");
				System.out.println("---------------------------");
			}
		} while (nameItem.length() <= 1);
		String descriptionItem;
		do {
			System.out.print("Please type in description of the item you want to sell: ");
			descriptionItem = scan.nextLine();
			if (descriptionItem.length() <= 1) {
				System.out.println("---------------------------");
				System.out.println("Please enter a valid item description (at least 1 character)");
				System.out.println("---------------------------");
			}
		} while (descriptionItem.length() <= 1);
		String conditionUser;
		EnumItemCondition itemCondition = null;
		do {
			System.out.println();
			System.out.println("Please choose condition of the item: ");
			System.out.println("1. New");
			System.out.println("2. Nearly new");
			System.out.println("3. Used");
			System.out.println("4. Spare or repair");
			conditionUser = scan.nextLine();
			switch (conditionUser) {
			case "1": {
				itemCondition = EnumItemCondition.neW;
				break;
			}
			case "2": {
				itemCondition = EnumItemCondition.nearlyNew;
				break;
			}
			case "3": {
				itemCondition = EnumItemCondition.used;
				break;
			}
			case "4": {
				itemCondition = EnumItemCondition.spareOrRepair;
				break;
			}
			default: {
				System.out.println("---------------------------");
				System.out.println("Please enter ");
				System.out.println("---------------------------");
			}
			}
		} while (itemCondition == null);
		Item newItem = new Item(nameItem, descriptionItem, itemCondition);
		seller.addItem(newItem);
		System.out.println("---------------------------");
		System.out.println("This item has succesfully been added to your account!");
		System.out.println("---------------------------");

		double startPrice = 0;
		do {
			System.out.println();
			System.out.print("Please type in the price you wish the item to start at: �");
			try {
				startPrice = scan.nextDouble();
				if (startPrice > 0) {
					System.out.println("---------------------------");
					System.out.println("Start Price Set!");
					System.out.println("---------------------------");
					scan.nextLine();
					break;
				} else {
					System.out.println("---------------------------");
					System.out.println("Start price must be higher than 0!");
					System.out.println("---------------------------");
				}
			} catch (Exception InputMisMatch) {
				System.out.println("---------------------------");
				System.out.println("Please enter a valid price!");
				System.out.println("---------------------------");
			}
			scan.nextLine();
		} while (true);

		double reservePrice = 0;
		do {
			System.out.println();
			System.out.print("Please type in the reserve price you wish the item to not sell for less than: �");
			try {
				reservePrice = scan.nextDouble();
				if (reservePrice > startPrice) {
					// System.out.println(startPrice + " " + reservePrice);
					System.out.println();
					System.out.println("---------------------------");
					System.out.println("Reserve Price Set!");
					System.out.println("---------------------------");
					scan.nextLine();
					break;
				} else {
					System.out.println("---------------------------");
					System.out.println("Reserve price must be higher than the start price!");
					System.out.println("---------------------------");
				}
			} catch (Exception InputMisMatch) {
				System.out.println("---------------------------");
				System.out.println("Please enter a valid Reserve Price");
				System.out.println("---------------------------");
			}
			scan.nextLine();
		} while (true);

		LocalDateTime closeDate = null;
		String date;
		do {
			System.out.println();
			System.out
					.println("Please type in date and time you want to finish the auction. Format (DD/MM/YYYY HH:MM)");
			System.out.println("Auction must end within the nearest 7 days!");
			System.out.print("Please type in the date here: ");
			date = scan.nextLine();
			closeDate = dateFormatCheck(date);
			if (closeDate == null) {
				System.out.println("------------------------------------");
				System.out.println("Please type in the date and time in the right format (DD/MM/YYYY HH:MM)");
				System.out.println("------------------------------------");
			}
		} while (closeDate == null);
		Auction newAuction = new Auction(startPrice, reservePrice, closeDate, newItem, seller);

		System.out.println();
		System.out.println("Please verify the auction in order to start it");
		System.out.println("------------------------------------");
		System.out.printf(
				"Item's name: %s , description of the item: %s , starting price: %.2f , reserve price: %.2f%n",
				nameItem, descriptionItem, startPrice, reservePrice);
		String verify;

		do {
			System.out.println();
			System.out.print(
					"Please type in [Y] if you would like to verify the auction now, otherwise please type in [N]: ");
			verify = scan.nextLine().toUpperCase();
			switch (verify) {
			case "Y": {
				newAuction.verify();
				auctions.add(newAuction);
				System.out.println("------------------------------------");
				System.out.println("The auction is now listed!");
				System.out.println("------------------------------------");
				return;
			}
			case "N": {
				auctions.add(newAuction);
				System.out.println("------------------------------------");
				System.out.println("Auction not verified!");
				System.out.println("You can verify any pending auction in the Seller Menu!");
				System.out.println("------------------------------------");
				return;
			}
			default:
				System.out.println("------------------------------------");
				System.out.println("Please type in [Y] for Yes or [N] for No!");
				System.out.println("------------------------------------");
			}
		} while (true);
	}

}
