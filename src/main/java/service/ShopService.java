package service;

import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import model.Order;
import model.OrderStatus;
import model.Product;
import repo.OrderRepo;
import repo.ProductRepo;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

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

    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        return orderRepo.getOrders().stream()
                .collect(Collectors.groupingBy(
                        Order::status,
                        Collectors.minBy(Comparator.comparing(Order::orderDate))
                )).entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ));
    }
}
