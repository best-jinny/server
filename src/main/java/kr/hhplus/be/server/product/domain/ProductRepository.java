package kr.hhplus.be.server.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(Long id);
    Page<ProductResult> findWithStock(Pageable pageable);
    Product save(Product product);
    void deleteAll();
}
