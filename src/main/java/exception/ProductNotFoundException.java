package exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("de.neuefische.bootcamp240625.model.Product mit der Id:" + productId + " konnte nicht bestellt werden!");
    }
}
