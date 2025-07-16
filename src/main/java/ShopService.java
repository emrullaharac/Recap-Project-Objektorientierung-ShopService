import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Optional<Order> addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            productRepo.getProductById(productId)
                    .ifPresentOrElse(
                            products::add,
                            () -> {throw new ProductNotFoundException(productId);}
                    );
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public void updateOrder(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderRepo.removeOrder(orderId);
        orderRepo.addOrder(order.withStatus(newStatus));
    }

    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        return orderRepo.getOrders().stream()
                .filter(o -> o.status().equals(orderStatus))
                .toList();
    }
}
