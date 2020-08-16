package eAuction;

import java.io.Serializable;

/**
 * The item class is used to store all the information about each item that is
 * about to be sold on the auction site. The data is stored in an item object
 * ready to be accessed by the system.
 * 
 * @author Maksymilian Kawula, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public final class Item implements Serializable {
	private String name;
	private String description;
	private EnumItemCondition condition;

	public Item(String name, String description, EnumItemCondition condition) {
		setDescription(description);
		setName(name);
		setCondition(condition);
	}

	public synchronized String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EnumItemCondition getCondition() {
		return this.condition;
	}

	public void setCondition(EnumItemCondition condition) {
		this.condition = condition;
	}
}
