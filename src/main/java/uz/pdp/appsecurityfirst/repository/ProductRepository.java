package uz.pdp.appsecurityfirst.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appsecurityfirst.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
