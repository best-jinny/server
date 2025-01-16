package kr.hhplus.be.server.product.domain;

import jakarta.persistence.EntityNotFoundException;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResult> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // todo 정렬?
        return productRepository.findWithStock(pageable);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
    }

    public void validateProductPrices(List<ValidateProductPriceCommand> commands) {
        for (ValidateProductPriceCommand orderLineRequest : commands) {
            Product product = getProduct(orderLineRequest.getProductId());
            product.validatePrice(orderLineRequest.getPrice());
        }
    }
}
