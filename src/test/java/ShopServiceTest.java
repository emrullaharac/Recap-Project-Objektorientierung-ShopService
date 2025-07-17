import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import model.Order;
import model.OrderStatus;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import repo.OrderMapRepo;
import repo.OrderRepo;
import repo.ProductRepo;
import service.IdService;
import service.ShopService;
import service.UuidService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    private ProductRepo productRepo;
    private OrderRepo orderRepo;
    private IdService idService;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepo();
        orderRepo = new OrderMapRepo();
        idService = new UuidService();
        shopService = new ShopService(productRepo, orderRepo, idService);
    }

    @Test
    void addOrderTest() {
        // GIVEN
        productRepo.addProduct(new Product("1", "Apfel"));
        List<String> productsIds = List.of("1");

        // WHEN
        Optional<Order> actual = shopService.addOrder(productsIds);

        // THEN
        assertTrue(actual.isPresent());
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, actual.get().orderDate());
        assertEquals(expected.products(), actual.get().products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        // GIVEN
        productRepo.addProduct(new Product("1", "Apfel"));
        List<String> productsIds = List.of("1", "2");

        // WHEN & THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void updateOrder_shouldThrowException_ifOrderNotFound() {
        // WHEN & THEN
        assertThrows(OrderNotFoundException.class, () -> shopService.updateOrder("X", OrderStatus.COMPLETED));
    }

    @Test
    void getOldestOrderPerStatus_returnsOldestOrderForEachStatus() {
        // GIVEN
        productRepo.addProduct(new Product("1", "Apfel"));

        Order o1 = new Order("A", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.parse("2024-07-01T10:00:00Z"));
        Order o2 = new Order("B", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.parse("2024-07-02T10:00:00Z"));
        Order o3 = new Order("C", List.of(new Product("1", "Apfel")), OrderStatus.COMPLETED, Instant.parse("2024-07-01T08:00:00Z"));
        Order o4 = new Order("D", List.of(new Product("1", "Apfel")), OrderStatus.COMPLETED, Instant.parse("2024-07-02T07:00:00Z"));

        orderRepo.addOrder(o1);
        orderRepo.addOrder(o2);
        orderRepo.addOrder(o3);
        orderRepo.addOrder(o4);

        // WHEN
        Map<OrderStatus, Order> oldestPerStatus = shopService.getOldestOrderPerStatus();

        // THEN
        assertEquals(2, oldestPerStatus.size());
        assertEquals(o1, oldestPerStatus.get(OrderStatus.PROCESSING));
        assertEquals(o3, oldestPerStatus.get(OrderStatus.COMPLETED));
    }
}
