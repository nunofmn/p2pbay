package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class ItemNotFinalizedException extends RuntimeException{

    public ItemNotFinalizedException() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param title the message
     */
    public ItemNotFinalizedException(final String title) {
        super("Item: " + title + " is not finalized");
    }

}
