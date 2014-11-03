package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class ItemNotFinalized extends RuntimeException{

    public ItemNotFinalized() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param title the message
     */
    public ItemNotFinalized(final String title) {
        super("Item: " + title + " is not finalized");
    }

}
