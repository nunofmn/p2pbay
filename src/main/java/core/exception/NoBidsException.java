package core.exception;

/**
 * Created by Carlos on 04-11-2014.
 */
public class NoBidsException extends RuntimeException{

    public NoBidsException() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param title the message
     */
    public NoBidsException(final String title) {
        super("Item: " +title + " can not be finalized");
    }
}
