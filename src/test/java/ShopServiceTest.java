import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN & THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }
}
