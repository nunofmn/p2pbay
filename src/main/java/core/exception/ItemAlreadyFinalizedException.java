package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class ItemAlreadyFinalizedException extends RuntimeException{


    public ItemAlreadyFinalizedException() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param title the message
     */
    public ItemAlreadyFinalizedException(final String title) {
        super("Item: " +title + " is already finalized");
    }

}
