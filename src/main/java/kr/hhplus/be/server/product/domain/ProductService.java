package kr.hhplus.be.server.product.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다"));
    }

    public void validateProductPrices(List<ValidateProductPriceCommand> commands) {
        for (ValidateProductPriceCommand orderLineRequest : commands) {
            Product product = getProduct(orderLineRequest.getProductId());

            // 상품 가격 검증
            if (!product.getPrice().equals(orderLineRequest.getPrice())) {
                throw new IllegalArgumentException("상품 가격이 변경되었습니다" + product.getId());
            }
        }
    }
}
