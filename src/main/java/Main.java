import model.OrderStatus;
import model.Product;
import repo.OrderMapRepo;
import repo.OrderRepo;
import repo.ProductRepo;
import service.IdService;
import service.ShopService;
import service.UuidService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new UuidService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        productRepo.addProduct(new Product("1", "Apfel"));
        productRepo.addProduct(new Product("2", "Banane"));
        productRepo.addProduct(new Product("3", "Birne"));

        List<String> ids1 = List.of("1", "2");
        List<String> ids2 = List.of("2", "3");
        List<String> ids3 = List.of("1", "3");
        shopService.addOrder(ids1);
        shopService.addOrder(ids2);
        shopService.addOrder(ids3);

        shopService.getOrdersByStatus(OrderStatus.PROCESSING).forEach(System.out::println);
    }
}
