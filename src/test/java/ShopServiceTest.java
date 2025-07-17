import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apfel"));
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new UuidService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);
        List<String> productsIds = List.of("1");

        //WHEN
        Optional<Order> actual = shopService.addOrder(productsIds);

        //THEN
        assertTrue(actual.isPresent());
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, actual.get().orderDate());
        assertEquals(expected.products(), actual.get().products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apfel"));
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new UuidService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);
        List<String> productsIds = List.of("1", "2");

        //WHEN & THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }


    @Test
    void updateOrder_shouldThrowException_ifOrderNotFound() {
        // GIVEN
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apfel"));
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new UuidService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        // WHEN & THEN
        assertThrows(OrderNotFoundException.class, () -> shopService.updateOrder("X", OrderStatus.COMPLETED));
    }

    @Test
    void getOldestOrderPerStatus_returnsOldestOrderForEachStatus() {
        // GIVEN
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apfel"));
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = () -> "test-id";

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

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
        assertEquals(o1, oldestPerStatus.get(OrderStatus.PROCESSING)); // oldest o1 in PROCESSING
        assertEquals(o3, oldestPerStatus.get(OrderStatus.COMPLETED));  // oldest o3 in COMPLETED
    }
}
