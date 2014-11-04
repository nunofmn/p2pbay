package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class InvalidBidValueException extends RuntimeException{

    public InvalidBidValueException() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param value
     */
    public InvalidBidValueException(final double currentValue, final double bitValue) {
        super("Item value: " + currentValue+ " but, new bit value is: " + bitValue);
    }


}

