import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

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

    public Optional<Order> updateOrder(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderRepo.removeOrder(orderId);
        Order updatedOrder = order.withStatus(newStatus);
        orderRepo.addOrder(updatedOrder);
        return Optional.of(updatedOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        return orderRepo.getOrders().stream()
                .filter(o -> o.status().equals(orderStatus))
                .toList();
    }
}
