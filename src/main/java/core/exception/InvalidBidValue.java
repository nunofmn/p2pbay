package core.exception;

/**
 * Created by Carlos on 03-11-2014.
 */
public class InvalidBidValue extends RuntimeException{

    public InvalidBidValue() {
        super();
    }


    /**
     * Instantiates a new rest exception.
     * @param value
     */
    public InvalidBidValue(final double currentValue, final double bitValue) {
        super("Item value:" + currentValue+ "but bit value is:" + bitValue);
    }


}

