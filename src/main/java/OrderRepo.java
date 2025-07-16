import java.util.List;
import java.util.Optional;

public interface OrderRepo {

    List<Order> getOrders();

    Optional<Order> getOrderById(String id);

    Optional<Order> addOrder(Order newOrder);

    void removeOrder(String id);
}
