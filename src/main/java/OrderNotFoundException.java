public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order mit der Id:" + orderId + " konnte nicht gefunden werden!");
    }
}
