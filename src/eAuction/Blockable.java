package eAuction;

/**
 * This class is used to block an auction or a user.
 * 
 * @author Maksymilian Kawulam, Philip Hughes
 * @version 1.0
 * @since   2019-03-15 
 */
public interface Blockable {

	boolean isBlocked();

	void setBlocked();
}
