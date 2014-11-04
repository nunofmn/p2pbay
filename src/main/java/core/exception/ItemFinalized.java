package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class ItemFinalized extends RuntimeException{


    public ItemFinalized() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param title the message
     */
    public ItemFinalized(final String title) {
        super("Item: " +title + " is already finalized");
    }

}
