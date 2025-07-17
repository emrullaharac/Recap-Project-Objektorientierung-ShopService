import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apfel"));
        OrderRepo orderRepo = new OrderMapRepo();
        ShopService shopService = new ShopService(productRepo, orderRepo);
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
        ShopService shopService = new ShopService(productRepo, orderRepo);
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
        ShopService shopService = new ShopService(productRepo, orderRepo);

        // WHEN & THEN
        assertThrows(OrderNotFoundException.class, () -> shopService.updateOrder("X", OrderStatus.COMPLETED));
    }
}
