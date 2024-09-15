package project climate;

/**
 * An exception thrown when a grid point is not on the map.
 *
 * @author Joanna Klukowska
 */
@SuppressWarnings("serial")
public class NotOnMapException extends Exception {
    public NotOnMapException(String message) {
        super(message);
    }
}
