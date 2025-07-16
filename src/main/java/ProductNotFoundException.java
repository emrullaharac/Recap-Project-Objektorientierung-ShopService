public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Product mit der Id:" + productId + " konnte nicht bestellt werden!");
    }
}
