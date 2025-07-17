package exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("de.neuefische.bootcamp240625.model.Order mit der Id:" + orderId + " konnte nicht gefunden werden!");
    }
}
