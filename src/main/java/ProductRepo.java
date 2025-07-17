import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {
    private final List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> getProductById(String id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }

    public Product addProduct(Product newProduct) {
        products.add(newProduct);
        return newProduct;
    }

    public void removeProduct(String id) {
        for (Product product : products) {
           if (product.id().equals(id)) {
               products.remove(product);
               return;
           }
        }
    }
}
