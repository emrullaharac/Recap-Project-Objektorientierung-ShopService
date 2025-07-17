import java.util.*;

public class OrderMapRepo implements OrderRepo{
    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Optional<Order> getOrderById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public Optional<Order> addOrder(Order newOrder) {
        orders.put(newOrder.id(), newOrder);
        return Optional.of(newOrder);
    }

    @Override
    public void removeOrder(String id) {
        orders.remove(id);
    }
}
